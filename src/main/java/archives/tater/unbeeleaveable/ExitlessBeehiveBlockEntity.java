package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.duck.HasMaxSize;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class ExitlessBeehiveBlockEntity extends BeehiveBlockEntity implements HasMaxSize {
    public ExitlessBeehiveBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public boolean isFullOfBees() {
        return false;
    }

    @Override
    public BlockEntityType<?> getType() {
        return Unbeeleaveable.EXITLESS_BEEHIVE_BLOCK_ENTITY;
    }

    @Override
    public int unbeeleaveable$getMaxSize() {
        return Integer.MAX_VALUE;
    }
}
