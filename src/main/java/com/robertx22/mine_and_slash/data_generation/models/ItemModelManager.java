package com.robertx22.mine_and_slash.data_generation.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robertx22.mine_and_slash.database.gearitemslots.bases.GearItemSlot;
import com.robertx22.mine_and_slash.database.gearitemslots.offhand.Shield;
import com.robertx22.mine_and_slash.database.gearitemslots.weapons.Bow;
import com.robertx22.mine_and_slash.database.gearitemslots.weapons.CrossBow;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.registry.SlashRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;

public class ItemModelManager extends ItemModelProvider {

    public ItemModelManager(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Ref.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        ForgeRegistries.ITEMS.forEach(x -> {
            if (x instanceof IAutoModel) {
                IAutoModel auto = (IAutoModel) x;
                auto.generateModel(this);
            }
        });

        SlashRegistry.UniqueGears()
            .getSerializable()
            .forEach(x -> {
                if (x.getGearSlot() != Bow.INSTANCE && x.getGearSlot() != CrossBow.INSTANCE) {
                    if (x.getGearSlot()
                        .slotType()
                        .equals(GearItemSlot.GearSlotType.Weapon)) {
                        handheld(x.getUniqueItem());
                    } else {
                        generated(x.getItemForRegistration());
                    }
                }
            });
        SlashRegistry.GearTypes()
            .getList()
            .forEach(x -> x.getItemsForRaritiesMap()
                .values()
                .forEach(i -> {
                    if (x != Bow.INSTANCE && x != CrossBow.INSTANCE && x != Shield.INSTANCE) {
                        if (x.GUID()
                            .contains("cloth") || x.GUID()
                            .contains("leather")) {
                            //generated(i, itemTexture(i), overlay(i));
                        } else if (x.slotType()
                            .equals(GearItemSlot.GearSlotType.Weapon)) {
                            handheld(i);
                        } else {
                            generated(i);
                        }
                    }
                }));

    }

    @Override
    public String getName() {
        return "Mine and Slash Item Models";
    }

    public String modid(Item item) {
        return item
            .asItem()
            .getRegistryName()
            .getNamespace();
    }

    public String name(Item item) {
        return item
            .asItem()
            .getRegistryName()
            .getPath();
    }

    public ItemModelBuilder generated(Item item) {
        return generated(item, itemTexture(item));
    }

    public ItemModelBuilder generated(Item item, ResourceLocation... layers) {
        ItemModelBuilder ret = withExistingParent(name(item), "item/generated");
        for (int i = 0; i < layers.length; i++) {
            ret = ret.texture("layer" + i, layers[i]);
        }
        return ret;
    }

    public ResourceLocation itemTexture(Item item) {
        return modLoc("items/" + name(item));
    }

    public ResourceLocation overlay(Item item) {
        return modLoc("items/" + name(item) + "_overlay");
    }

    public ItemModelBuilder handheld(Item item) {
        return handheld(item, itemTexture(item));
    }

    public ItemModelBuilder handheld(Item item, ResourceLocation texture) {
        return withExistingParent(name(item), "item/handheld").texture("layer0", texture);
    }

    // TEMP WORKAROUND UNTIL FORGE FIXES SHIT
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting()
        .create();

    @Override
    protected void generateAll(DirectoryCache cache) {
        for (ItemModelBuilder model : generatedModels.values()) {
            Path target = getPath(model);
            try {
                IDataProvider.save(GSON, cache, model.toJson(), target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path getPath(ItemModelBuilder model) {
        ResourceLocation loc = model.getLocation();
        return generator.getOutputFolder()
            .resolve("assets/" + loc.getNamespace() + "/models/item/" + loc.getPath() + ".json");
    }
    // TEMP WORKAROUND UNTIL FORGE FIXES SHIT
}
