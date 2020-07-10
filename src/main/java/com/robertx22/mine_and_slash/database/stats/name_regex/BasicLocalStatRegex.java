package com.robertx22.mine_and_slash.database.stats.name_regex;

import com.robertx22.mine_and_slash.database.stats.Stat;
import com.robertx22.mine_and_slash.uncommon.enumclasses.StatModTypes;

public class BasicLocalStatRegex extends StatNameRegex {

    @Override
    public String getStatNameRegex(StatModTypes type, Stat stat) {

        if (stat.UsesSecondValue()) {
            return NAME + ": " + MIN_VALUE + "-" + MAX_VALUE;
        } else {
            return NAME + ": " + VALUE;
        }

    }
}