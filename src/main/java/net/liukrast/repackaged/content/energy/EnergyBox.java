package net.liukrast.repackaged.content.energy;

import com.simibubi.create.AllSoundEvents;
import net.liukrast.deployer.lib.logistics.packager.CustomPackageStyle;
import net.liukrast.deployer.lib.logistics.packager.StockInventoryType;
import net.liukrast.repackaged.RepackagedConfig;
import net.liukrast.repackaged.content.UnwrappablePackageItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.function.Supplier;

public class EnergyBox extends UnwrappablePackageItem {

    public EnergyBox(Properties properties, CustomPackageStyle style, Supplier<StockInventoryType<?, ?, ?>> type) {
        super(properties, style, type);
    }

    public EnergyBox(Properties properties, CustomPackageStyle style, Supplier<StockInventoryType<?, ?, ?>> type, String descriptionId) {
        super(properties, style, type, descriptionId);
    }

    public EnergyBox(Properties properties, CustomPackageStyle style, boolean cardboard, Supplier<StockInventoryType<?, ?, ?>> type, String descriptionId) {
        super(properties, style, cardboard, type, descriptionId);
    }

    @Override
    public InteractionResultHolder<ItemStack> open(Level worldIn, Player playerIn, InteractionHand handIn) {
        if(worldIn.isClientSide())
            return super.open(worldIn, playerIn, handIn);
        ItemStack box = playerIn.getItemInHand(handIn);
        var energy = box.getCapability(Capabilities.EnergyStorage.ITEM);
        if(energy != null && energy.getEnergyStored() >= RepackagedConfig.Server.BATTERY_LIGHTNING_SPAWN.getAsInt()) {
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(worldIn);
            if(lightningBolt != null) {
                lightningBolt.moveTo(playerIn.position());
                worldIn.addFreshEntity(lightningBolt);
            }
        }
        playerIn.setItemInHand(handIn, box.getCount() <= 1 ? ItemStack.EMPTY : box.copyWithCount(box.getCount() - 1));
        AllSoundEvents.PACKAGE_POP.playOnServer(worldIn, playerIn.blockPosition());
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, box);
    }
}
