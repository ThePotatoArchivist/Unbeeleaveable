package archives.tater.unbeeleaveable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;

public class ExitlessBeehiveBlockEntity extends BeehiveBlockEntity {
    public ExitlessBeehiveBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public boolean isFullOfBees() {
        return false;
    }

    @Override
    public void tryEnterHive(Entity entity, boolean hasNectar, int ticksInHive) {
        entity.stopRiding();
        entity.removeAllPassengers();
        NbtCompound nbtCompound = new NbtCompound();
        entity.saveNbt(nbtCompound);
        this.addBee(nbtCompound, ticksInHive, hasNectar);
        if (this.world != null) {
            BlockPos blockPos = this.getPos();
            this.world.playSound((PlayerEntity) null, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(entity, this.getCachedState()));
        }

        entity.discard();
        super.markDirty();
    }

    @Override
    public BlockEntityType<?> getType() {
        return Unbeeleaveable.EXITLESS_BEEHIVE_BLOCK_ENTITY;
    }
}
