package com.robertx22.customitems.bases;

import java.util.HashMap;

import com.robertx22.customitems.oldreplacesoon.NewItemCreator;
import com.robertx22.utilityclasses.OnItemCreatedUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public abstract class BaseSwordItem extends ItemSword {

	static ItemSword.ToolMaterial Mat = EnumHelper.addToolMaterial("swordmat", 0, 900, 1F, 1F, 1);

	public abstract String Name();

	public BaseSwordItem(int rarity, HashMap<Integer, Item> map) {
		super(Mat);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setCreativeTab(NewItemCreator.MyModTab);
		this.setUnlocalizedName(Name().toLowerCase() + rarity);
		this.setRegistryName(Name().toLowerCase() + rarity);

		map.put(rarity, this);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {

		OnItemCreatedUtils.TryReroll(stack, worldIn);

	}

}