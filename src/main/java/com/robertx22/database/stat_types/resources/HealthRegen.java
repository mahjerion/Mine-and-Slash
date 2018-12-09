package com.robertx22.database.stat_types.resources;

import com.robertx22.stats.Stat;
import com.robertx22.uncommon.enumclasses.Elements;

public class HealthRegen extends Stat {
    public static String GUID = "Health Regen";

    public HealthRegen() {
	this.StatMinimum = 1;
    }

    @Override
    public String LocString() {
	return "health_regen";
    }

    @Override
    public String Guid() {
	return GUID;
    }

    @Override
    public boolean ScalesToLevel() {
	return true;
    }

    @Override
    public Elements Element() {
	return null;
    }

    @Override
    public boolean IsPercent() {
	return false;
    }

}