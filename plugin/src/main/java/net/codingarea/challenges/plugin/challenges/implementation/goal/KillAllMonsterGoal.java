package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.KillMobsGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.3
 */
@Since("2.1.3")
public class KillAllMonsterGoal extends KillMobsGoal {

	public KillAllMonsterGoal() {
		super(getAllMobsToKill());
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ARROW, Message.forName("item-all-monster-goal"));
	}

	@Override
	public Message getBossbarMessage() {
		return Message.forName("bossbar-kill-all-monster");
	}

	static List<EntityType> getAllMobsToKill() {
		LinkedList<EntityType> list = new LinkedList<>(Arrays.asList(EntityType.values()));
		list.removeIf(type -> !type.isAlive());
		list.removeIf(type -> !Monster.class.isAssignableFrom(type.getEntityClass()));
		list.add(EntityType.PHANTOM);
		list.add(EntityType.ENDER_DRAGON);
		list.add(EntityType.SHULKER);
		list.add(EntityType.GHAST);
		list.add(EntityType.HOGLIN);
		list.add(EntityType.MAGMA_CUBE);
		list.add(EntityType.SLIME);
		list.remove(EntityType.GIANT);
		list.remove(EntityType.ILLUSIONER);
		return list;
	}

}
