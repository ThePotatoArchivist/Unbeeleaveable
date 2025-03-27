package archives.tater.unbeeleaveable.mixin;

import archives.tater.unbeeleaveable.ExitlessBeehiveBlockEntity;
import archives.tater.unbeeleaveable.Unbeeleaveable;
import archives.tater.unbeeleaveable.duck.HasMaxSize;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BeehiveBlockEntity.class)
public class BeehiveBlockEntityMixin {
    @ModifyArg(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;<init>(Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"),
            index = 0
    )
    private static BlockEntityType<?> fixType(BlockEntityType<?> type, @Local(argsOnly = true) BlockState blockState) {
        return blockState.isOf(Unbeeleaveable.EXITLESS_BEEHIVE) ? Unbeeleaveable.EXITLESS_BEEHIVE_BLOCK_ENTITY : type;
    }

    @Inject(
            at = @At("HEAD"),
            method = "releaseBee",
            cancellable = true
    )
    private static void disallowReleaseFromExitless(World world, BlockPos pos, BlockState state, BeehiveBlockEntity.BeeData bee, @Nullable List<Entity> entities, BeehiveBlockEntity.BeeState beeState, @Nullable BlockPos flowerPos, CallbackInfoReturnable<Boolean> cir) {
        if (world.getBlockEntity(pos) instanceof ExitlessBeehiveBlockEntity) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(
            method = "tryEnterHive",
            at = @At(value = "CONSTANT", args = "intValue=3")
    )
    private int checkMaxSized(int original) {
        return this instanceof HasMaxSize hasMaxSize ? hasMaxSize.unbeeleaveable$getMaxSize() : original;
    }
}
