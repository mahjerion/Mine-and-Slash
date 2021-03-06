package com.robertx22.mine_and_slash.database.currency.map;

import com.robertx22.mine_and_slash.advacements.PlayerLevelTrigger;
import com.robertx22.mine_and_slash.database.currency.base.CurrencyItem;
import com.robertx22.mine_and_slash.database.currency.base.ICurrencyItemEffect;
import com.robertx22.mine_and_slash.database.currency.base.IShapedRecipe;
import com.robertx22.mine_and_slash.database.currency.loc_reqs.BaseLocRequirement;
import com.robertx22.mine_and_slash.database.currency.loc_reqs.item_types.MapReq;
import com.robertx22.mine_and_slash.items.SimpleMatItem;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModItems;
import com.robertx22.mine_and_slash.saveclasses.item_classes.MapItemData;
import com.robertx22.mine_and_slash.uncommon.datasaving.ItemType;
import com.robertx22.mine_and_slash.uncommon.datasaving.Map;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;

public class EndlessSkiesItem extends CurrencyItem implements ICurrencyItemEffect, IShapedRecipe {

    @Override
    public String GUID() {
        return "currency/add_map_level";
    }

    private static final String name = Ref.MODID + ":currency/add_map_level";

    public EndlessSkiesItem() {
        super(name);
        this.itemTypesUsableOn = ItemType.MAP;
    }

    @Override
    public ItemStack ModifyItem(ItemStack stack, ItemStack Currency) {

        MapItemData map = Map.Load(stack);

        map.level += 5;

        Map.Save(stack, map);

        return stack;
    }

    @Override
    public ShapedRecipeBuilder getRecipe() {
        return shaped(ModItems.ENDLESS_SKIES.get())
            .key('#', SimpleMatItem.GOLDEN_ORB)
            .key('t', ModItems.CRYSTAL_OF_LEGEND.get())
            .key('v', Items.GOLD_INGOT)
            .key('b', Items.GLASS_BOTTLE)
            .patternLine("#v#")
            .patternLine("vtv")
            .patternLine(" b ")
            .addCriterion("player_level", new PlayerLevelTrigger.Instance(10));
    }

    @Override
    public List<BaseLocRequirement> requirements() {
        return Arrays.asList(MapReq.INSTANCE);
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public int getRarityRank() {
        return IRarity.Epic;
    }

    @Override
    public List<String> loreLines() {
        return Arrays.asList("Venture further.");
    }

    @Override
    public String locNameForLangFile() {
        return nameColor + "Endless Skies";
    }

    @Override
    public String locDescForLangFile() {
        return "Increases map level by 5";
    }

    @Override
    public boolean addsInstability() {
        return false;
    }

    @Override
    public int instabilityAddAmount() {
        return 0;
    }

}