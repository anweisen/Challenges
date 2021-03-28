package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class PlayerJumpListener implements Listener {

	/**
	 * Detecting jumps and calls {@link PlayerJumpEvent}
	 * @param event the event which is called
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerStatisticIncrement(@Nonnull PlayerStatisticIncrementEvent event) {
		if (event.getStatistic() == Statistic.JUMP) {
			Bukkit.getPluginManager().callEvent(new PlayerJumpEvent(event.getPlayer()));
		}
	}

}