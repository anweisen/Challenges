package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class TheFloorIsLavaChallenge extends AdvancedChallenge implements Listener {

	public TheFloorIsLavaChallenge() {
		menu = MenuType.CHALLENGES;
		this.value = 5;
		maxValue = 25;
		nextActionInSeconds = -1;
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.MAGMA_BLOCK, ItemTranslation.THE_FLOOR_IS_LAVA).build();
	}

	@Override
	public void onValueChange(ChallengeEditEvent event) {
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		this.value = 5;
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
	}

	@Override
	public void onTimeActivation() {
	}

	private void addBlock(Location location) {

		while (location.getBlock().isPassable() && location.getBlock().getY() > 0) {
			location.subtract(0, 1, 0);
		}

		final Block block = location.getBlock();
		if (block.getType() == Material.END_PORTAL || block.getType() == Material.NETHER_PORTAL) return;

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {

			if (block.getType() == Material.LAVA) return;

			block.setType(Material.MAGMA_BLOCK);

		}, 20*value);

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			block.setType(Material.LAVA);
		}, 20*value*2);

	}

	@EventHandler
	public void onPlayerChangeBlock(PlayerMoveEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;

		addBlock(event.getTo().clone());

	}
}
