package com.robertx22.spells.aoe_projectile.AcidExplosion;

import com.robertx22.spells.bases.projectile.EntityElementalBoltAOE;
import com.robertx22.uncommon.enumclasses.Elements;

import net.minecraft.world.World;

public class EntityAcidExplosion extends EntityElementalBoltAOE {

	public EntityAcidExplosion(World worldIn) {

		super(worldIn);

	}

	@Override
	public Elements element() {
		return Elements.Nature;
	}

}