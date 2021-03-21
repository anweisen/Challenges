package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.TimedChallenge;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BedrockWallChallenge extends SettingModifier {

	public BedrockWallChallenge() {
		super(MenuType.CHALLENGES, 1, 15);
	}

	@EventHandler
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		Location location = event.getFrom();
		if (BlockUtils.isSameBlockIgnoreHeight(event.getTo(), location)) return;

		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
			World world = event.getPlayer().getWorld();

			List<Block> blocks = new ArrayList<>();
			for (int y = 1; y < world.getMaxHeight(); y++) {
				Location blockLocation = location.clone();
				blockLocation.setY(y);
				blocks.add(blockLocation.getBlock());
			}

			Bukkit.getScheduler().runTask(plugin, () -> {
				for (Block block : blocks) {
					block.setType(Material.BEDROCK, false);
				}
			});

		}, getValue() * 60L);

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BEDROCK, Message.forName("item-bedrock-walls-challenge"));
	}

}