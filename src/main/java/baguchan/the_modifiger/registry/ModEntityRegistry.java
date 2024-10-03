package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import baguchan.the_modifiger.entity.Modifiger;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TheModifiger.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TheModifiger.MODID);

	public static final RegistryObject<EntityType<Modifiger>> MODIFIGER = ENTITIES_REGISTRY.register("modifiger", () -> EntityType.Builder.of(Modifiger::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(prefix("modifiger")));

	@SubscribeEvent
	public static void registerEntityAttribute(EntityAttributeCreationEvent event) {
		event.put(MODIFIGER.get(), Modifiger.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerEntityAttribute(SpawnPlacementRegisterEvent event) {
		event.register(MODIFIGER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

	private static String prefix(String path) {
		return TheModifiger.MODID + "." + path;
	}
}