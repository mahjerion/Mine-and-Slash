package com.robertx22.database.stats.mods.flat.elemental.resist;

import com.robertx22.stats.StatMod;
import com.robertx22.uncommon.enumclasses.StatTypes;

public abstract class BaseEleResistFlat extends StatMod {

	public BaseEleResistFlat() {
	}

	@Override
	public int Min() {
		return 2;
	}

	@Override
	public int Max() {
		return 10;
	}

	@Override
	public StatTypes Type() {
		return StatTypes.Flat;
	}

}
