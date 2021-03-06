package com.robertx22.mine_and_slash.db_lists.initializers;

import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.BaseSpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.divine.*;
import com.robertx22.mine_and_slash.database.spells.spell_classes.divine.buffs.BraverySpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.divine.buffs.TrickerySpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.divine.buffs.WizardrySpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.fire.*;
import com.robertx22.mine_and_slash.database.spells.spell_classes.fire.buffs.DraconicBloodSpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.fire.buffs.VampiricBloodSpell;
import com.robertx22.mine_and_slash.database.spells.spell_classes.hunting.*;
import com.robertx22.mine_and_slash.database.spells.spell_classes.hunting.buffs.SilentWindBuff;
import com.robertx22.mine_and_slash.database.spells.spell_classes.hunting.buffs.WindWalkBuff;
import com.robertx22.mine_and_slash.database.spells.spell_classes.nature.*;
import com.robertx22.mine_and_slash.database.spells.spell_classes.ocean.*;
import com.robertx22.mine_and_slash.database.spells.spell_classes.ocean.buffs.FrostShieldBuff;
import com.robertx22.mine_and_slash.database.spells.spell_classes.ocean.buffs.IceBladeBuff;
import com.robertx22.mine_and_slash.database.spells.spell_classes.ocean.buffs.NourishmentBuff;
import com.robertx22.mine_and_slash.database.spells.spell_classes.storm.*;
import com.robertx22.mine_and_slash.registry.ISlashRegistryInit;

import java.util.ArrayList;
import java.util.List;

public class Spells implements ISlashRegistryInit {

    @Override
    public void registerAll() {

        List<BaseSpell> All = new ArrayList<BaseSpell>() {
            {
                {

                    add(BraverySpell.getInstance());
                    add(TrickerySpell.getInstance());
                    add(WizardrySpell.getInstance());

                    add(HolyFlowerSpell.getInstance());
                    add(SpearOfJudgementSpell.getInstance());
                    add(PurifyingFiresSpell.getInstance());
                    add(HealingAuraSpell.getInstance());

                    add(FrostballSpell.getInstance());
                    add(WhirlpoolSpell.getInstance());
                    add(HeartOfIceSpell.getInstance());
                    add(TidalWaveSpell.getInstance());
                    add(BlizzardSpell.getInstance());
                    add(FrozenOrbSpell.getInstance());
                    add(FrostShieldBuff.getInstance());
                    add(NourishmentBuff.getInstance());
                    add(IceBladeBuff.getInstance());
                    add(FreezeSpell.getInstance());

                    add(NatureBalmSpell.getInstance());
                    add(GorgonsGazeSpell.getInstance());
                    add(ThornArmorSpell.getInstance());
                    add(ThornBushSpell.getInstance());
                    add(PoisonedWeaponsSpell.getInstance());
                    add(PoisonBallSpell.getInstance());
                    add(PoisonCloudSpell.getInstance());
                    add(RootSpell.getInstance());
                    add(CorrosiveShadowSpell.getInstance());
                    add(MortalitySapSpell.getInstance());
                    add(ThoughtSeizeSpell.getInstance());

                    add(ThunderstormSpell.getInstance());
                    add(ThunderspearSpell.getInstance());
                    add(ThunderDashSpell.getInstance());
                    add(LightningTotemSpell.getInstance());
                    add(ChargedNovaSpell.getInstance());
                    add(CriticalSurgeSpell.getInstance());
                    add(PowerSurgeSpell.getInstance());
                    add(QuickChargeSpell.getInstance());
                    add(BatteryFusiladeSpell.getInstance());

                    add(BlazingInfernoSpell.getInstance());
                    add(FireballSpell.getInstance());
                    add(VolcanoSpell.getInstance());
                    add(MagmaFlowerSpell.getInstance());
                    add(ThrowFlamesSpell.getInstance());
                    add(FireBombsSpell.getInstance());
                    add(FlameBlitzSpell.getInstance());
                    add(DraconicBloodSpell.getInstance());
                    add(VampiricBloodSpell.getInstance());
                    add(SpellBladeSpell.getInstance());

                    add(ArrowBarrageSpell.getInstance());
                    add(RecoilShotSpell.getInstance());
                    add(MultiShotSpell.getInstance());
                    add(ImbueSpell.getInstance());
                    add(HuntingPotionSpell.getInstance());
                    add(ArrowStormSpell.getInstance());
                    add(WindWalkBuff.getInstance());
                    add(SilentWindBuff.getInstance());
                    add(SnareTrapSpell.getInstance());
                    add(WeakenTrapSpell.getInstance());
                    add(BlastTrapSpell.getInstance());
                    add(DashSpell.getInstance());
                    add(HeavyStrikeSpell.getInstance());
                    add(GroundSlamSpell.getInstance());
                    add(WhirlwindSpell.getInstance());

                }
            }

        };

        All.forEach(x -> x.registerToSlashRegistry());

    }
}
