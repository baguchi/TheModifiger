package baguchan.the_modifiger.blockentity;

import baguchan.the_modifiger.registry.ModBlockEntitys;
import baguchan.the_modifiger.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ArrowSpawnerBlockEntity extends BlockEntity {
    protected final ArrowSpawner spawner = new ArrowSpawner() {
        public void broadcastEvent(Level p_155767_, BlockPos p_155768_, int p_155769_) {
            p_155767_.blockEvent(p_155768_, ModBlocks.ARROW_SPAWNER.get(), p_155769_, 0);
        }

        public void clientTick(Level p_151320_, BlockPos p_151321_) {
            RandomSource randomsource = p_151320_.getRandom();
            double d0 = (double)p_151321_.getX() + randomsource.nextDouble();
            double d1 = (double)p_151321_.getY() + randomsource.nextDouble();
            double d2 = (double)p_151321_.getZ() + randomsource.nextDouble();
            p_151320_.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
            p_151320_.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
        }

        public @org.jetbrains.annotations.Nullable BlockEntity getSpawnerBlockEntity() {
            return ArrowSpawnerBlockEntity.this;
        }
    };

    public ArrowSpawnerBlockEntity(BlockPos p_155752_, BlockState p_155753_) {
        super(ModBlockEntitys.ARROW_SPAWNER.get(), p_155752_, p_155753_);
    }

    public void load(CompoundTag p_155760_) {
        super.load(p_155760_);
        this.spawner.load(this.level, this.worldPosition, p_155760_);
    }

    protected void saveAdditional(CompoundTag p_187521_) {
        super.saveAdditional(p_187521_);
        this.spawner.save(p_187521_);
    }

    public static void clientTick(Level p_155755_, BlockPos p_155756_, BlockState p_155757_, ArrowSpawnerBlockEntity p_155758_) {
        p_155758_.spawner.clientTick(p_155755_, p_155756_);
    }

    public static void serverTick(Level p_155762_, BlockPos p_155763_, BlockState p_155764_, ArrowSpawnerBlockEntity p_155765_) {
        p_155765_.spawner.serverTick((ServerLevel)p_155762_, p_155763_);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag compoundtag = this.saveWithoutMetadata();
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    public boolean triggerEvent(int p_59797_, int p_59798_) {
        return this.spawner.onEventTriggered(this.level, p_59797_) ? true : super.triggerEvent(p_59797_, p_59798_);
    }

    public boolean onlyOpCanSetNbt() {
        return true;
    }

    public void setEntityId(EntityType<?> p_254530_, RandomSource p_253719_) {
        this.spawner.setEntityId(p_254530_, this.level, p_253719_, this.worldPosition);
    }

    public ArrowSpawner getSpawner() {
        return this.spawner;
    }
}
