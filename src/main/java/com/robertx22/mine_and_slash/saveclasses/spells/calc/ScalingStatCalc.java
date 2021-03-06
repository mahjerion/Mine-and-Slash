package com.robertx22.mine_and_slash.saveclasses.spells.calc;

import com.robertx22.mine_and_slash.database.stats.Stat;
import com.robertx22.mine_and_slash.registry.SlashRegistry;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;
import info.loenwind.autosave.annotations.Factory;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

@Storable
public class ScalingStatCalc extends BaseStatCalc {

    @Store
    public String statID;

    @Store
    public float multi;

    public Stat getStat() {
        return SlashRegistry.Stats()
            .get(statID);
    }

    @Factory
    private ScalingStatCalc() {

    }

    public ScalingStatCalc(Stat stat, float multi) {
        super();
        this.statID = stat.GUID();
        this.multi = multi;
    }

    @Override
    public float getMulti() {
        return multi;
    }

    public int getMultiAsPercent() {
        return (int) (multi * 100);
    }

    @Override
    public int getCalculatedValue(EntityCap.UnitData data) {
        return (int) (data.getUnit()
            .peekAtStat(statID)
            .getAverageValue() * multi);
    }

    @Override
    public List<ITextComponent> GetTooltipString(TooltipInfo info) {
        return getTooltipFor(multi, getCalculatedValue(info.unitdata), getStat().locName(), getStat().getElement());
    }
}
