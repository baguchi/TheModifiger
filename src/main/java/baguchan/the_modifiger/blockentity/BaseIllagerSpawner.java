package baguchan.the_modifiger.blockentity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public abstract class BaseIllagerSpawner {
    public static final String SPAWN_DATA_TAG = "SpawnData";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int EVENT_SPAWN = 1;
    private int spawnDelay = 10;
    private SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();
    @Nullable
    private SpawnData nextSpawnData;
    private double spin;
    private double oSpin;
    private int minSpawnDelay = 10;
    private int maxSpawnDelay = 20;
    private int spawnCount = 4;
    @Nullable
    private Entity displayEntity;
    private int maxNearbyEntities = 6;
    private int spawnRange = 4;

    public BaseIllagerSpawner() {
    }

    public void setEntityId(EntityType<?> p_253682_, @Nullable Level p_254041_, RandomSource p_254221_, BlockPos p_254050_) {
        this.getOrCreateNextSpawnData(p_254041_, p_254221_, p_254050_).getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(p_253682_).toString());
    }

    public void clientTick(Level p_151320_, BlockPos p_151321_) {
         if (this.displayEntity != null) {
            RandomSource randomsource = p_151320_.getRandom();
            double d0 = (double)p_151321_.getX() + randomsource.nextDouble();
            double d1 = (double)p_151321_.getY() + randomsource.nextDouble();
            double d2 = (double)p_151321_.getZ() + randomsource.nextDouble();
            p_151320_.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            p_151320_.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }

            this.oSpin = this.spin;
            this.spin = (this.spin + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0;
        }

    }

    public void serverTick(ServerLevel p_151312_, BlockPos p_151313_) {
            if (this.spawnDelay == -1) {
                this.delay(p_151312_, p_151313_);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            } else {
                boolean flag = false;
                RandomSource randomsource = p_151312_.getRandom();
                SpawnData spawndata = this.getOrCreateNextSpawnData(p_151312_, randomsource, p_151313_);
                int i = 0;

                while(true) {
                    if (i >= this.spawnCount) {
                        if (flag) {
                            this.delay(p_151312_, p_151313_);
                        }
                        break;
                    }

                    CompoundTag compoundtag = spawndata.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        this.delay(p_151312_, p_151313_);
                        return;
                    }

                    ListTag listtag = compoundtag.getList("Pos", 6);
                    int j = listtag.size();
                    double d0 = j >= 1 ? listtag.getDouble(0) : (double)p_151313_.getX() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)this.spawnRange + 0.5;
                    double d1 = j >= 2 ? listtag.getDouble(1) : (double)(p_151313_.getY() + randomsource.nextInt(3) - 1);
                    double d2 = j >= 3 ? listtag.getDouble(2) : (double)p_151313_.getZ() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)this.spawnRange + 0.5;
                    if (p_151312_.noCollision(((EntityType)optional.get()).getAABB(d0, d1, d2))) {
                        label105: {
                            BlockPos blockpos = BlockPos.containing(d0, d1, d2);
                            if (spawndata.getCustomSpawnRules().isPresent()) {
                                if (!((EntityType)optional.get()).getCategory().isFriendly() && p_151312_.getDifficulty() == Difficulty.PEACEFUL) {
                                    break label105;
                                }
                            } else if (!SpawnPlacements.checkSpawnRules((EntityType)optional.get(), p_151312_, MobSpawnType.SPAWNER, blockpos, p_151312_.getRandom())) {
                                break label105;
                            }

                            Entity entity = EntityType.loadEntityRecursive(compoundtag, p_151312_, (p_151310_) -> {
                                p_151310_.moveTo(d0, d1, d2, p_151310_.getYRot(), p_151310_.getXRot());
                                return p_151310_;
                            });
                            if (entity == null) {
                                this.delay(p_151312_, p_151313_);
                                return;
                            }

                            int k = p_151312_.getEntitiesOfClass(entity.getClass(), (new AABB((double)p_151313_.getX(), (double)p_151313_.getY(), (double)p_151313_.getZ(), (double)(p_151313_.getX() + 1), (double)(p_151313_.getY() + 1), (double)(p_151313_.getZ() + 1))).inflate((double)this.spawnRange)).size();
                            if (k >= this.maxNearbyEntities) {
                                this.delay(p_151312_, p_151313_);
                                return;
                            }

                            entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), randomsource.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof Mob) {
                                Mob mob = (Mob)entity;
                                if (!ForgeEventFactory.checkSpawnPositionSpawner(mob, p_151312_, MobSpawnType.SPAWNER, spawndata, null)) {
                                    break label105;
                                }

                                MobSpawnEvent.FinalizeSpawn event = ForgeEventFactory.onFinalizeSpawnSpawner(mob, p_151312_, p_151312_.getCurrentDifficultyAt(entity.blockPosition()), (SpawnGroupData)null, compoundtag, null);
                                if (event != null && spawndata.getEntityToSpawn().size() == 1 && spawndata.getEntityToSpawn().contains("id", 8)) {
                                    ((Mob)entity).finalizeSpawn(p_151312_, event.getDifficulty(), event.getSpawnType(), event.getSpawnData(), event.getSpawnTag());
                                }
                            }

                            if (!p_151312_.tryAddFreshEntityWithPassengers(entity)) {
                                this.delay(p_151312_, p_151313_);
                                return;
                            }

                            p_151312_.levelEvent(2004, p_151313_, 0);
                            p_151312_.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
                            if (entity instanceof Mob) {
                                ((Mob)entity).spawnAnim();
                            }

                            flag = true;
                        }
                    }

                    ++i;
                }
            }
    }

    private void delay(Level p_151351_, BlockPos p_151352_) {
        RandomSource randomsource = p_151351_.random;
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            this.spawnDelay = this.minSpawnDelay + randomsource.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
        }

        this.spawnPotentials.getRandom(randomsource).ifPresent((p_186386_) -> {
            this.setNextSpawnData(p_151351_, p_151352_, (SpawnData)p_186386_.getData());
        });
        this.broadcastEvent(p_151351_, p_151352_, 1);
    }

    public void load(@Nullable Level p_151329_, BlockPos p_151330_, CompoundTag p_151331_) {
        this.spawnDelay = p_151331_.getShort("Delay");
        boolean flag = p_151331_.contains("SpawnData", 10);
        if (flag) {
            SpawnData spawndata = (SpawnData)SpawnData.CODEC.parse(NbtOps.INSTANCE, p_151331_.getCompound("SpawnData")).resultOrPartial((p_186391_) -> {
                LOGGER.warn("Invalid SpawnData: {}", p_186391_);
            }).orElseGet(SpawnData::new);
            this.setNextSpawnData(p_151329_, p_151330_, spawndata);
        }

        boolean flag1 = p_151331_.contains("SpawnPotentials", 9);
        if (flag1) {
            ListTag listtag = p_151331_.getList("SpawnPotentials", 10);
            this.spawnPotentials = (SimpleWeightedRandomList)SpawnData.LIST_CODEC.parse(NbtOps.INSTANCE, listtag).resultOrPartial((p_186388_) -> {
                LOGGER.warn("Invalid SpawnPotentials list: {}", p_186388_);
            }).orElseGet(SimpleWeightedRandomList::empty);
        } else {
            this.spawnPotentials = SimpleWeightedRandomList.single(this.nextSpawnData != null ? this.nextSpawnData : new SpawnData());
        }

        if (p_151331_.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = p_151331_.getShort("MinSpawnDelay");
            this.maxSpawnDelay = p_151331_.getShort("MaxSpawnDelay");
            this.spawnCount = p_151331_.getShort("SpawnCount");
        }

        if (p_151331_.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = p_151331_.getShort("MaxNearbyEntities");
        }

        if (p_151331_.contains("SpawnRange", 99)) {
            this.spawnRange = p_151331_.getShort("SpawnRange");
        }

        this.displayEntity = null;
    }

    public CompoundTag save(CompoundTag p_186382_) {
        p_186382_.putShort("Delay", (short)this.spawnDelay);
        p_186382_.putShort("MinSpawnDelay", (short)this.minSpawnDelay);
        p_186382_.putShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        p_186382_.putShort("SpawnCount", (short)this.spawnCount);
        p_186382_.putShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
       p_186382_.putShort("SpawnRange", (short)this.spawnRange);
        if (this.nextSpawnData != null) {
            p_186382_.put("SpawnData", (Tag)SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> {
                return new IllegalStateException("Invalid SpawnData");
            }));
        }

        p_186382_.put("SpawnPotentials", (Tag)SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return p_186382_;
    }

    @Nullable
    public Entity getOrCreateDisplayEntity(Level p_254323_, RandomSource p_254353_, BlockPos p_254313_) {
        if (this.displayEntity == null) {
            CompoundTag compoundtag = this.getOrCreateNextSpawnData(p_254323_, p_254353_, p_254313_).getEntityToSpawn();
            if (!compoundtag.contains("id", 8)) {
                return null;
            }

            this.displayEntity = EntityType.loadEntityRecursive(compoundtag, p_254323_, Function.identity());
            if (compoundtag.size() == 1 && this.displayEntity instanceof Mob) {
            }
        }

        return this.displayEntity;
    }

    public boolean onEventTriggered(Level p_151317_, int p_151318_) {
        if (p_151318_ == 1) {
            if (p_151317_.isClientSide) {
                this.spawnDelay = this.minSpawnDelay;
            }

            return true;
        } else {
            return false;
        }
    }

    protected void setNextSpawnData(@Nullable Level p_151325_, BlockPos p_151326_, SpawnData p_151327_) {
        this.nextSpawnData = p_151327_;
    }

    private SpawnData getOrCreateNextSpawnData(@Nullable Level p_254503_, RandomSource p_253892_, BlockPos p_254487_) {
        if (this.nextSpawnData != null) {
            return this.nextSpawnData;
        } else {
            this.setNextSpawnData(p_254503_, p_254487_, (SpawnData)this.spawnPotentials.getRandom(p_253892_).map(WeightedEntry.Wrapper::getData).orElseGet(SpawnData::new));
            return this.nextSpawnData;
        }
    }

    public abstract void broadcastEvent(Level var1, BlockPos var2, int var3);

    public double getSpin() {
        return this.spin;
    }

    public double getoSpin() {
        return this.oSpin;
    }

    @Nullable
    public Entity getSpawnerEntity() {
        return null;
    }

    @Nullable
    public BlockEntity getSpawnerBlockEntity() {
        return null;
    }
}
