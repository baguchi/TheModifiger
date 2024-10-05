package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.block.ArrowSpawnerBlock;
import baguchan.the_modifiger.block.InstantSpawnerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TheModifiger.MODID);

    public static final RegistryObject<Block> ILLAGERS_SPAWNER = register("illagers_spawner", () -> new InstantSpawnerBlock(BlockBehaviour.Properties.of().strength(3.5F, 5.0F).noOcclusion().isSuffocating((state, blockGetter, blockPos) -> false).requiresCorrectToolForDrops().noLootTable().sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> ARROW_SPAWNER = register("arrow_spawner", () -> new ArrowSpawnerBlock(BlockBehaviour.Properties.of().strength(3.5F, 5.0F).noOcclusion().isSuffocating((state, blockGetter, blockPos) -> false).requiresCorrectToolForDrops().noLootTable().sound(SoundType.NETHERITE_BLOCK)));
    public static final RegistryObject<Block> DARK_OAK_SCAFFOLD = register("dark_oak_scaffolding", () -> new ArrowSpawnerBlock(BlockBehaviour.Properties.of().strength(0.75F, 1.0F).requiresCorrectToolForDrops().noLootTable().sound(SoundType.SCAFFOLDING).dropsLike(Blocks.DIRT)));
    public static final RegistryObject<Block> DARK_OAK_SCAFFOLD_SAND = register("dark_oak_scaffolding_sand", () -> new ArrowSpawnerBlock(BlockBehaviour.Properties.of().strength(0.75F, 1.0F).requiresCorrectToolForDrops().noLootTable().sound(SoundType.SCAFFOLDING).dropsLike(Blocks.SAND)));
    private static <T extends Block> RegistryObject<T> baseRegister(String name, Supplier<? extends T> block, Function<RegistryObject<T>, Supplier<? extends Item>> item) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        ModItemRegistry.ITEM_REGISTRY.register(name, item.apply(register));
        return register;
    }

    private static <T extends Block> RegistryObject<T> noItemRegister(String name, Supplier<? extends T> block) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        return register;
    }

    private static <B extends Block> RegistryObject<B> register(String name, Supplier<? extends Block> block) {
        return (RegistryObject<B>) baseRegister(name, block, (object) -> ModBlocks.registerBlockItem(object));
    }

    private static <T extends Block> Supplier<BlockItem> registerBlockItem(final RegistryObject<T> block) {
        return () -> {
            return new BlockItem(Objects.requireNonNull(block.get()), new Item.Properties());
        };
    }
}