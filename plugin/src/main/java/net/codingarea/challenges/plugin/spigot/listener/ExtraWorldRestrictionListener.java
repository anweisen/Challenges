package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ExtraWorldRestrictionListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		if (!isInExtraWorld(event.getBlock().getLocation())) return;
		if (Challenges.getInstance().getWorldManager().getSettings().isPlaceBlocks()) return;

		event.setBuild(false);
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		if (!isInExtraWorld(event.getBlock().getLocation())) return;
		if (Challenges.getInstance().getWorldManager().getSettings().isDestroyBlocks()) return;

		event.setCancelled(true);
	}

	private boolean isInExtraWorld(@Nonnull Location location) {
		return Challenges.getInstance().getWorldManager().getExtraWorld().equals(location.getWorld());
	}

}
