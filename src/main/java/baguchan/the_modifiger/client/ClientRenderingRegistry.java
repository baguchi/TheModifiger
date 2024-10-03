package baguchan.the_modifiger.client;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.client.model.ModifigerModel;
import baguchan.the_modifiger.client.render.ModifigerRenderer;
import baguchan.the_modifiger.client.render.blockentity.ArrowSpawnerRenderer;
import baguchan.the_modifiger.client.render.blockentity.IllagersSpawnerRenderer;
import baguchan.the_modifiger.registry.ModBlockEntitys;
import baguchan.the_modifiger.registry.ModEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TheModifiger.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRenderingRegistry {
	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntityRegistry.MODIFIGER.get(), ModifigerRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntitys.ILLAGERS_SPAWNER.get(), IllagersSpawnerRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntitys.ARROW_SPAWNER.get(), ArrowSpawnerRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.MODIFIGER, ModifigerModel::createBodyLayer);
	}

}