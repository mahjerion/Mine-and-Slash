package com.robertx22.mine_and_slash.database.item_modifications.gear_items;

import com.robertx22.mine_and_slash.database.item_modifications.bases.BaseGearMod;
import com.robertx22.mine_and_slash.database.item_modifications.bases.ItemModType;
import com.robertx22.mine_and_slash.saveclasses.item_classes.GearItemData;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.ICommonDataItem;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import net.minecraft.util.text.ITextComponent;

public class PerfectPrimaryStats extends BaseGearMod {

    @Override
    public ItemModType getItemModType() {
        return ItemModType.PRIMARY_STATS;
    }

    @Override
    public int getRarityRank() {
        return IRarity.Legendary;
    }

    @Override
    public ITextComponent locName() {
        return Words.PefectPrimaryStats.locName();
    }

    @Override
    protected boolean canModifyPRIVATE(ICommonDataItem data) {
        GearItemData gear = (GearItemData) data;
        return gear.primaryStats != null;
    }

    @Override
    public String GUID() {
        return "perfect_primary_stats";
    }

    @Override
    public void modifyGear(GearItemData gear) {
        gear.primaryStats.increaseAllModsBy(gear, gear.getRarity().StatPercents().max);
    }
}