package com.robertx22.uncommon.utilityclasses;

import com.robertx22.mmorpg.Main;
import com.robertx22.network.ParticlePackage;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class ParticleUtils {

	public static void spawnParticle(Entity source, String name, double x, double y, double z, double xVel, double yVel,
			double zVel) {

		ParticlePackage packet = new ParticlePackage(false, name, x, y, z, xVel, yVel, zVel);

		TargetPoint point = new TargetPoint(source.dimension, x, y, z, 100);

		Main.Network.sendToAllAround(packet, point);

	}

	public static void spawnParticleGenerator(Entity source, String name, double x, double y, double z, double xVel,
			double yVel, double zVel) {

		ParticlePackage packet = new ParticlePackage(true, name, x, y, z, xVel, yVel, zVel);

		TargetPoint point = new TargetPoint(source.dimension, x, y, z, 100);

		Main.Network.sendToAllAround(packet, point);

	}

}
