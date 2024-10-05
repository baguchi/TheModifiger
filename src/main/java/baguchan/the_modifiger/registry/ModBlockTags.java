package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> ILLAGER_BLOCK = BlockTags.create(new ResourceLocation(TheModifiger.MODID, "illager_blocks"));
}
