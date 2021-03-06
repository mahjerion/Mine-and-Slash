package com.robertx22.mine_and_slash.database.loot_crates.bases;

import com.robertx22.mine_and_slash.database.loot_crates.loot_crate_item.MapLootCrateItem;
import com.robertx22.mine_and_slash.db_lists.Rarities;
import com.robertx22.mine_and_slash.loot.LootInfo;
import com.robertx22.mine_and_slash.registry.ISlashRegistryEntry;
import com.robertx22.mine_and_slash.registry.SlashRegistryType;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.RandomUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class LootCrate implements ISlashRegistryEntry {

    public Item lootCrateItem() {
        return MapLootCrateItem.ITEM;
    }

    public abstract ITextComponent name();

    public abstract ItemStack generateStack(LootInfo info);

    public float lotteryWinChance() {
        return 1;
    }

    public boolean wonLottery() {
        return RandomUtils.roll(lotteryWinChance());
    }

    public abstract int maxItems();

    public ItemStack getCrateStack(int lvl, int mapTier, int score) {

        ItemStack stack = new ItemStack(lootCrateItem());

        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        stack.getTag()
            .putInt(MapLootCrateItem.LVL, lvl);
        stack.getTag()
            .putInt(MapLootCrateItem.TIER, mapTier);
        stack.getTag()
            .putString(MapLootCrateItem.ID, GUID());
        stack.getTag()
            .putString(MapLootCrateItem.SCORE, MapScoreEnum.byNumber(score)
                .name());

        return stack;
    }

    public List<ItemStack> generateItems(LootInfo info) {

        List<ItemStack> items = new ArrayList<>();

        int amount = RandomUtils.RandomRange(maxItems() / 2, maxItems());

        if (RandomUtils.roll(1)) {
            amount *= 3;
        }

        while (items.size() < amount) {
            ItemStack stack = generateStack(info);
            if (stack != null && !stack.isEmpty()) {
                items.add(stack);
            }
        }

        return items;

    }

    @Override
    public int Weight() {
        return getRarity().Weight();
    }

    @Override
    public int getRarityRank() {
        return IRarity.Uncommon;
    }

    @Override
    public Rarity getRarity() {
        return Rarities.Gears.get(getRarityRank());
    }

    @Override
    public SlashRegistryType getSlashRegistryType() {
        return SlashRegistryType.LOOT_CRATE;
    }

}
