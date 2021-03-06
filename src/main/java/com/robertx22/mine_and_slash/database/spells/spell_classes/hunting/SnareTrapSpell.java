package com.robertx22.mine_and_slash.database.spells.spell_classes.hunting;

import com.robertx22.mine_and_slash.database.spells.entities.proj.FireBombEntity;
import com.robertx22.mine_and_slash.database.spells.entities.proj.SnareTrapEntity;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.BaseSpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.SpellCastContext;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.cast_types.SpellCastType;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.configs.ImmutableSpellConfigs;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.configs.PreCalcSpellConfigs;
import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.configs.SC;
import com.robertx22.mine_and_slash.potion_effects.ranger.SnareEffect;
import com.robertx22.mine_and_slash.potion_effects.ranger.WeakenEffect;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.saveclasses.spells.AbilityPlace;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Masteries;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SnareTrapSpell extends BaseSpell {

    private SnareTrapSpell() {
        super(
            new ImmutableSpellConfigs() {

                @Override
                public Masteries school() {
                    return Masteries.HUNTING;
                }

                @Override
                public SpellCastType castType() {
                    return SpellCastType.PROJECTILE;
                }

                @Override
                public SoundEvent sound() {
                    return SoundEvents.BLOCK_TRIPWIRE_CLICK_ON;
                }

                @Override
                public Elements element() {
                    return Elements.Physical;
                }
            }.cooldownIfCanceled(true)
                .summonsEntity(w -> new SnareTrapEntity(w))
                .setSwingArmOnCast());
    }

    public static SnareTrapSpell getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public PreCalcSpellConfigs getPreCalcConfig() {
        PreCalcSpellConfigs c = new PreCalcSpellConfigs();

        c.set(SC.MANA_COST, 14, 26);
        c.set(SC.BASE_VALUE, 0, 0);
        c.set(SC.SHOOT_SPEED, 1.4F, 2F);
        c.set(SC.PROJECTILE_COUNT, 1, 1);
        c.set(SC.CAST_TIME_TICKS, 0, 0);
        c.set(SC.COOLDOWN_SECONDS, 24, 18);
        c.set(SC.DURATION_TICKS, 200, 300);
        c.set(SC.RADIUS, 1.5F, 3.0F);
        c.set(SC.TICK_RATE, 20, 20);

        c.setMaxLevel(8);

        return c;
    }

    @Override
    public AbilityPlace getAbilityPlace() {
        return new AbilityPlace(7, 6);
    }

    @Override
    public String GUID() {
        return "snare_trap";
    }

    @Override
    public List<ITextComponent> GetDescription(TooltipInfo info, SpellCastContext ctx) {

        List<ITextComponent> list = new ArrayList<>();

        list.add(new StringTextComponent("Throw out a trap that stops enemy movement in AOE."));

        list.addAll(SnareEffect.INSTANCE.GetTooltipStringWithNoExtraSpellInfo(info));

        return list;

    }

    @Override
    public Words getName() {
        return Words.SnareTrap;
    }

    private static class SingletonHolder {
        private static final SnareTrapSpell INSTANCE = new SnareTrapSpell();
    }
}
