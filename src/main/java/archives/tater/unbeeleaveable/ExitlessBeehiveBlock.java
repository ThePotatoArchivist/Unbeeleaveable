package archives.tater.unbeeleaveable;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ExitlessBeehiveBlock extends BeehiveBlock {

    public ExitlessBeehiveBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExitlessBeehiveBlockEntity(pos, state);
    }
}
