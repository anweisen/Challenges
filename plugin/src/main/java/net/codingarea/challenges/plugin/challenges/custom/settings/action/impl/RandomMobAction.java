package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.action.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.EntityTargetAction;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class RandomMobAction extends EntityTargetAction {

	private static final EntityType[] spannableMobs;
	private static final EntityType[] livingMobs;

	static {
		List<EntityType> list = new LinkedList<>(Arrays.asList(EntityType.values()));
		list = list.stream().filter(EntityType::isSpawnable).collect(Collectors.toList());
		spannableMobs = list.toArray(new EntityType[0]);
		list = list.stream().filter(EntityType::isAlive).collect(Collectors.toList());
		livingMobs = list.toArray(new EntityType[0]);
	}

	public RandomMobAction(String name) {
		super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false));
	}

	public static EntityType[] getSpawnableMobs() {
		return spannableMobs;
	}

	public static EntityType[] getLivingMobs() {
		return livingMobs;
	}

	@Override
	public void executeFor(Entity entity, Map<String, String[]> subActions) {
		if (entity.getLocation().getWorld() == null) return;
		EntityType value = ChallengeAction.random.choose(spannableMobs);
		entity.getLocation().getWorld().spawnEntity(entity.getLocation(), value);
	}

	@Override
	public Material getMaterial() {
		return Material.BLAZE_SPAWN_EGG;
	}

}
