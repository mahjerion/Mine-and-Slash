package com.robertx22.mine_and_slash.blocks.item_modify_station;

import com.robertx22.mine_and_slash.blocks.bases.BaseTile;
import com.robertx22.mine_and_slash.config.forge.ModConfig;
import com.robertx22.mine_and_slash.database.currency.base.IAddsInstability;
import com.robertx22.mine_and_slash.database.currency.loc_reqs.LocReqContext;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModTileEntities;
import com.robertx22.mine_and_slash.packets.particles.ParticleEnum;
import com.robertx22.mine_and_slash.packets.particles.ParticlePacketData;
import com.robertx22.mine_and_slash.saveclasses.item_classes.IInstability;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.ICommonDataItem;
import com.robertx22.mine_and_slash.uncommon.localization.CLOC;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.RandomUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TileGearModify extends BaseTile {

    @Override
    public boolean isAutomatable() {
        return false;
    }

    @Override
    public boolean isItemValidInput(ItemStack stack) {
        return true;
    }

    @Override
    public int getCookTime() {
        return COOK_TIME_FOR_COMPLETION;
    }

    public enum ModifyResult {
        BREAK, SUCCESS, NONE
    }

    @Override
    public boolean isOutputSlot(int slot) {
        return false;
    }

    public ResultItem getSmeltingResultForItem(ItemStack stack) {

        LocReqContext context = getLocReqContext();

        if (context.isValid() == false) {
            return new ResultItem(ItemStack.EMPTY, ModifyResult.NONE);
        }

        if (context.effect != null) {

            if (context.effect.canItemBeModified(context)) {
                ItemStack copy = context.stack.copy();
                copy = context.effect.ModifyItem(copy, context.Currency);

                ICommonDataItem data = ICommonDataItem.load(copy);

                if (ModConfig.INSTANCE.Server.ENABLE_CURRENCY_ITEMS_INSTABILITY_SYSTEM.get()) {
                    if (data instanceof IInstability && context.Currency.getItem() instanceof IAddsInstability) {

                        IInstability insta = (IInstability) data;
                        IAddsInstability addsInta = (IAddsInstability) context.Currency.getItem();

                        if (insta.isInstabilityCapReached() && !addsInta.canBeUsedAtFullInstability()) {
                            copy = ItemStack.EMPTY;
                        } else {
                            boolean broke = false;
                            if (insta.usesBreakChance()) {
                                if (addsInta.activatesBreakRoll()) {
                                    float breakChance =
                                        (addsInta.additionalBreakChance() + insta.getBreakChance()) * addsInta
                                            .breakChanceMulti();
                                    if (RandomUtils.roll(breakChance)) {
                                        copy = new ItemStack(Items.GUNPOWDER);
                                        broke = true;
                                        return new ResultItem(new ItemStack(Items.GUNPOWDER), ModifyResult.BREAK);
                                    }
                                }
                            }
                            if (broke == false) {
                                insta.increaseInstability(addsInta.instabilityAddAmount());
                                data.saveToStack(copy);
                            }
                        }
                    }
                }

                return new ResultItem(copy, ModifyResult.SUCCESS);
            } else {
                return new ResultItem(ItemStack.EMPTY, ModifyResult.NONE);
            }

        }

        return new ResultItem(ItemStack.EMPTY, ModifyResult.NONE);
    }

    public LocReqContext getLocReqContext() {

        ItemStack gearStack = this.GearSlot();
        ItemStack craftStack = this.CraftItemSlot();

        LocReqContext context = new LocReqContext(gearStack, craftStack, this);

        return context;

    }

    public ItemStack GearSlot() {
        return itemStacks[FIRST_INPUT_SLOT];
    }

    public ItemStack CraftItemSlot() {
        return itemStacks[FIRST_INPUT_SLOT + 1];
    }

    public ItemStack OutputSot() {
        return itemStacks[FIRST_INPUT_SLOT + 2];
    }

    public void setOutputSot(ItemStack stack) {
        itemStacks[FIRST_INPUT_SLOT + 2] = stack;
    }

    // IMPORTANT STUFF ABOVE

    // Create and initialize the itemStacks variable that will store store the
    // itemStacks
    public static final int INPUT_SLOTS_COUNT = 2;
    public static final int OUTPUT_SLOTS_COUNT = 1;
    public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    public static final int FIRST_INPUT_SLOT = 0;
    public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;

    private static final short COOK_TIME_FOR_COMPLETION = 40; // vanilla value is 200 = 10 seconds

    public TileGearModify() {
        super(ModTileEntities.GEAR_MODIFY.get());
        itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
        clear();

    }

    @Override
    public int ticksRequired() {
        return COOK_TIME_FOR_COMPLETION;
    }

    @Override
    public void finishCooking() {
        this.modifyItem();
    }

    @Override
    public boolean isCooking() {
        return canModifyItem();
    }

    @Override
    public int tickRate() {
        return 1;
    }

    @Override
    public void doActionEveryTime() {

    }

    public double fractionOfCookTimeComplete() {
        double fraction = cookTime / (double) getCookTime();
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    static class ResultItem {

        ItemStack stack;
        ModifyResult resultEnum;

        public ResultItem(ItemStack stack, ModifyResult resultEnum) {
            this.stack = stack;
            this.resultEnum = resultEnum;
        }
    }

    private ResultItem getResult() {

        return getSmeltingResultForItem(this.GearSlot());

    }

    private boolean canModifyItem() {

        ItemStack gear = this.GearSlot();

        if (gear.isEmpty()) {
            return false;
        }

        if (getSmeltingResultForItem(gear).stack.isEmpty()) {
            return false;
        }

        if (OutputSot().isEmpty() == false) {
            return false;
        }

        return true;

    }

    private boolean modifyItem() {

        if (this.canModifyItem()) {

            ResultItem result = this.getResult();

            this.GearSlot()
                .shrink(1);
            this.setOutputSot(result.stack.copy());
            this.OutputSot().setCount(1);
            result.stack = ItemStack.EMPTY;
            this.CraftItemSlot()
                .shrink(1);

            markDirty();

            if (result.resultEnum.equals(ModifyResult.BREAK)) {

                SoundUtils.playSound(world, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);

                ParticleEnum.sendToClients(
                    pos.up(), world, new ParticlePacketData(pos.up(), ParticleEnum.AOE).radius(0.5F)
                        .type(ParticleTypes.POOF)
                        .motion(new Vec3d(0, 0, 0))
                        .amount(5));

            } else if (result.resultEnum.equals(ModifyResult.SUCCESS)) {
                SoundUtils.ding(world, pos);

                ParticleEnum.sendToClients(
                    pos.up(), world, new ParticlePacketData(pos.up(), ParticleEnum.AOE).radius(0.5F)
                        .type(ParticleTypes.COMPOSTER)
                        .amount(15));

            }

            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Container createMenu(int num, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerGearModify(num, inventory, this, this.getPos());
    }

    @Override
    public ITextComponent getDisplayName() {
        return CLOC.blank("block.mmorpg.modify_station");
    }
}