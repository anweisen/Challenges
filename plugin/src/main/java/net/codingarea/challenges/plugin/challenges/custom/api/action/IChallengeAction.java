package net.codingarea.challenges.plugin.challenges.custom.api.action;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public interface IChallengeAction {

	public static final Random random = new Random();

	static IChallengeAction SPAWN_RANDOM_MOB = (entity, strings) -> {
		for (Entity target : getTargets(entity, strings, 0)) {
			System.out.println(target.toString());
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

	void execute(Entity entity, String... subActions);

	static List<Entity> getTargets(Entity conditionTarget, String[] subActions, int param) {
		if (param >= subActions.length) return Lists.newArrayList(conditionTarget);

		switch (subActions[param]) {
			case "every_player":
				return Lists.newArrayList(Bukkit.getOnlinePlayers());
			case "current_player":
				return conditionTarget instanceof Player ? Lists.newArrayList(conditionTarget) : Lists.newArrayList();
			case "every_mob":
				ArrayList<Entity> list = Lists.newArrayList();

				for (World world : Bukkit.getWorlds()) {
					list.addAll(world.getEntities());
				}

				return list;
		}
		return Lists.newArrayList(conditionTarget);
	}

}
