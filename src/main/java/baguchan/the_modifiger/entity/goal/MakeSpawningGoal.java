package baguchan.the_modifiger.entity.goal;

import baguchan.the_modifiger.blockentity.IllagersSpawnerBlockEntity;
import baguchan.the_modifiger.entity.Modifiger;
import baguchan.the_modifiger.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

import static net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.processBlockInfos;

public class MakeSpawningGoal extends MoveToBlockGoal {
    private final Modifiger mob;
    private int spawnedCount;
    private boolean canRaid = true;

    public MakeSpawningGoal(Modifiger p_25919_, float speedMultiplier) {
        super(p_25919_, speedMultiplier, 16);

        this.mob = p_25919_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canContinueToUse() {
        return this.canRaid && super.canContinueToUse();
    }


    public void tick() {
        super.tick();
        this.mob.getLookControl().setLookAt((double)this.blockPos.getX() + 0.5, (double)(this.blockPos.getY() + 1), (double)this.blockPos.getZ() + 0.5, 10.0F, (float)this.mob.getMaxHeadXRot());
        if (this.isReachedTarget()) {
            Level level = this.mob.level();
            BlockPos blockpos = this.blockPos.above();
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            if (this.canRaid && this.spawnedCount < 60) {
                BlockEntity blockEntity = this.mob.level().getBlockEntity(this.blockPos);
                if(blockEntity instanceof IllagersSpawnerBlockEntity blockEntity1 && this.mob.level() instanceof ServerLevel serverLevel){
                    blockEntity1.getSpawner().serverTick(serverLevel, this.blockPos);
                }
                this.spawnedCount+= 1;
            }else {
                this.nextStartTick = 300 + level.random.nextInt(150);
                this.canRaid = false;
            }

        }

    }

    @Override
    public void start() {
        super.start();
        canRaid = true;
    }

    @Override
    public void stop() {
        super.stop();
        this.spawnedCount = 0;
    }

    @Override
    protected boolean isValidTarget(LevelReader p_29785_, BlockPos p_29786_) {
        BlockState blockstate = p_29785_.getBlockState(p_29786_);
        if (blockstate.is(ModBlocks.ILLAGERS_SPAWNER.get())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

}