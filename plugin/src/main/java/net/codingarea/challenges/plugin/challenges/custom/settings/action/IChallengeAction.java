package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public interface IChallengeAction {

	Random random = new Random();

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
	IChallengeAction DAMAGE = (entity, map) -> {
		System.out.println("Damage");
		for (Entity target : getTargets(entity, map)) {
			int damage = Integer.parseInt(map.get("damage"));
			if (target instanceof LivingEntity) {
				((LivingEntity) target).setNoDamageTicks(0);
				((LivingEntity) target).damage(damage);
				((LivingEntity) target).setNoDamageTicks(0);
			}
		}
	};
	IChallengeAction KILL = (entity, map) -> {
		for (Entity target : getTargets(entity, map)) {
			if (target instanceof Player) {
				ChallengeHelper.kill(((Player) target));
			} else if (target instanceof LivingEntity) {
				((LivingEntity) target).damage(((LivingEntity) target).getHealth());
			}
		}
	};
	IChallengeAction RANDOM_ITEM = (entity, map) -> {
		for (Entity target : getTargets(entity, map)) {
			if (target instanceof Player) {

			}
		}
	};

	void execute(Entity entity, Map<String, String> subActions);

	static List<Entity> getTargets(Entity conditionTarget, Map<String, String> subActions) {

		if (!subActions.containsKey("target_entity")) {
			return Lists.newLinkedList();
		}
		String targetEntity = subActions.get("target_entity");

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
