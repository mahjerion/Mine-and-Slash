package com.robertx22.mine_and_slash.uncommon.effectdatas;

import com.robertx22.mine_and_slash.config.ModConfig;
import com.robertx22.mine_and_slash.database.spells.bases.MyDamageSource;
import com.robertx22.mine_and_slash.database.stats.stat_effects.defense.BlockEffect;
import com.robertx22.mine_and_slash.mmorpg.MMORPG;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.network.DmgNumPacket;
import com.robertx22.mine_and_slash.uncommon.capability.EntityCap.UnitData;
import com.robertx22.mine_and_slash.uncommon.effectdatas.interfaces.*;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.HealthUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.NumberUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.Map.Entry;

public class DamageEffect extends EffectData implements IArmorReducable, IPenetrable, IDamageEffect, IElementalResistable, IElementalPenetrable, ICrittable {

    public DamageEffect(LivingHurtEvent event, LivingEntity source, LivingEntity target,
                        int dmg, UnitData sourceData, UnitData targetData,
                        EffectTypes effectType, WeaponTypes weptype) {
        super(source, target, sourceData, targetData);

        this.setEffectType(effectType, weptype);
        this.number = dmg;
        this.event = event;
    }

    LivingHurtEvent event;

    private HashMap<Elements, Integer> bonusElementDamageMap = new HashMap();

    public Elements getHighestBonusElementalDamageElement() {

        int highest = 0;
        Elements ele = null;
        for (Entry<Elements, Integer> entry : bonusElementDamageMap.entrySet()) {
            if (entry.getValue() > highest) {
                ele = entry.getKey();
                highest = entry.getValue();
            }
        }
        return ele;

    }

    public void addBonusEleDmgDivideByMulti(Elements element, float dmg) {
        addBonusEleDmg(element, dmg / damageMultiplier);
    }

    public void addBonusEleDmg(Elements element, float dmg) {
        if (bonusElementDamageMap.containsKey(element)) {
            bonusElementDamageMap.put(element, (int) (bonusElementDamageMap.get(element) + dmg));

        } else {
            bonusElementDamageMap.put(element, (int) dmg);
        }
    }

    public static String dmgSourceName = Ref.MODID + ".custom_damage";
    public Elements element = Elements.Physical;
    public int armorPene;
    public int elementalPene;

    public float damageMultiplier = 1;

    public float healthHealed;
    public float manaRestored;
    public boolean isFullyBlocked = false;
    public boolean isPartiallyBlocked = false;
    public boolean isDodged = false;

    public boolean isBlocked() {

        if (isFullyBlocked || isPartiallyBlocked) {
            return true;
        }
        return false;
    }

    public float getActualDamage() {

        this.number *= damageMultiplier; // this way axes can do double damage instead of doing double attacks

        float dmg = HealthUtils.DamageToMinecraftHealth(number + 1, target);

        return dmg;

    }

    @Override
    protected void activate() {

        if (target.getHealth() <= 0F || !target.isAlive()) {
            return;
        }

        if (this.canceled) {
            return;
        }

        MyDamageSource dmgsource = new MyDamageSource(dmgSourceName, this.source, element, (int) number);

        if (this.isPartiallyBlocked) {
            dmgsource.setDamageBypassesArmor();
        }

        if (this.isFullyBlocked == false) {

            this.sourceData.onAttackEntity(source, target);

            int hurtResistantTime = 0;

            if (ModConfig.INSTANCE.Server.USE_ATTACK_COOLDOWN.get()) {
                hurtResistantTime = target.hurtResistantTime;
            } else {
                hurtResistantTime = 3;
            }

            target.hurtResistantTime = 0;   // set to 0 so my attack can work (cus it comes after a vanilla atk) and then set it back to what it was before

            DmgByElement info = getDmgByElement();

            if (event != null) {
                event.setAmount(info.totalDmg);
                event.getSource().setDamageBypassesArmor();
            } else {
                target.attackEntityFrom(dmgsource, info.totalDmg);
            }

            target.hurtResistantTime = hurtResistantTime;

            Heal();
            RestoreMana();

            if ((int) number > 0 && source instanceof ServerPlayerEntity) {

                ServerPlayerEntity player = (ServerPlayerEntity) source;
                DmgNumPacket packet = new DmgNumPacket(target, this.element, NumberUtils.formatDamageNumber(this));
                MMORPG.sendToClient(packet, player);

            }
        }

        BlockEffect.removeKnockbackResist(target);

    }

    private void RestoreMana() {
        int restored = (int) manaRestored;
        if (restored > 0) {
            this.sourceData.restoreMana(restored);
        }
    }

    private void Heal() {
        int healed = (int) healthHealed;
        if (healed > 0) {
            sourceData.heal(new HealData(source, sourceData, healed));
        }
    }

    public DamageEffect setMultiplier(float multi) {
        this.damageMultiplier = multi;
        return this;
    }

    static class DmgByElement {

        public HashMap<Elements, Integer> dmgmap = new HashMap<>();
        public Elements highestDmgElement;
        public float highestDmgValue;
        public float totalDmg = 0;

        public void addDmg(float dmg, Elements element) {

            int total = (int) (dmgmap.getOrDefault(element, 0) + dmg);

            dmgmap.put(element, total);

            totalDmg += dmg;

            if (total > highestDmgValue) {
                highestDmgElement = element;
                highestDmgValue = total;
            }

        }

    }

    private DmgByElement getDmgByElement() {
        DmgByElement info = new DmgByElement();

        for (Entry<Elements, Integer> entry : bonusElementDamageMap.entrySet()) {
            if (entry.getValue() > 0) {
                DamageEffect bonus = new DamageEffect(null, source, target, entry.getValue(), this.sourceData, this.targetData, EffectTypes.BONUS_ATTACK, this.weaponType);
                bonus.element = entry.getKey();
                bonus.damageMultiplier = this.damageMultiplier;

                info.addDmg(bonus.getActualDamage(), bonus.element);

            }
        }
        info.addDmg(this.getActualDamage(), this.element);

        return info;

    }

    @Override
    public LivingEntity Source() {
        return source;
    }

    @Override
    public LivingEntity Target() {
        return target;
    }

    @Override
    public float Number() {
        return number;
    }

    @Override
    public Elements GetElement() {
        return element;
    }

    @Override
    public void SetArmorPenetration(int val) {
        this.armorPene = val;

    }

    @Override
    public void addElementalPenetration(int val) {
        this.elementalPene += val;
    }

    @Override
    public int GetArmorPenetration() {
        return this.armorPene;
    }

    public boolean crit = false;

    @Override
    public void SetCrit(boolean bool) {
        crit = bool;
    }

    @Override
    public boolean GetCrit() {
        return crit;
    }

    @Override
    public int GetElementalPenetration() {
        return this.elementalPene;
    }

}
