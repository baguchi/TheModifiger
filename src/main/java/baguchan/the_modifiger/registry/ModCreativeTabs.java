package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = TheModifiger.MODID)
public class ModCreativeTabs {
	@SubscribeEvent
	public static void addCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModItemRegistry.MODIFIGER_SPAWNEGG.get());
		}

		if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			event.accept(ModBlocks.ARROW_SPAWNER.get());
			event.accept(ModBlocks.ILLAGERS_SPAWNER.get());
		}
	}
}