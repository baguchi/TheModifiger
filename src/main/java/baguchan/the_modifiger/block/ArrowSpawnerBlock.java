package baguchan.the_modifiger.block;

import baguchan.the_modifiger.blockentity.ArrowSpawnerBlockEntity;
import baguchan.the_modifiger.blockentity.IllagersSpawnerBlockEntity;
import baguchan.the_modifiger.registry.ModBlockEntitys;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ArrowSpawnerBlock extends BaseEntityBlock {
    public ArrowSpawnerBlock(Properties properties) {
        super(properties);
    }

    public void spawnAfterBreak(BlockState p_222477_, ServerLevel p_222478_, BlockPos p_222479_, ItemStack p_222480_, boolean p_222481_) {
        super.spawnAfterBreak(p_222477_, p_222478_, p_222479_, p_222480_, p_222481_);
    }

    public int getExpDrop(BlockState state, LevelReader world, RandomSource randomSource, BlockPos pos, int fortune, int silktouch) {
        return 5 + randomSource.nextInt(5) + randomSource.nextInt(5);
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    public void appendHoverText(ItemStack p_255714_, @javax.annotation.Nullable BlockGetter p_255801_, List<Component> p_255708_, TooltipFlag p_255667_) {
        super.appendHoverText(p_255714_, p_255801_, p_255708_, p_255667_);
        Optional<Component> optional = this.getSpawnEntityDisplayName(p_255714_);
        if (optional.isPresent()) {
            p_255708_.add((Component) optional.get());
        } else {
            p_255708_.add(CommonComponents.EMPTY);
            p_255708_.add(Component.translatable("block.the_modifiger.arrow_spawner.desc1").withStyle(ChatFormatting.GRAY));
            p_255708_.add(CommonComponents.space().append(Component.translatable("block.the_modifiger.arrow_spawner.desc2").withStyle(ChatFormatting.BLUE)));
        }

    }

    private Optional<Component> getSpawnEntityDisplayName(ItemStack p_256057_) {
        CompoundTag compoundtag = BlockItem.getBlockEntityData(p_256057_);
        if (compoundtag != null && compoundtag.contains("SpawnData", 10)) {
            String s = compoundtag.getCompound("SpawnData").getCompound("entity").getString("id");
            ResourceLocation resourcelocation = ResourceLocation.tryParse(s);
            if (resourcelocation != null) {
                return BuiltInRegistries.ENTITY_TYPE.getOptional(resourcelocation).map((p_255782_) -> {
                    return Component.translatable(p_255782_.getDescriptionId()).withStyle(ChatFormatting.GRAY);
                });
            }
        }

        return Optional.empty();
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArrowSpawnerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide() ? createTickerHelper(p_153214_, ModBlockEntitys.ARROW_SPAWNER.get(), ArrowSpawnerBlockEntity::clientTick) : createTickerHelper(p_153214_, ModBlockEntitys.ARROW_SPAWNER.get(), ArrowSpawnerBlockEntity::serverTick);
    }
}
