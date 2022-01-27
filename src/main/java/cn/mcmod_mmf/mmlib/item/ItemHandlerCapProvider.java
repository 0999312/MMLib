package cn.mcmod_mmf.mmlib.item;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerCapProvider implements ICapabilitySerializable<CompoundTag> {
    public ItemStack stack;

    public final ItemStackHandler handler;

    public ItemHandlerCapProvider(ItemStack stack, CompoundTag nbt) {
        this.stack = stack;
        this.handler = new ItemStackHandler();
    }

    public ItemStackHandler getInventory() {
        return handler;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> this.handler).cast();
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.handler.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.handler.deserializeNBT(nbt);
    }

}
