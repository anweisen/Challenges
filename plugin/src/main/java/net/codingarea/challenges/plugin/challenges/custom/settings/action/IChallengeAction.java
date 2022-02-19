package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.anweisen.utilities.common.collection.IRandom;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.UncraftItemsChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeAction {

	IRandom random = IRandom.create();

	IChallengeAction SPAWN_RANDOM_MOB = (entity, map) -> {
		for (Entity target : getTargets(entity, map)) {
			for (int i = 0; i < 100; i++) {
				EntityType value = EntityType.values()[random.nextInt(EntityType.values().length)];
				if (value.isSpawnable()) {
					try {
						target.getLocation().getWorld().spawnEntity(target.getLocation(), value);
					} catch (Exception ex) { }
					break;
				}
			}
		}
	};
	TargetEntitiesChallengeAction DAMAGE = (entity, map) -> {
		int damage = Integer.parseInt(map.get("damage")[0]);
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).setNoDamageTicks(0);
			((LivingEntity) entity).damage(damage);
			((LivingEntity) entity).setNoDamageTicks(0);
		}
	};
	TargetEntitiesChallengeAction KILL = (entity, map) -> {
		if (entity instanceof Player) {
			ChallengeHelper.kill(((Player) entity));
		} else if (entity instanceof LivingEntity) {
			((LivingEntity) entity).damage(((LivingEntity) entity).getHealth());
		}
	};
	IChallengeAction RANDOM_ITEM = (entity, map) -> {

		ArrayList<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
		list.removeIf(material -> !material.isItem());

		for (Entity target : getTargets(entity, map)) {
			if (target instanceof Player) {
				Player player = (Player) target;
				InventoryUtils.giveItem(player.getInventory(), player.getLocation(), new ItemStack(random.choose(list)));
			}
		}
	};
	TargetEntitiesChallengeAction UNCRAFT_INVENTORY = (entity, map) -> {
		if (entity instanceof Player) {
			Player player = (Player) entity;
			UncraftItemsChallenge.uncraftInventory(player);
		}
	};
	TargetEntitiesChallengeAction BOOST_IN_AIR = (entity, map) -> {
		if (entity instanceof Player) {
			Player player = (Player) entity;

		}
	};

	void execute(Entity entity, Map<String, String[]> subActions);

	static List<Entity> getTargets(Entity conditionTarget, Map<String, String[]> subActions) {
		if (!subActions.containsKey("target_entity")) {
			return Lists.newLinkedList();
		}
		String targetEntity = subActions.get("target_entity")[0];

		switch (targetEntity) {
			case "random_player":
				List<Player> players = ChallengeAPI.getPlayingPlayers();
				if (players.isEmpty()) return new LinkedList<>();
				return Collections.singletonList(players.get(random.nextInt(players.size())));
			case "every_player":
				return Lists.newLinkedList(ChallengeAPI.getPlayingPlayers());
			case "current_player":
				return conditionTarget instanceof Player ? Lists.newArrayList(conditionTarget) : Lists.newLinkedList();
			case "every_mob":
				List<Entity> everyList = Lists.newLinkedList();
				for (World world : Bukkit.getWorlds()) {
					everyList.addAll(world.getLivingEntities());
				}
				return everyList;
			case "every_mob_except_current":
				List<Entity> exceptList = Lists.newLinkedList();
				for (World world : Bukkit.getWorlds()) {
					exceptList.addAll(world.getLivingEntities());
				}
				exceptList.remove(conditionTarget);
				return exceptList;
			case "every_mob_except_players":
				List<Entity> noPlayers = Lists.newLinkedList();
				for (World world : Bukkit.getWorlds()) {
					for (LivingEntity entity : world.getLivingEntities()) {
						if (entity.getType() == EntityType.PLAYER) continue;
						noPlayers.add(entity);
					}
				}

				return noPlayers;
		}
		if (conditionTarget == null) {
			return Lists.newLinkedList();
		}
		return Lists.newArrayList(conditionTarget);
	}

}
