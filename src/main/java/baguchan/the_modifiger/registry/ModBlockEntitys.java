package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.blockentity.ArrowSpawnerBlockEntity;
import baguchan.the_modifiger.blockentity.IllagersSpawnerBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlockEntitys {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TheModifiger.MODID);

    public static final Supplier<BlockEntityType<IllagersSpawnerBlockEntity>> ILLAGERS_SPAWNER = BLOCK_ENTITIES.register("illagers_spawner", () -> register(TheModifiger.MODID + ":illagers_spawner", BlockEntityType.Builder.of(IllagersSpawnerBlockEntity::new, ModBlocks.ILLAGERS_SPAWNER.get())));
    public static final Supplier<BlockEntityType<ArrowSpawnerBlockEntity>> ARROW_SPAWNER = BLOCK_ENTITIES.register("arrow_spawner", () -> register(TheModifiger.MODID + ":arrow_spawner", BlockEntityType.Builder.of(ArrowSpawnerBlockEntity::new, ModBlocks.ARROW_SPAWNER.get())));

    private static <T extends BlockEntity> BlockEntityType<T> register(String p_200966_0_, BlockEntityType.Builder<T> p_200966_1_) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, p_200966_0_);
        return p_200966_1_.build(type);
    }
}