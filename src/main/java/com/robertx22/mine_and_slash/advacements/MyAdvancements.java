package com.robertx22.mine_and_slash.advacements;

import com.robertx22.mine_and_slash.database.currency.ChaosOrbItem;
import com.robertx22.mine_and_slash.database.currency.OrbOfTransmutationItem;
import com.robertx22.mine_and_slash.database.currency.StoneOfHopeItem;
import com.robertx22.mine_and_slash.database.rarities.MobRarity;
import com.robertx22.mine_and_slash.db_lists.Rarities;
import com.robertx22.mine_and_slash.items.bags.AutoSalvageBag;
import com.robertx22.mine_and_slash.items.bags.currency_bag.ItemCurrencyBag;
import com.robertx22.mine_and_slash.items.bags.loot_bag.ItemLootBag;
import com.robertx22.mine_and_slash.items.bags.map_bag.ItemMapBag;
import com.robertx22.mine_and_slash.items.bags.master_bag.ItemMasterBag;
import com.robertx22.mine_and_slash.items.gearitems.weapons.ItemHammer;
import com.robertx22.mine_and_slash.items.gearitems.weapons.ItemSword;
import com.robertx22.mine_and_slash.items.misc.ItemMap;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModBlocks;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModItems;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import com.robertx22.mine_and_slash.uncommon.localization.AdvDescs;
import com.robertx22.mine_and_slash.uncommon.localization.AdvTitles;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class MyAdvancements implements Consumer<Consumer<Advancement>> {

    public void accept(Consumer<Advancement> consu) {
        Advancement parent = Advancement.Builder.builder()
            .withDisplay(ItemSword.Items.get(4), AdvTitles.WelcomeMineandslash.locName(),
                AdvDescs.WelcomeMineandslash.locName(),
                new ResourceLocation("textures/gui/advancements/backgrounds/end.png"), FrameType.TASK,
                false, false, false
            )
            .withCriterion("crafting_table", InventoryChangeTrigger.Instance.forItems(Blocks.CRAFTING_TABLE))
            .register(consu, id("root"));

        Advancement lvlpenalty = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(Items.GUNPOWDER, AdvTitles.LevelPenalty.locName(), AdvDescs.LevelPenalty.locName(), null,
                FrameType.TASK, true, true, false
            )
            .withCriterion(id("lvl_penalty"), new DropLvlPenaltyTrigger.Instance(5))
            .register(consu, id("lvl_penalty"));

        Advancement lvl_10 = levelAdv(10, AdvDescs.LevelUp10, parent, consu, ItemHammer.Items.get(0));
        Advancement lvl_25 = levelAdv(20, AdvDescs.LevelUp, lvl_10, consu, ItemHammer.Items.get(1));
        Advancement lvl_50 = levelAdv(50, AdvDescs.LevelUp, lvl_25, consu, ItemHammer.Items.get(2));
        Advancement lvl_60 = levelAdv(60, AdvDescs.LevelUp, lvl_50, consu, ItemHammer.Items.get(3));

        Advancement legendaryMob = mobRarityKill(Rarities.Mobs.get(IRarity.Legendary),
            AdvDescs.MobRaritySpawnAt.locName()
                .appendText("10"), parent, consu,
            Items.ZOMBIE_HEAD
        );

        Advancement bossMob = mobRarityKill(
            Rarities.Mobs.get(IRarity.Boss), AdvDescs.BossSpawnAt.locName(), legendaryMob, consu, Items.DRAGON_HEAD);

        Advancement first_adv_map = Advancement.Builder.builder()
            .withParent(lvl_10)
            .withDisplay(ItemMap.Items.get(0), AdvTitles.FirstAdventureMap.locName(),
                AdvDescs.AdventureMap.locName(), null, FrameType.GOAL, true, true, false
            )
            .withCriterion("adv_map", InventoryChangeTrigger.Instance.forItems(ItemMap.Items.get(0)))
            .register(consu, id("adv_map"));

        Advancement repair = itemAdv(AdvTitles.RepairStation, AdvDescs.RepairStation, "repair", parent, consu,
            ModBlocks.GEAR_REPAIR.get()
        );
        Advancement modify = itemAdv(AdvTitles.ModifyStation, AdvDescs.ModifyStation, "modify", lvl_10, consu,
            ModBlocks.GEAR_MODIFY.get()
        );
        Advancement salvage = itemAdv(AdvTitles.SalvageStation, AdvDescs.SalvageStation, "salvage", parent, consu,
            ModBlocks.GEAR_SALVAGE.get()
        );

        Advancement currency_bag = itemAdv(
            AdvTitles.CurrencyBag, AdvDescs.CurrencyBag, "currency_bag", repair, consu, ItemCurrencyBag.ITEM);
        Advancement map_bag = itemAdv(AdvTitles.MapBag, AdvDescs.MapBag, "map_bag", repair, consu, ItemMapBag.ITEM);
        Advancement loot_bag = itemAdv(
            AdvTitles.LootBag, AdvDescs.LootBag, "loot_bag", repair, consu, ItemLootBag.ITEM);
        Advancement master_bag = itemAdv(
            AdvTitles.MasterBag, AdvDescs.MasterBag, "master_bag", repair, consu, ItemMasterBag.ITEM);

        Advancement map_device = itemAdv(AdvTitles.MapDevice, AdvDescs.MapDevice, "map_device", first_adv_map, consu,
            ModBlocks.MAP_DEVICE.get()
        );

        Advancement orbOfTrans = byItemName(ModItems.ORB_OF_TRANSMUTATION.get(),
            new StringTextComponent(new OrbOfTransmutationItem().locNameForLangFile()),
            "orb_of_transmutation", AdvDescs.OrbOfTransmutation, lvl_10, consu,
            ModItems.ORB_OF_TRANSMUTATION.get()
        );

        Advancement chaosOrb = byItemName(ModItems.CHAOS_ORB.get(),
            new StringTextComponent(new ChaosOrbItem().locNameForLangFile()), "chaos_orb",
            AdvDescs.ChaosOrb, orbOfTrans, consu, ModItems.CHAOS_ORB.get()
        );
        Advancement addaffix = byItemName(ModItems.UNEARTH_PREFIX.get(), AdvTitles.AddAffix.locName(), "add_affix",
            AdvDescs.AddAffix, orbOfTrans, consu, ModItems.UNEARTH_PREFIX.get()
        );
        Advancement stoneofhope = byItemName(ModItems.STONE_OF_HOPE.get(),
            new StringTextComponent(new StoneOfHopeItem().locNameForLangFile()),
            "stone_of_hope", AdvDescs.Stoneofhope, orbOfTrans, consu,
            ModItems.STONE_OF_HOPE.get()
        );

        Advancement sal0 = salvageBag(AutoSalvageBag.Items.get(0), salvage, consu);

    }

    private Advancement salvageBag(Item bag, Advancement parent, Consumer<Advancement> consumerAdv) {

        AutoSalvageBag bagitem = (AutoSalvageBag) bag;

        String id = "auto_salvage_bag" + bagitem.rarity;

        Advancement adv = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(bag, new StringTextComponent(bagitem.locNameForLangFile()),
                AdvDescs.AutoSalvageBag.locName(), null, FrameType.GOAL, true, true, false
            )
            .withCriterion(id, InventoryChangeTrigger.Instance.forItems(bag))
            .register(consumerAdv, id(id));

        return adv;
    }

    private Advancement levelAdv(int lvl, AdvDescs desc, Advancement parent, Consumer<Advancement> consumerAdv,
                                 IItemProvider item) {
        String id = "player_level_" + lvl;

        Advancement adv = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(item, Words.Level.locName()
                    .appendText(": " + lvl), desc.locName(), null,
                FrameType.CHALLENGE, true, true, false
            )
            .withCriterion(id, new PlayerLevelTrigger.Instance(lvl))
            .register(consumerAdv, id(id));

        return adv;
    }

    private Advancement mobRarityKill(MobRarity rar, ITextComponent desc, Advancement parent,
                                      Consumer<Advancement> consumerAdv, IItemProvider item) {
        String id = "kill_mob_rarity_" + rar.Rank();

        Advancement adv = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(item, Words.Kill.locName()
                    .appendText(": ")
                    .appendSibling(rar.locName()), desc, null,
                FrameType.CHALLENGE, true, true, false
            )
            .withCriterion(id, new KillRarityMobTrigger.Instance(rar.Rank()))
            .register(consumerAdv, id(id));

        return adv;
    }

    private Advancement byItemName(Item display, ITextComponent title, String id, AdvDescs desc, Advancement parent,
                                   Consumer<Advancement> consumerAdv, Item... item) {

        Advancement adv = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(display, title, desc.locName(), null, FrameType.GOAL, true, true, false)
            .withCriterion(id, InventoryChangeTrigger.Instance.forItems(item))
            .register(consumerAdv, id(id));

        return adv;
    }

    private Advancement itemAdv(AdvTitles title, AdvDescs desc, String id, Advancement parent,
                                Consumer<Advancement> consumerAdv, IItemProvider item) {
        Advancement adv = Advancement.Builder.builder()
            .withParent(parent)
            .withDisplay(item, title.locName(), desc.locName(), null, FrameType.TASK, true, true, false)
            .withCriterion("gained_" + id, InventoryChangeTrigger.Instance.forItems(item))
            .register(consumerAdv, id(id));

        return adv;
    }

    private String id(String str) {
        return Ref.MODID + ":mine_and_slash/" + str;
    }

}

