package archives.tater.unbeeleaveable.mixin;

import archives.tater.unbeeleaveable.duck.CustomExplodable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.TntEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {
    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/TntEntity;explode()V")
    )
    public void useCustomExplosion(TntEntity instance, Operation<Void> original) {
        if (!(instance instanceof CustomExplodable explodable)) {
            original.call(instance);
            return;
        }
        explodable.explodeCustom();
    }
}
