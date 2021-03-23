package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class SurfaceHoleChallenge extends SettingModifier {

	public SurfaceHoleChallenge() {
		super(MenuType.CHALLENGES, 1, 15);
	}

	@EventHandler
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;

		Location location = event.getFrom();
		if (BlockUtils.isSameBlockIgnoreHeight(event.getTo(), location)) return;

		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
			if (!shouldExecuteEffect()) return;

			World world = event.getPlayer().getWorld();

			List<Block> blocks = new ArrayList<>();
			for (int y = 0; y < world.getMaxHeight(); y++) {
				Location blockLocation = location.clone();
				blockLocation.setY(y);
				blocks.add(blockLocation.getBlock());
			}

			Bukkit.getScheduler().runTask(plugin, () -> {
				for (Block block : blocks) {
					block.setType(Material.AIR, false);
				}
			});

		}, getValue() * 20L);

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BARRIER, Message.forName("item-surface-hole-challenge"));
	}

}