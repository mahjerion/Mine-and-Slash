package com.robertx22.database.map_mods.bonus.ele_dmg;

import com.robertx22.database.map_mods.bases.BonusEleDmgBase;
import com.robertx22.database.stats.types.offense.bonus.BonusWaterDamage;
import com.robertx22.stats.Stat;

public class BonusWaterDamageMap extends BonusEleDmgBase {

	public BonusWaterDamageMap() {
	}

	@Override
	public String GUID() {
		return "BonusWaterDamageMap";
	}

	@Override
	public Stat GetBaseStat() {
		return new BonusWaterDamage();
	}

}