package net.liukrast.repackaged.content.fluid;

import net.liukrast.deployer.lib.logistics.packager.AbstractPackagerBlockEntity;
import net.liukrast.deployer.lib.logistics.packager.GenericUnpackingHandler;
import net.liukrast.deployer.lib.logistics.stockTicker.GenericOrderContained;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum DefaultFluidUnpackProcedure implements GenericUnpackingHandler<Fluid, FluidStack, IFluidHandler> {
    INSTANCE;

    @Override
    public boolean unpack(Level level, BlockPos pos, BlockState state, Direction side, List<FluidStack> items, @Nullable GenericOrderContained<FluidStack> orderContext, boolean simulate, AbstractPackagerBlockEntity<Fluid, FluidStack, IFluidHandler> packager) {
        BlockEntity targetBE = level.getBlockEntity(pos);
        if(targetBE == null)
            return false;

        IFluidHandler targetInv = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, targetBE, side);
        if(targetInv == null)
            return false;

        if(simulate) {
            for(FluidStack fluid : items) {
                int filled = targetInv.fill(fluid.copy(), IFluidHandler.FluidAction.SIMULATE);
                if(filled < fluid.getAmount())
                    return false;
            }
            return true;
        } else {
            for(FluidStack fluid : items) {
                targetInv.fill(fluid.copy(), IFluidHandler.FluidAction.EXECUTE);
            }
            return true;
        }
    }
}
