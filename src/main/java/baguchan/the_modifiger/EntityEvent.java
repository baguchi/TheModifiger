package baguchan.the_modifiger;

import baguchan.the_modifiger.blockentity.ArrowSpawnerBlockEntity;
import baguchan.the_modifiger.blockentity.IllagersSpawnerBlockEntity;
import baguchan.the_modifiger.entity.Modifiger;
import baguchan.the_modifiger.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheModifiger.MODID)
public class EntityEvent {

	@SubscribeEvent
	public static void addSpawnner(PlayerInteractEvent.RightClickBlock event) {
		ItemStack itemstack = event.getItemStack();
		Player player = event.getEntity();
		BlockPos blockpos = event.getPos();
		Level level = event.getLevel();
		if(level.getBlockState(blockpos).is(ModBlocks.ILLAGERS_SPAWNER.get())) {
			if (itemstack.getItem() instanceof SpawnEggItem spawnEggItem) {
				BlockEntity blockentity = level.getBlockEntity(blockpos);
				BlockState blockState = level.getBlockState(blockpos);
				if (blockentity instanceof IllagersSpawnerBlockEntity) {
					IllagersSpawnerBlockEntity spawnerblockentity = (IllagersSpawnerBlockEntity) blockentity;
					EntityType<?> entitytype1 = spawnEggItem.getType(itemstack.getTag());
					spawnerblockentity.setEntityId(entitytype1, level.getRandom());
					blockentity.setChanged();
					level.sendBlockUpdated(blockpos, blockState, blockState, 3);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
					itemstack.shrink(1);

					event.setCanceled(true);
				}
			}
		}

		if(level.getBlockState(blockpos).is(ModBlocks.ARROW_SPAWNER.get())) {
			if (itemstack.getItem() instanceof ArrowItem spawnEggItem) {
				BlockEntity blockentity = level.getBlockEntity(blockpos);
				BlockState blockState = level.getBlockState(blockpos);
				if (blockentity instanceof ArrowSpawnerBlockEntity) {
					ArrowSpawnerBlockEntity spawnerblockentity = (ArrowSpawnerBlockEntity) blockentity;
					EntityType<?> entitytype1 = spawnEggItem.createArrow(level, itemstack, player).getType();
					spawnerblockentity.setEntityId(entitytype1, level.getRandom());
					blockentity.setChanged();
					level.sendBlockUpdated(blockpos, blockState, blockState, 3);
					level.gameEvent(player, GameEvent.BLOCK_CHANGE, blockpos);
					itemstack.shrink(1);

					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void addSpawn(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Villager) {
			Villager abstractVillager = (Villager) event.getEntity();

			abstractVillager.goalSelector.addGoal(1, new AvoidEntityGoal(abstractVillager, Modifiger.class, 16.0F, 0.75F, 0.8F));
		}

		if (event.getEntity() instanceof WanderingTrader) {
			WanderingTrader wanderingTraderEntity = (WanderingTrader) event.getEntity();

			wanderingTraderEntity.goalSelector.addGoal(1, new AvoidEntityGoal((PathfinderMob) wanderingTraderEntity, Modifiger.class, 16.0F, 0.8F, 0.85F));
		}
	}
}