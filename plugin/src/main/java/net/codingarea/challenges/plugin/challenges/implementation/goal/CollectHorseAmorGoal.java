package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ItemCollectionGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
@Since("2.1.2")
public class CollectHorseAmorGoal extends ItemCollectionGoal {

	public CollectHorseAmorGoal() {
		super(Material.DIAMOND_HORSE_ARMOR, Material.GOLDEN_HORSE_ARMOR, Material.IRON_HORSE_ARMOR, Material.LEATHER_HORSE_ARMOR);
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.DIAMOND_HORSE_ARMOR, Message.forName("item-collect-horse-armor-goal"));
	}

}
