package baguchan.the_modifiger.entity;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.entity.goal.ConstructGoal;
import baguchan.the_modifiger.registry.ModBlocks;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Modifiger extends AbstractIllager {
    private static final String[] STRUCTURE_LOCATIONS = new String[]{"the_modifiger:spawner_1", "the_modifiger:arrow_spawner"};

    private Optional<BlockPos> buildingPos = Optional.empty();
    private List<BoundingBox> buildingBoundingBoxPos = Lists.newArrayList();
    private ResourceLocation buildingStructureName;
    private int buildingStep;
    public AnimationState idleAnimationState = new AnimationState();
    public AnimationState buildingAnimationState = new AnimationState();
    private int idleCooldown;
    private int buildCooldown;
    private int buildCount;

    public Modifiger(EntityType<? extends Modifiger> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ConstructGoal(this, STRUCTURE_LOCATIONS, 0.8F));

        this.goalSelector.addGoal(2, new RaiderOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, IronGolem.class, 12F, 1F, 1F));

         this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.65D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers(AbstractIllager.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true).setUnseenMemoryTicks(300));

    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    public void addBuildingBoundingBox(BoundingBox buildingBoundingBox) {
        this.buildingBoundingBoxPos.add(buildingBoundingBox);
    }

    public List<BoundingBox> getBuildingBoundingBoxPos() {
        return buildingBoundingBoxPos;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_37856_, DifficultyInstance p_37857_, MobSpawnType p_37858_, @Nullable SpawnGroupData p_37859_, @Nullable CompoundTag p_37860_) {
        return super.finalizeSpawn(p_37856_, p_37857_, p_37858_, p_37859_, p_37860_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 32.0D).add(Attributes.ARMOR, 8F).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 8.0D).add(Attributes.FOLLOW_RANGE, 30.0D);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_33306_) {
        return SoundEvents.PILLAGER_HURT;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    public Optional<BlockPos> getBuildingPos() {
        return this.buildingPos;
    }

    public void setBuildingPos(Optional<BlockPos> buildingPos) {
        this.buildingPos = buildingPos;
    }

    public int getBuildingStep() {
        return buildingStep;
    }

    public void setBuildingStep(int buildingStep) {
        this.buildingStep = buildingStep;
    }

    public ResourceLocation getBuildingStructureName() {
        return buildingStructureName;
    }

    public void setBuildingStructureName(ResourceLocation buildingStructureName) {
        this.buildingStructureName = buildingStructureName;
    }

    public int getBuildCooldown() {
        return buildCooldown;
    }

    public void setBuildCooldown(int buildCooldown) {
        this.buildCooldown = buildCooldown;
    }

    public int getBuildCount() {
        return buildCount;
    }

    public void setBuildCount(int buildCount) {
        this.buildCount = buildCount;
    }

    public boolean isAlliedTo(Entity p_33314_) {
        if (super.isAlliedTo(p_33314_)) {
            return true;
        } else if (p_33314_ instanceof LivingEntity && ((LivingEntity) p_33314_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_33314_.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.level().isClientSide()) {
            if(!this.buildingAnimationState.isStarted()) {
                if (this.idleCooldown <= 0) {
                    this.idleCooldown = 60 + this.random.nextInt(40);
                    this.idleAnimationState.start(this.tickCount);
                } else {
                    --this.idleCooldown;
                }
            }else {
                this.idleAnimationState.stop();
            }
            this.buildingAnimationState.animateWhen(this.getPose() == Pose.SITTING, this.tickCount);
        } else if (this.getBuildCooldown() > 0) {
            this.setBuildCooldown(this.buildCooldown - 1);
        }
        super.tick();
    }


    @Override
    public void die(DamageSource p_37847_) {
        super.die(p_37847_);
        if (this.level() instanceof ServerLevel serverLevel) {
            for (BoundingBox boundingBox : this.getBuildingBoundingBoxPos()) {
                for (int x = boundingBox.minX(); x < boundingBox.maxX(); x++) {
                    for (int y = boundingBox.minY(); y < boundingBox.maxY(); y++) {
                        for (int z = boundingBox.minZ(); z < boundingBox.maxZ(); z++) {
                            BlockPos blockPos = new BlockPos(x, y, z);
                            BlockState blockState = serverLevel.getBlockState(blockPos);
                            if (blockState.is(ModBlocks.DARK_OAK_SCAFFOLD.get())) {
                                serverLevel.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 3);
                            } else if (blockState.is(ModBlocks.DARK_OAK_SCAFFOLD_SAND.get())) {
                                serverLevel.setBlock(blockPos, Blocks.SAND.defaultBlockState(), 3);
                            } else {
                                serverLevel.destroyBlock(blockPos, true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public IllagerArmPose getArmPose() {
        return this.isCelebrating() ? IllagerArmPose.CELEBRATING : IllagerArmPose.CROSSED;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_37870_) {
        super.addAdditionalSaveData(p_37870_);
        if (this.getBuildingPos().isPresent()) {
            p_37870_.put("BuildingPos", NbtUtils.writeBlockPos(this.getBuildingPos().get()));
        }
        p_37870_.putInt("BuildCount", this.buildCount);
        p_37870_.putInt("BuildCooldown", this.buildCooldown);

        if (this.buildingStructureName != null) {
            p_37870_.putString("BuildingName", this.buildingStructureName.toString());
        }
        p_37870_.putInt("BuildingStep", this.buildingStep);
        ListTag listTag = new ListTag();

        this.getBuildingBoundingBoxPos().forEach(boundingBox -> {
            DataResult<Tag> var10000 = BoundingBox.CODEC.encodeStart(NbtOps.INSTANCE, boundingBox);
            Logger var10001 = TheModifiger.LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((p_35454_) -> {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put("BoundingBox", p_35454_);
                listTag.add(compoundTag);
            });
        });
        p_37870_.put("BoundingBoxList", listTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_37862_) {
        super.readAdditionalSaveData(p_37862_);
        if (p_37862_.contains("BuildingPos", 10)) {
            this.setBuildingPos(Optional.of(NbtUtils.readBlockPos(p_37862_.getCompound("BuildingPos"))));
        }
        if (p_37862_.contains("BoundingBoxList", 99)) {
            this.getBuildingBoundingBoxPos().clear();
            ListTag listTag = p_37862_.getList("BoundingBoxList", 99);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                DataResult<BoundingBox> dataresult = BoundingBox.CODEC.parse(new Dynamic(NbtOps.INSTANCE, tag.get("BoundingBox")));
                Logger var10001 = TheModifiger.LOGGER;
                Objects.requireNonNull(var10001);
                dataresult.resultOrPartial(var10001::error).ifPresent(this::addBuildingBoundingBox);
            }
        }

        if (p_37862_.contains("BuildingName")) {
            this.setBuildingStructureName(ResourceLocation.tryParse(p_37862_.getString("BuildingName")));
        }
        if (p_37862_.contains("BuildingStep")) {
            this.setBuildingStep(p_37862_.getInt("BuildingStep"));
        }

        if (p_37862_.contains("BuildingCount")) {
            this.setBuildCount(p_37862_.getInt("BuildingCount"));
        }

        if (p_37862_.contains("BuildingCooldown")) {
            this.setBuildCooldown(p_37862_.getInt("BuildingCooldown"));
        }
    }

    @Override
    public boolean canBeLeader() {
        return true;
    }
}
