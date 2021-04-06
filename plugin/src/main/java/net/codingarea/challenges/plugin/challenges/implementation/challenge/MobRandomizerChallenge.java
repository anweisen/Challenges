package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.codingarea.challenges.plugin.challenges.type.RandomizerSetting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ListBuilder;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MobRandomizerChallenge extends RandomizerSetting {

	private final Map<EntityType, EntityType> entityRandomizer = new HashMap<>();
	private final Map<EntityType, EntityType> inverseRandomizer = new HashMap<>();

	private boolean inSpawn = false;

	public MobRandomizerChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		if (!shouldExecuteEffect()) return;
		loadAllEntities();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		unLoadAllEntities();
	}

	@TimerTask(status = TimerStatus.PAUSED, async = false)
	public void onPause() {
		unLoadAllEntities();
	}

	private void loadAllEntities() {
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (!entityRandomizer.containsKey(entity.getType())) continue;
				entity.remove();
				entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());

			}

		}
	}

	private void unLoadAllEntities() {
		inSpawn = true;

		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (!inverseRandomizer.containsKey(entity.getType())) continue;
				entity.remove();
				EntityType entityType = inverseRandomizer.get(entity.getType());
				entity.getWorld().spawnEntity(entity.getLocation(), entityType);

			}

		}

		inSpawn = false;
	}


	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.COMMAND_BLOCK_MINECART, Message.forName("item-mob-randomizer-challenge"));
	}

	@Override
	protected void reloadRandomization() {
		List<EntityType> entityTypes = getSpawnAbleEntities();
		List<EntityType> randomEntityTypes = new ArrayList<>(entityTypes);
		Collections.shuffle(randomEntityTypes, random);

		for (int i = 0; i < entityTypes.size(); i++) {
			EntityType type = entityTypes.get(i);
			EntityType randomType = randomEntityTypes.get(i);
			entityRandomizer.put(type, randomType);
			inverseRandomizer.put(randomType, type);
		}

	}

	public List<EntityType> getSpawnAbleEntities() {
		return new ListBuilder<>(EntityType.values())
				.removeIf(type -> !type.isSpawnable())
				.removeIf(type -> !type.isAlive())
				.remove(EntityType.ENDER_DRAGON)
				.remove(EntityType.WITHER)
				.remove(EntityType.GIANT)
				.remove(EntityType.ILLUSIONER)
				.build();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntitySpawn(@Nonnull EntitySpawnEvent event) {
		if (!shouldExecuteEffect()) return;
		if (!entityRandomizer.containsKey(event.getEntityType())) return;
		if (inSpawn) return;

		event.setCancelled(true);

		EntityType type = entityRandomizer.get(event.getEntityType());
		Location location = event.getLocation();
		if (location.getWorld() == null) return;
		if (!maySpawn(type, location.getWorld())) return;
		inSpawn = true;
		event.getEntity().getWorld().spawnEntity(location, type);
		inSpawn = false;
	}

	private boolean maySpawn(@Nonnull EntityType newType, @Nonnull World world) {
		EntityCategory category = getEntityCategory(newType);
		int currentMobCount = getCurrentMobCount(category, world);
		int spawnLimit = getEntityCategory(newType).getSpawnLimit(world);
		return currentMobCount < spawnLimit;
	}

	private int getCurrentMobCount(@Nonnull EntityCategory entityState, @Nonnull World world) {

		int mobCount = 0;

		for (Entity entity : world.getLivingEntities()) {
			if (!(entity instanceof LivingEntity)) continue;
			EntityCategory entityTypeState = getEntityCategory(entity.getType());
			if (entityState == entityTypeState) {
				mobCount++;
			}

		}

		return mobCount;
	}

	private EntityCategory getEntityCategory(@Nonnull EntityType type) {
		Class<? extends Entity> entity = type.getEntityClass();
		if (entity == null) return EntityCategory.OTHER;

		if (Animals.class.isAssignableFrom(entity)) {
			return EntityCategory.ANIMAL;
		}
		if (Drowned.class.isAssignableFrom(entity) || Guardian.class.isAssignableFrom(entity)) {
			return EntityCategory.WATER_AMBIENT;
		}
		if (Monster.class.isAssignableFrom(entity)) {
			return EntityCategory.HOSTILE;
		}
		if (WaterMob.class.isAssignableFrom(entity)) {
			return EntityCategory.WATER_ANIMAL;
		}

		return EntityCategory.AMBIENT;
	}

	public enum EntityCategory {

		HOSTILE,
		AMBIENT,
		ANIMAL,
		WATER_ANIMAL,
		WATER_AMBIENT,
		OTHER;

		private int getSpawnLimit(@Nonnull World world) {

			switch (this) {
				case AMBIENT: return world.getAmbientSpawnLimit();
				case HOSTILE: return world.getMonsterSpawnLimit();
				case ANIMAL: return world.getAnimalSpawnLimit();
				case WATER_ANIMAL: return world.getWaterAnimalSpawnLimit();
				case WATER_AMBIENT: return world.getWaterAmbientSpawnLimit();
			}

			return 0;
		}

	}

}