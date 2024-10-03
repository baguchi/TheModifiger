package baguchan.the_modifiger.registry;

import baguchan.the_modifiger.TheModifiger;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemRegistry {
	public static final DeferredRegister<Item> ITEM_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, TheModifiger.MODID);

	public static final RegistryObject<Item> MODIFIGER_SPAWNEGG = ITEM_REGISTRY.register("modifiger_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityRegistry.MODIFIGER, 0x959B9B, 0x7341BC, (new Item.Properties())));

}