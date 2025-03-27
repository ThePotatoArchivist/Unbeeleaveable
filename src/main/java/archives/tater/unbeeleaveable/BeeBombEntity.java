package archives.tater.unbeeleaveable;

import archives.tater.unbeeleaveable.duck.CustomExplodable;
import archives.tater.unbeeleaveable.mixin.TntEntityAccessor;
import net.minecraft.block.entity.BeehiveBlockEntity.BeeData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;

import java.util.Collections;
import java.util.List;

public class BeeBombEntity extends TntEntity implements CustomExplodable {

    private static final double ANGER_RANGE = 16.0;
    private static final double MAX_VELOCITY = 0.5;
    private static final String BEES_KEY = "Bees";

    private List<BeeData> bees;

    public BeeBombEntity(EntityType<? extends BeeBombEntity> entityType, World world) {
        super(entityType, world);
        this.bees = Collections.nCopies(3, BeeData.create(0));
    }

    public BeeBombEntity(World world, List<BeeData> bees, double x, double y, double z, LivingEntity igniter) {
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
        getWorld().createExplosion(this, getX(), getBodyY(0.0625), getZ(), 1.0f, ExplosionSourceType.TNT);

        var entities = getWorld().getOtherEntities(null, Box.of(getPos(), 2*ANGER_RANGE, 2*ANGER_RANGE, 2*ANGER_RANGE),
                e -> e instanceof LivingEntity livingEntity && !(e instanceof BeeEntity) && !livingEntity.isInvulnerable());

        for (var entity : entities)
            if (entity instanceof ServerPlayerEntity serverPlayerEntity)
                Unbeeleaveable.NEAR_BEE_EXPLOSION.trigger(serverPlayerEntity);

        var targets = entities.stream().filter(e -> !e.equals(getOwner())).toList();

        for (var bee : bees) {
            var beeEntity = bee.loadEntity(getWorld(), getBlockPos());
            if (beeEntity == null) continue;
            beeEntity.refreshPositionAndAngles(getBlockPos(), getYaw(), getPitch());
            beeEntity.setVelocity(
                    random.nextTriangular(0.0, MAX_VELOCITY),
                    random.nextTriangular(0.0, MAX_VELOCITY),
                    random.nextTriangular(0.0, MAX_VELOCITY)
            );
            getWorld().spawnEntity(beeEntity);
            if (beeEntity instanceof Angerable angerable)
                angerable.universallyAnger();
            if (beeEntity instanceof MobEntity mobEntity && !targets.isEmpty())
                mobEntity.setTarget((LivingEntity) targets.get(random.nextInt(targets.size())));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put(BEES_KEY, BeeData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, bees).getOrThrow());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (!nbt.contains(BEES_KEY)) {
            bees.clear();
            return;
        }

        bees = BeeData.LIST_CODEC
                .parse(NbtOps.INSTANCE, nbt.get(BEES_KEY))
                .resultOrPartial(string -> Unbeeleaveable.LOGGER.error("Failed to parse bees: '{}'", string))
                .orElse(List.of());
    }
}
