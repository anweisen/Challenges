package net.codingarea.challenges.plugin.management.inventory;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.lang.loader.LanguageLoader;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerInventoryManager implements Listener {

	private static final List<Player> hasItems = new ArrayList<>();
	private ItemStack[] items;
	private Consumer<Player>[] actions;

	private final boolean enabled;

	public PlayerInventoryManager() {
		enabled = Challenges.getInstance().getConfigDocument().getBoolean("inventory-menu");
	}

	public void enable() {
		Challenges.getInstance().registerListener(this);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCommandsUpdate(@Nonnull PlayerCommandSendEvent event) {
		updateInventoryAuto(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(@Nonnull PlayerJoinEvent event) {
		updateInventoryJoin(event.getPlayer(), true);
	}

	@EventHandler
	public void onQuit(@Nonnull PlayerQuitEvent event) {
		if (items == null) return;
		if (hasItems.contains(event.getPlayer()) && hasNoOtherItems(event.getPlayer().getInventory())) {
			remove(event.getPlayer());
		} else {
			hasItems.remove(event.getPlayer());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameModeChange(@Nonnull PlayerGameModeChangeEvent event) {
		updateInventoryGamemode(event.getPlayer(), event.getNewGameMode());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDrop(@Nonnull PlayerDropItemEvent event) {
		if (!hasItems.contains(event.getPlayer())) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRespawn(@Nonnull PlayerRespawnEvent event) {
		updateInventoryAlive(event.getPlayer(), true);
	}

	@EventHandler
	public void onInteract(@Nonnull PlayerInteractEvent event) {
		switch (event.getAction()) {
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				return;
		}

		if (!hasItems.contains(event.getPlayer())) return;
		int slot = event.getPlayer().getInventory().getHeldItemSlot();
		if (slot >= actions.length) return;
		Consumer<Player> action = actions[slot];
		if (action == null) return;

		action.accept(event.getPlayer());
		SoundSample.PLOP.play(event.getPlayer());
	}

	public void updateInventoryAuto(@Nonnull Player player) {
		updateInventory(player, player.getGameMode(), false, !player.isDead());
	}

	public void updateInventoryAlive(@Nonnull Player player, boolean alive) {
		updateInventory(player, player.getGameMode(), false, alive);
	}

	public void updateInventoryGamemode(@Nonnull Player player, @Nonnull GameMode gamemode) {
		updateInventory(player, gamemode, false, !player.isDead());
	}

	public void updateInventoryJoin(@Nonnull Player player, boolean join) {
		updateInventory(player, player.getGameMode(), join, !player.isDead());
	}

	public void updateInventory(@Nonnull Player player, @Nonnull GameMode gamemode, boolean join, boolean alive) {
		if (!LanguageLoader.isLoaded()) return;
		if (items == null) createItems();

		if (ChallengeAPI.isPaused()) {
			updateInventoryPaused(player, gamemode, join, alive);
		} else {
			updateInventoryStarted(player, gamemode, join, alive);
		}
	}

	private void updateInventoryStarted(@Nonnull Player player, @Nonnull GameMode gamemode, boolean join, boolean alive) {
		if (hasNoOtherItems(player.getInventory())) {
			remove(player);
		}
	}

	private void updateInventoryPaused(@Nonnull Player player, @Nonnull GameMode gamemode, boolean join, boolean alive) {
		if (hasNoOtherItems(player.getInventory())) {
			if (gamemode == GameMode.CREATIVE || gamemode == GameMode.SPECTATOR || !player.hasPermission("challenges.gui") || !enabled) {
				remove(player);
				return;
			}

			if (!alive) return;
			for (int i = 0; i < items.length; i++) {
				player.getInventory().setItem(i, items[i]);
			}
			if (join) player.getInventory().setHeldItemSlot(4);
			if (!hasItems.contains(player))
				hasItems.add(player);
		}
	}

	private boolean hasNoOtherItems(@Nonnull Inventory inventory) {
		for (int i = 0; i < items.length && i < inventory.getSize(); i++) {
			ItemStack expected = items[i];
			ItemStack found = inventory.getItem(i);
			if (found == null)                          continue;
			if (expected == null)                       return false;
			if (expected.getType() != found.getType())  return false;
		}
		return true;
	}

	private void remove(@Nonnull Player player) {
		for (int i = 0; i < items.length; i++) {
			player.getInventory().setItem(i, null);
		}
		hasItems.remove(player);
	}

	private void createItems() {
		items       = new ItemStack[9];
		actions     = new Consumer[9];
		items[3]    = new ItemBuilder(Material.CLOCK, Message.forName("item-menu-timer").asString()).build();
		actions[3]  = player -> player.performCommand("timer");
		items[4]    = new ItemBuilder(Material.BOOK, Message.forName("item-menu-challenges").asString()).build();
		actions[4]  = player -> player.performCommand("challenge");
		items[5]    = new ItemBuilder(Material.LIME_DYE, Message.forName("item-menu-start").asString()).build();
		actions[5]  = player -> player.performCommand("start");
	}

}
