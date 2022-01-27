package cn.mcmod_mmf.mmlib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerCapProvider implements ICapabilitySerializable<CompoundNBT> {
    protected ItemStack stack;

    protected final ItemStackHandler handler;

    protected final int inventorySize;

    public ItemHandlerCapProvider(ItemStack stack, int invSize, CompoundNBT nbt) {
        this.stack = stack;
        this.inventorySize = invSize;
        this.handler = new ItemStackHandler(invSize);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> this.handler).cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.handler.deserializeNBT(nbt);
    }

}
