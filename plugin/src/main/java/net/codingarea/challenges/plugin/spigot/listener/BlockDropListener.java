package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class BlockDropListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Material block = event.getBlock().getType();
		List<Material> drops = Challenges.getInstance().getBlockDropManager().getCustomDrops(block);
		if (drops == null) return;

		Location location = event.getBlock().getLocation().clone().add(0.5, 0, 0.5);
		if (!location.isWorldLoaded()) return;

		event.setDropItems(false);
		for (Material drop : drops) {
			location.getWorld().dropItem(location, new ItemStack(drop));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockExplosion(@Nonnull BlockExplodeEvent event) {
		for (Block block : event.blockList()) {

			Material material = block.getType();
			if (material.isAir()) continue;

			List<Material> drops = Challenges.getInstance().getBlockDropManager().getCustomDrops(material);
			if (drops == null) return;

			Location location = block.getLocation().clone().add(0.5, 0, 0.5);
			if (!location.isWorldLoaded()) continue;

			event.setYield(0);
			for (Material drop : drops) {
				location.getWorld().dropItem(location, new ItemStack(drop));
			}

		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplosion(@Nonnull EntityExplodeEvent event) {
		for (Block block : event.blockList()) {

			Material material = block.getType();
			if (material.isAir()) continue;

			List<Material> drops = Challenges.getInstance().getBlockDropManager().getCustomDrops(material);
			if (drops == null) return;

			Location location = block.getLocation().clone().add(0.5, 0, 0.5);
			if (!location.isWorldLoaded()) continue;

			event.setYield(0);
			for (Material drop : drops) {
				location.getWorld().dropItem(location, new ItemStack(drop));
			}

		}
	}

}
