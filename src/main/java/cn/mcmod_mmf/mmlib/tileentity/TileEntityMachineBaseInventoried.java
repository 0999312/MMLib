package cn.mcmod_mmf.mmlib.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;

public abstract class TileEntityMachineBaseInventoried extends TileEntityMachineBase implements ISidedInventory {

    protected NonNullList<ItemStack> inventory;
    protected int size;

    public TileEntityMachineBaseInventoried(TileEntityType<?> p_i48289_1_, int size) {
        super(p_i48289_1_);
        this.size = size;
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public ItemStack getItem(int i) {
        return inventory.get(i);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return ItemStackHelper.removeItem(this.inventory, p_70298_1_, p_70298_2_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ItemStackHelper.takeItem(this.inventory, p_70304_1_);
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        ItemStack itemstack = this.inventory.get(p_70299_1_);
        boolean flag = !p_70299_2_.isEmpty() && p_70299_2_.sameItem(itemstack)
                && ItemStack.tagMatches(p_70299_2_, itemstack);
        this.inventory.set(p_70299_1_, p_70299_2_);
        if (p_70299_2_.getCount() > this.getMaxStackSize()) {
            p_70299_2_.setCount(this.getMaxStackSize());
        }

        if (p_70299_1_ == 0 && !flag) {
            this.maxTime = this.getMaxTime();
            this.processTime = 0;
            this.setChanged();
        }

    }

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return p_70300_1_.distanceToSqr((double) this.worldPosition.getX() + 0.5D,
                    (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

}
