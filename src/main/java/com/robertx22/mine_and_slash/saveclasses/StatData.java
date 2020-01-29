package com.robertx22.mine_and_slash.saveclasses;

import com.robertx22.mine_and_slash.database.stats.Stat;
import com.robertx22.mine_and_slash.db_lists.registry.SlashRegistry;
import com.robertx22.mine_and_slash.uncommon.enumclasses.StatTypes;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;

import java.text.DecimalFormat;

@Storable
public class StatData {

    private static StatData empty = new StatData();

    public static StatData empty() {
        return empty;
    }

    public StatData() {

    }

    public StatData(Stat stat) {
        this.id = stat.GUID();
    }

    public Stat GetStat() {
        return SlashRegistry.Stats().get(id);
    }

    @Store// guid
    private String id = "";

    public float Flat = 0;

    public float Percent = 0;

    public float Multi = 0;

    @Store
    public float val = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addExact(StatTypes type, float value) {
        if (type == StatTypes.Flat) {
            this.Flat += value;
        } else if (type == StatTypes.Percent) {
            this.Percent += value;
        } else {
            this.Multi += value;
        }
    }

    public void addExact(StatData data) {
        if (data.id.equals(this.id)) {
            this.Flat += data.Flat;
            this.Percent += data.Percent;
            this.Multi += data.Multi;
        }

    }

    public void addFlat(float val, int lvl) {
        if (this.GetStat().ScalesToLevel()) {
            this.Flat += this.GetStat().calculateScalingStatGrowth(val, lvl);
        } else {
            this.Flat += val;
        }
    }

    public void Clear() {
        Flat = 0;
        Percent = 0;
        Multi = 0;

    }

    public String formattedValue() {

        float val = this.val;

        DecimalFormat format = new DecimalFormat();

        if (Math.abs(val) < 10) {
            format.setMaximumFractionDigits(1);

            return format.format(val);

        } else {
            int intval = (int) val;
            return intval + "";
        }

    }

    public float getMultiplier() {
        return 1 + val / 100;
    }

    public boolean isNotEmpty() {
        return Flat != 0 || val != 0 || Percent != 0 || Multi != 0;
    }
}
