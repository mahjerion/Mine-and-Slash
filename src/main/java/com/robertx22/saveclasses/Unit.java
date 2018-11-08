package com.robertx22.saveclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.robertx22.customitems.bases.IWeapon;
import com.robertx22.database.lists.Rarities;
import com.robertx22.database.lists.StatusEffects;
import com.robertx22.database.rarities.MobRarity;
import com.robertx22.database.stats.types.defense.Armor;
import com.robertx22.database.stats.types.elementals.damage.FireDamage;
import com.robertx22.database.stats.types.elementals.damage.NatureDamage;
import com.robertx22.database.stats.types.elementals.damage.ThunderDamage;
import com.robertx22.database.stats.types.elementals.damage.WaterDamage;
import com.robertx22.database.stats.types.elementals.resist.FireResist;
import com.robertx22.database.stats.types.elementals.resist.NatureResist;
import com.robertx22.database.stats.types.elementals.resist.ThunderResist;
import com.robertx22.database.stats.types.elementals.resist.WaterResist;
import com.robertx22.database.stats.types.offense.CriticalDamage;
import com.robertx22.database.stats.types.offense.CriticalHit;
import com.robertx22.database.stats.types.offense.PhysicalDamage;
import com.robertx22.database.stats.types.resources.Energy;
import com.robertx22.database.stats.types.resources.Health;
import com.robertx22.database.stats.types.resources.Mana;
import com.robertx22.database.status.effects.bases.BaseStatusEffect;
import com.robertx22.effectdatas.DamageEffect;
import com.robertx22.effectdatas.EffectData.EffectTypes;
import com.robertx22.onevent.combat.OnHealDecrease;
import com.robertx22.saveclasses.effects.StatusEffectData;
import com.robertx22.saveclasses.gearitem.StatModData;
import com.robertx22.stats.IAffectsOtherStats;
import com.robertx22.stats.Stat;
import com.robertx22.stats.Trait;
import com.robertx22.uncommon.datasaving.GearSaving;
import com.robertx22.uncommon.datasaving.UnitSaving;
import com.robertx22.uncommon.utilityclasses.HealthUtils;
import com.robertx22.uncommon.utilityclasses.ListUtils;
import com.robertx22.uncommon.utilityclasses.RandomUtils;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Unit implements Serializable {

	public void Save(EntityLivingBase entity) {

		if (entity instanceof EntityPlayer) {
			UnitSaving.Save(entity, this);
		} else {
			if (this.InitialMobSave == false) {
				this.InitialMobSave = true;
				UnitSaving.Save(entity, this);
			}
		}

	}

	public void ReloadStatsAndSave(EntityLivingBase entity) {

		if (entity instanceof EntityPlayer) {
			this.RecalculateStats(entity);
			UnitSaving.Save(entity, this);
		}

	}

	public boolean InitialMobSave = false;

	private static final long serialVersionUID = -6658683548383891230L;
	public int level = 1;

	public Unit() {

		if (Stats == null) {
			Stats = com.robertx22.database.lists.Stats.All;

		} else {
			for (Stat stat : com.robertx22.database.lists.Stats.All.values()) {
				if (!Stats.containsKey(stat.Name())) {
					Stats.put(stat.Name(), stat);
				}
			}
		}

	}

	public HashMap<String, StatusEffectData> statusEffects = new HashMap<String, StatusEffectData>();
	public String GUID = UUID.randomUUID().toString();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Unit) {
			return ((Unit) obj).GUID == this.GUID;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return GUID.hashCode();
	}

	public void BasicAttack(EntityLivingBase source, EntityLivingBase target, Unit unitsource) {
		int num = (int) unitsource.Stats.get(PhysicalDamage.GUID).Value;
		DamageEffect dmg = new DamageEffect(source, target, num);
		dmg.Type = EffectTypes.BASIC_ATTACK;
		dmg.Activate();
	}

	public HashMap<String, Stat> Stats = null;

	// Stat shortcuts
	public Health health() {
		return (Health) Stats.get(new Health().Name());
	}

	public Mana mana() {
		return (Mana) Stats.get(new Mana().Name());
	}

	public Energy energy() {
		return (Energy) Stats.get(new Energy().Name());
	}

	public void SpendMana(int i) {
		mana().Decrease(i);
	}

	public void SpendEnergy(int i) {
		energy().Decrease(i);
	}

	public void RestoreMana(int i) {
		mana().Increase(i);
	}

	public void RestoreEnergy(int i) {
		energy().Increase(i);
	}

	transient public boolean StatsDirty = true;

	public List<GearItemData> GetEquips(EntityLivingBase entity) {

		List<ItemStack> list = new ArrayList<ItemStack>();

		for (ItemStack stack : entity.getArmorInventoryList()) {
			if (stack != null) {
				list.add(stack);
			}
		}
		ItemStack weapon = entity.getHeldItemMainhand();
		if (weapon.getItem() instanceof IWeapon) {
			list.add(weapon);
		}

		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler((EntityPlayer) entity);

		for (int i = 0; i < baubles.getSlots(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack != null) {
				list.add(stack);
			}

		}

		List<GearItemData> gearitems = new ArrayList<GearItemData>();

		for (ItemStack stack : list) {

			GearItemData gear = GearSaving.Load(stack);

			if (gear != null) {
				gearitems.add(gear);

			}

		}

		return gearitems;

	}

	protected void ClearStats() {
		for (Stat stat : Stats.values()) {
			stat.Clear();
		}
	}

	private void AddAllGearStats(EntityLivingBase entity) {

		List<GearItemData> gears = GetEquips(entity);

		for (GearItemData gear : gears) {
			if (gear.level > this.level) {
				entity.sendMessage(
						new TextComponentString(gear.GetDisplayName() + " is too high level for you, no stats added!"));
			} else {

				List<StatModData> datas = gear.GetAllStats(gear.level);
				for (StatModData data : datas) {
					Stat stat = Stats.get(data.GetBaseMod().GetBaseStat().Name());
					if (stat == null) {
						System.out
								.println("Error! can't load a stat called: " + data.GetBaseMod().GetBaseStat().Name());
					} else {
						stat.Add(data, gear.level);

					}
				}
			}
		}
	}

	public void RecalculateStats(EntityLivingBase entity) {

		if (entity instanceof EntityPlayer) {
			// StopWatch watch = new StopWatch();
			// watch.start();
			ClearStats();
			AddPlayerBaseStats();
			AddAllGearStats(entity);
			AddStatusEffectStats();
			CalcStats();
			CalcTraits();
			CalcStats();
			// watch.stop();

		} else {
			ClearStats();
			AddMobcStats();
			SetMobStrengthMultiplier();
			AddStatusEffectStats();
			CalcStats();

		}

	}

	private void AddPlayerBaseStats() {

		Stats.get(Health.GUID).Flat += 10;
		Stats.get(PhysicalDamage.GUID).Flat += 2;

	}

	protected void AddStatusEffectStats() {

		for (StatusEffectData status : this.statusEffects.values()) {
			List<StatModData> datas = status.GetAllStats(this.level);
			for (StatModData data : datas) {
				Stat stat = Stats.get(data.GetBaseMod().GetBaseStat().Name());
				if (stat == null) {
					System.out.println("Error! can't load a stat called: " + data.GetBaseMod().GetBaseStat().Name());
				} else {
					stat.Add(data, level);

				}
			}
		}

	}

	protected void CalcTraits() {
		for (Stat stat : Stats.values()) {

			if (stat instanceof Trait && stat instanceof IAffectsOtherStats) {
				if (stat.Value > 0) {
					IAffectsOtherStats affects = (IAffectsOtherStats) stat;
					affects.TryAffectOtherStats(this);

				}
			}
		}

	}

	protected void CalcStats() {

		Stats.values().forEach((Stat stat) -> stat.CalcVal(this));
	}

	public int vanillaHP;
	public int rarity = 0;

	public static Unit Mob(EntityLivingBase en, int level) {

		Unit mob = new Unit();

		mob.level = level;
		mob.Stats.get(Health.GUID).BaseFlat = (int) en.getMaxHealth();
		mob.rarity = RandomUtils.RandomWithMinRarity(en).Rank();
		mob.vanillaHP = (int) en.getMaxHealth();
		mob.uid = en.getUniqueID();

		mob.AddRandomMobStatusEffects();
		mob.RecalculateStats(en);

		return mob;

	}

	private void AddRandomMobStatusEffects() {

		int max = this.GetRarity().MaxMobEffects();

		if (max > 0) {
			if (this.GetRarity().MaxMobEffects() > StatusEffects.All.values().size()) {
				System.out.println("ERROR! Can't have more unique effects than there are effects!");
				max = StatusEffects.All.values().size() - 1;
			}

			int amount = RandomUtils.RandomRange(0, max);

			while (amount > 0) {

				BaseStatusEffect effect = null;

				while (effect == null || this.statusEffects.containsKey(effect.GUID())) {
					effect = (BaseStatusEffect) RandomUtils
							.WeightedRandom(ListUtils.CollectionToList(StatusEffects.All.values()));
				}
				amount--;
				this.statusEffects.put(effect.GUID(), new StatusEffectData(effect));

			}
		}
	}

	public UUID uid;

	public String GetName(EntityLivingBase entity) {
		return TextFormatting.YELLOW + "[Lv:" + this.level + "] " + GetRarity().Color() + GetRarity().Name() + " "
				+ entity.getName();

	}

	private MobRarity GetRarity() {
		return Rarities.Mobs.get(rarity);
	}

	private void AddMobcStats() {

		this.Stats.get(Health.GUID).Flat += 10 * level;
		this.Stats.get(Armor.GUID).Flat += 10 * level;
		this.Stats.get(CriticalHit.GUID).Flat += 5;
		this.Stats.get(CriticalDamage.GUID).Flat += 20;

		this.Stats.get(WaterResist.GUID).Flat += 10 * level;
		this.Stats.get(FireResist.GUID).Flat += 10 * level;
		this.Stats.get(ThunderResist.GUID).Flat += 10 * level;
		this.Stats.get(NatureResist.GUID).Flat += 10 * level;

		this.Stats.get(WaterDamage.GUID).Flat += 10 * level;
		this.Stats.get(FireDamage.GUID).Flat += 10 * level;
		this.Stats.get(ThunderDamage.GUID).Flat += 10 * level;
		this.Stats.get(NatureDamage.GUID).Flat += 10 * level;

		this.Stats.get(PhysicalDamage.GUID).Flat += 2 * level;

	}

	private void SetMobStrengthMultiplier() {
		float totalMulti = /* 1 + this.vanillaHP / 20 + */ GetRarity().StatMultiplier();

		for (Stat stat : Stats.values()) {
			stat.Flat *= totalMulti;
		}

	}

	public int experience = 0;

	public int GetExpRequiredForLevelUp() {

		int tens = level / 10;

		if (level < 5) {
			return 250 * level;
		}

		return level * 1000 + (tens * 5000);

	}

	public void GiveExp(EntityPlayer player, int i) {

		experience += i;

	}

	public boolean CheckIfCanLevelUp() {

		return experience >= GetExpRequiredForLevelUp();

	}

	public void LevelUp() {
		level++;
		experience = 0;
	}

	public void Heal(EntityLivingBase entity, int healthrestored) {
		entity.heal(HealthUtils.DamageToMinecraftHealth(healthrestored * OnHealDecrease.HEAL_DECREASE, entity));
	}

}
