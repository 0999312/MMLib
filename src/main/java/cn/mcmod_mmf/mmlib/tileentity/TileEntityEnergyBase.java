package cn.mcmod_mmf.mmlib.tileentity;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEntityEnergyBase extends TileEntity {

    public TileEntityEnergyBase(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    protected int energy = 0;

    // A public interface to access how much energy does a tile have
    public abstract int getEnergyStored();

    // Then how much it can store
    public abstract int getMaxEnergyStored();

    // Is this direction acceptable for energy
    public abstract boolean isDirectionAcceptable(Direction side);

    public abstract LazyOptional<IEnergyStorage> getLazyOptional();

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.putInt("energy", this.energy);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        this.energy = tag.getInt("energy");

    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(getLevel().getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        boolean isEnergy = Objects.equals(cap, CapabilityEnergy.ENERGY) && isDirectionAcceptable(side);
        return isEnergy ? this.getLazyOptional().cast() : super.getCapability(cap, side);
    }

}
