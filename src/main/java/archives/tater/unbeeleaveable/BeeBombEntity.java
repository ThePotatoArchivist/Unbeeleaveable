package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.mixin.TntEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;

import java.util.List;
import java.util.stream.Collectors;

public class BeeBombEntity extends TntEntity implements CustomExplodable {

    private static final double ANGER_RANGE = 16.0;
    private static final double MAX_VELOCITY = 0.5;
    private static final String BEES_KEY = "Bees";

    private List<NbtCompound> bees;

    public BeeBombEntity(EntityType<? extends BeeBombEntity> entityType, World world) {
        super(entityType, world);
        this.bees = List.of(new NbtCompound(), new NbtCompound(), new NbtCompound());
    }

    public BeeBombEntity(World world, List<NbtCompound> bees, double x, double y, double z, LivingEntity igniter) {
        this(Unbeeleaveable.BEE_BOMB_ENTITY, world);
        this.bees = bees;
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * Math.PI * 2;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;

        // Casting and using accessor
        ((TntEntityAccessor) this).setCausingEntity(igniter);
    }

    @Override
    public void explodeCustom() {
        this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 1.0f, ExplosionSourceType.TNT);
        List<Entity> entities = this.getWorld().getOtherEntities(null, Box.of(this.getPos(), ANGER_RANGE, ANGER_RANGE, ANGER_RANGE),
                e -> e instanceof LivingEntity && !(e instanceof BeeEntity));

        for (NbtCompound bee : bees) {
            BeeEntity beeEntity = EntityType.BEE.create(this.getWorld());
            if (beeEntity != null) {
                beeEntity.readNbt(bee);
                beeEntity.refreshPositionAndAngles(this.getBlockPos(), this.getYaw(), this.getPitch());
                beeEntity.setVelocity(
                        this.random.nextTriangular(0.0, MAX_VELOCITY),
                        this.random.nextTriangular(0.0, MAX_VELOCITY),
                        this.random.nextTriangular(0.0, MAX_VELOCITY)
                );
                this.getWorld().spawnEntity(beeEntity);
                beeEntity.universallyAnger();
                if (!entities.isEmpty()) {
                    beeEntity.setTarget((LivingEntity) entities.get(this.random.nextInt(entities.size())));
                }
            }
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        NbtList beeList = new NbtList();
        beeList.addAll(this.bees);
        nbt.put(BEES_KEY, beeList);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.bees = nbt.getList(BEES_KEY, NbtCompound.COMPOUND_TYPE).stream()
                .map(tag -> (NbtCompound) tag)
                .collect(Collectors.toList());
    }
}
