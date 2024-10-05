package baguchan.the_modifiger;

import baguchan.the_modifiger.registry.ModBlockEntitys;
import baguchan.the_modifiger.registry.ModBlocks;
import baguchan.the_modifiger.registry.ModEntityRegistry;
import baguchan.the_modifiger.registry.ModItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.raid.Raid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TheModifiger.MODID)
public class TheModifiger {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "the_modifiger";
	public static final Logger LOGGER = LogUtils.getLogger();

	public TheModifiger() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(modEventBus);
		ModItemRegistry.ITEM_REGISTRY.register(modEventBus);
		ModBlockEntitys.BLOCK_ENTITIES.register(modEventBus);
		ModEntityRegistry.ENTITIES_REGISTRY.register(modEventBus);
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

    private void commonSetup(final FMLCommonSetupEvent event)
    {
		Raid.RaiderType.create("modifiger", ModEntityRegistry.MODIFIGER.get(), new int[]{0, 0, 0, 1, 1, 2, 2, 2});
    }
}
