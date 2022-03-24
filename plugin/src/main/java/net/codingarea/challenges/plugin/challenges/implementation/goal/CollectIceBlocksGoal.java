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
public class CollectIceBlocksGoal extends ItemCollectionGoal {

	public CollectIceBlocksGoal() {
		super(Material.ICE, Material.BLUE_ICE, Material.PACKED_ICE, Material.SNOW_BLOCK);
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.PACKED_ICE, Message.forName("item-collect-ice-goal"));
	}

}
