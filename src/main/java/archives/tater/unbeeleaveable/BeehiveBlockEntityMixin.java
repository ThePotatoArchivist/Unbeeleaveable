package archives.tater.unbeeleaveable.mixin;

import archives.tater.unbeeleaveable.ExitlessBeehiveBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
    @Inject(
            at = @At("HEAD"),
            method = "releaseBee",
            cancellable = true
    )
    private static void disallowReleaseFromExitless(World world, BlockPos pos, BlockState state, BeehiveBlockEntity.Bee bee, List<Entity> entities, BeehiveBlockEntity.BeeState beeState, BlockPos flowerPos, CallbackInfoReturnable<Boolean> cir) {
        if (world.getBlockEntity(pos) instanceof ExitlessBeehiveBlockEntity) {
            cir.setReturnValue(false);
        }
    }
}
