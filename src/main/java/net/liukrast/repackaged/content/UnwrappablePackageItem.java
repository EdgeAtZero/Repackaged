package net.liukrast.repackaged.content;

import com.simibubi.create.AllDamageTypes;
import net.liukrast.deployer.lib.logistics.packager.CustomPackageStyle;
import net.liukrast.deployer.lib.logistics.packager.GenericPackageItem;
import net.liukrast.deployer.lib.logistics.packager.StockInventoryType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class UnwrappablePackageItem extends GenericPackageItem {

    public UnwrappablePackageItem(Properties properties, CustomPackageStyle style, Supplier<StockInventoryType<?, ?, ?>> type) {
        super(properties, style, type);
    }

    public UnwrappablePackageItem(Properties properties, CustomPackageStyle style, Supplier<StockInventoryType<?, ?, ?>> type, String descriptionId) {
        super(properties, style, type, descriptionId);
    }

    public UnwrappablePackageItem(Properties properties, CustomPackageStyle style, boolean cardboard, Supplier<StockInventoryType<?, ?, ?>> type, String descriptionId) {
        super(properties, style, cardboard, type, descriptionId);
    }

    @Override
    public boolean canBeHurtBy(@NotNull ItemStack stack, DamageSource source) {
        if(source.is(AllDamageTypes.SAW)) return false;
        return super.canBeHurtBy(stack, source);
    }

    @Override
    public InteractionResultHolder<ItemStack> open(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack box = playerIn.getItemInHand(handIn);
        return new InteractionResultHolder<>(InteractionResult.FAIL, box);
    }
}
