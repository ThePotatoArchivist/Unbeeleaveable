package archives.tater.unbeeleaveable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.TntBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeeBombBlock extends TntBlock implements BlockEntityProvider {
    public BeeBombBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextFloat() > 0.1) return;
        world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_BEEHIVE_WORK, SoundCategory.BLOCKS, 1.0f, 1.0f, true);
    }

    // Will not work due to this event being called after the blockstate is set, so that the block entity is no longer accessible
    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {}

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BeeBombBlockEntity(pos, state);
    }

    public static void explodeBomb(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!world.isClient) {
            var blockEntity = (BeeBombBlockEntity) world.getBlockEntity(pos);
            List<NbtCompound> bees = blockEntity == null ? List.of() : blockEntity.getBees();
            var beeBombEntity =
                    new BeeBombEntity(world, bees, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter);
            world.spawnEntity(beeBombEntity);
            world.playSound(null, beeBombEntity.getX(), beeBombEntity.getY(), beeBombEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }
}
