package net.liukrast.repackaged.mixin;

import com.simibubi.create.content.logistics.packagePort.frogport.FrogportBlockEntity;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;

import net.liukrast.repackaged.content.logistics.PackagerConnectorBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackagerBlockEntity.class)
public class PackagerBlockEntityMixin {

    @Inject(method = "wakeTheFrogs", at = @At("HEAD"), cancellable = true)
    private void wakeTheFrogs(CallbackInfo ci) {
        PackagerBlockEntity packager = (PackagerBlockEntity) (Object) this;
        if (!(packager.getLevel().getBlockEntity(packager.getBlockPos().relative(Direction.UP)) instanceof FrogportBlockEntity))
            return;

        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = packager.getLevel().getBlockEntity(packager.getBlockPos().relative(direction));
            if (!(blockEntity instanceof PackagerConnectorBlockEntity connector))
                continue;
            if (connector.getTargetContainer() != packager.inventory)
                continue;

            connector.sendToNext(packager.inventory);
            if (packager.inventory.extractItem(0, 1, true).isEmpty()) {
                ci.cancel();
                return;
            }
        }
    }
}
