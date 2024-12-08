package archives.tater.unbeeleaveable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class BeeBombBlockEntity extends BlockEntity {
    public BeeBombBlockEntity(BlockPos pos, BlockState state) {
        super(Unbeeleaveable.BEE_BOMB_BLOCK_ENTITY, pos, state);
    }

    private List<NbtCompound> bees = List.of(new NbtCompound(), new NbtCompound(), new NbtCompound());

    public List<NbtCompound> getBees() {
        return bees;
    }

    public static final String BEES_KEY = "Bees";

    @Override
    protected void writeNbt(NbtCompound nbt) {
        var list = new NbtList();
        list.addAll(bees);
        nbt.put(BEES_KEY, list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        bees = nbt.getList(BEES_KEY, NbtCompound.COMPOUND_TYPE).stream().map(nbtElement -> (NbtCompound) nbtElement).toList();
    }
}
