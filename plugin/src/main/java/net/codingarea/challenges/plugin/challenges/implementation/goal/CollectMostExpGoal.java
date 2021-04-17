package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.PointsGoal;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class CollectMostExpGoal extends PointsGoal {

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.EXPERIENCE_BOTTLE, Message.forName("item-most-xp-goal"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpChange(@Nonnull PlayerExpChangeEvent event) {
		if (!shouldExecuteEffect()) return;
		collect(event.getPlayer(), event.getAmount());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		if (!shouldExecuteEffect()) return;
		event.setKeepLevel(true);
	}

}
