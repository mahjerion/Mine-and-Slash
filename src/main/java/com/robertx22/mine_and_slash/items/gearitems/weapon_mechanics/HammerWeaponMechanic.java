package com.robertx22.mine_and_slash.items.gearitems.weapon_mechanics;

import com.robertx22.mine_and_slash.database.stats.types.offense.PhysicalDamage;
import com.robertx22.mine_and_slash.database.stats.types.resources.Energy;
import com.robertx22.mine_and_slash.items.gearitems.bases.WeaponMechanic;
import com.robertx22.mine_and_slash.uncommon.capability.EntityCap.UnitData;
import com.robertx22.mine_and_slash.uncommon.effectdatas.DamageEffect;
import com.robertx22.mine_and_slash.uncommon.effectdatas.EffectData;
import com.robertx22.mine_and_slash.uncommon.effectdatas.interfaces.WeaponTypes;
import com.robertx22.mine_and_slash.uncommon.localization.Styles;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.EntityFinder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class HammerWeaponMechanic extends WeaponMechanic {

    @Override
    public ITextComponent tooltipDesc() {
        return new StringTextComponent(Styles.GREEN + "Aoe Attack");
    }

    @Override
    public float GetEnergyCost(int lvl) {
        return Energy.INSTANCE.calculateScalingStatGrowth(10, lvl);
    }

    @Override
    public WeaponTypes weaponType() {
        return WeaponTypes.Hammer;
    }

    float radius = 1.2F;

    @Override
    public boolean Attack(LivingHurtEvent event, LivingEntity source, LivingEntity target, UnitData unitsource,
                          UnitData targetUnit) {

        int num = (int) unitsource.getUnit().getCreateStat(PhysicalDamage.GUID).val;

        for (LivingEntity en : EntityFinder.start(source, LivingEntity.class, target.getPositionVector())
                .radius(radius)
                .build()) {

            if (en.equals(target)) {
                DamageEffect dmg = new DamageEffect(event, source, en, num, unitsource, targetUnit,
                                                    EffectData.EffectTypes.BASIC_ATTACK, WeaponTypes.Hammer
                );
                dmg.Activate();
            } else {
                DamageEffect dmg = new DamageEffect(null, source, en, num, unitsource, targetUnit,
                                                    EffectData.EffectTypes.BASIC_ATTACK, WeaponTypes.Hammer
                );
                dmg.Activate();
            }

        }

        return true;
    }

}
