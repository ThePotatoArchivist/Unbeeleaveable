package archives.tater.unbeeleaveable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity.BeeData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class BeeBombBlockEntity extends BlockEntity {
    public BeeBombBlockEntity(BlockPos pos, BlockState state) {
        super(Unbeeleaveable.BEE_BOMB_BLOCK_ENTITY, pos, state);
    }

    private List<BeeData> bees = new ArrayList<>(Collections.nCopies(3, BeeData.create(0)));

    public List<BeeData> getBees() {
        return bees;
    }

    private void setBees(Iterable<BeeData> bees) {
        this.bees.clear();
        bees.forEach(this.bees::add);
    }

    public static final String BEES_KEY = "Bees";

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        bees.clear();
        bees = components.getOrDefault(DataComponentTypes.BEES, List.of());
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataComponentTypes.BEES, bees);
    }

    @Override
    public void removeFromCopiedStackNbt(NbtCompound nbt) {
        super.removeFromCopiedStackNbt(nbt);
        nbt.remove(BEES_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.put(BEES_KEY, BeeData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, bees).getOrThrow());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (!nbt.contains(BEES_KEY)) {
            bees.clear();
            return;
        }

        setBees(BeeData.LIST_CODEC
                .parse(NbtOps.INSTANCE, nbt.get(BEES_KEY))
                .resultOrPartial(string -> Unbeeleaveable.LOGGER.error("Failed to parse bees: '{}'", string))
                .orElse(List.of()));
    }
}
