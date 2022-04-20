package net.codingarea.challenges.plugin.management.inventory;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.collection.pair.Triple;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.loader.LanguageLoader;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerInventoryManager implements Listener {

	private final boolean stats, control;
	private final boolean enabled;

	public PlayerInventoryManager() {
		Challenges.getInstance().registerListener(this);
		ChallengeAPI.registerScheduler(this);
		ChallengeAPI.subscribeLoader(LanguageLoader.class, () -> Bukkit.getOnlinePlayers().forEach(Challenges.getInstance().getPlayerInventoryManager()::updateInventoryAuto));

		Document config = Challenges.getInstance().getConfigDocument().getDocument("inventory-menu");
		control = config.getBoolean("control");
		stats = config.getBoolean("stats");
		enabled = stats || control;
	}

	public void handleDisable() {
		Bukkit.getOnlinePlayers().forEach(this::removeItems);
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
		removeItems(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onGameModeChange(@Nonnull PlayerGameModeChangeEvent event) {
		updateInventoryGamemode(event.getPlayer(), event.getNewGameMode());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onDrop(@Nonnull PlayerDropItemEvent event) {
		if (ChallengeAPI.isStarted()) return;
		if (!hasItems(event.getPlayer())) return;
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
			case PHYSICAL:
				return;
		}

		if (ChallengeAPI.isStarted()) return;
		if (!hasItems(event.getPlayer())) return;
		Triple<ItemStack, Consumer<Player>, String>[] pairs = createItemPairs(event.getPlayer());

		int slot = event.getPlayer().getInventory().getHeldItemSlot();
		if (slot >= pairs.length) return;
		Triple<ItemStack, Consumer<Player>, String> pair = pairs[slot];
		if (pair == null) return;
		if (pair.getThird() != null && !event.getPlayer().hasPermission(pair.getThird())) return;

		Consumer<Player> action = pair.getSecond();
		if (action == null) return;

		action.accept(event.getPlayer());
		SoundSample.PLOP.play(event.getPlayer());
	}

	@TimerTask(status = {TimerStatus.PAUSED, TimerStatus.RUNNING})
	public void updateInventories() {
		Bukkit.getOnlinePlayers().forEach(this::updateInventoryAuto);
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
		if (Bukkit.isPrimaryThread()) {
			Challenges.getInstance().runAsync(() -> updateInventory(player, gamemode, join, alive));
			return;
		}

		try {
			if (!LanguageLoader.isLoaded()) return;
			if (ChallengeAPI.isPaused()) {
				updateInventoryPaused(player, gamemode, join, alive);
			} else {
				updateInventoryStarted(player, gamemode, join, alive);
			}
		} catch (Exception ex) {
			Logger.error("Failed to update inventory", ex);
		}
	}

	private void updateInventoryStarted(@Nonnull Player player, @Nonnull GameMode gamemode, boolean join, boolean alive) {
		removeItems(player);
	}

	private void updateInventoryPaused(@Nonnull Player player, @Nonnull GameMode gamemode, boolean join, boolean alive) {
		if (gamemode == GameMode.CREATIVE || gamemode == GameMode.SPECTATOR || !enabled) {
			removeItems(player);
			return;
		}
		if (canGiveItems(player)) {
			if (!alive) return;
			giveItems(player);
			if (join) player.getInventory().setHeldItemSlot(4);
		}
	}

	private boolean hasItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, String>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, String> pair = pairs[i];
			ItemStack expected = pair == null ? null : pair.getFirst();
			ItemStack found = player.getInventory().getItem(i);
			if (pair != null && pair.getThird() != null && !player.hasPermission(pair.getThird()))
				continue;
			if (expected != null && found == null) return false;
			if (expected == null) continue;
			if (expected.getType() != found.getType()) return false;
		}
		return true;
	}

	private boolean canGiveItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, String>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, String> pair = pairs[i];
			ItemStack expected = pair == null ? null : pair.getFirst();
			ItemStack found = player.getInventory().getItem(i);
			if (expected == null && found != null) return false;
			if (expected == null) continue;
			if (found == null) continue;
			if (expected.getType() != found.getType()) return false;
		}
		return true;
	}

	private void removeItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, String>[] pairs = createItemPairs(player);
		for (Triple<ItemStack, Consumer<Player>, String> pair : pairs) {
			if (pair == null) continue;
			ItemStack item = pair.getFirst();
			if (item == null) continue;
			if (item.getItemMeta() == null) continue;
			ItemStack[] content = player.getInventory().getContents();
			for (int i = 0; i < content.length; i++) {
				ItemStack current = content[i];
				if (current == null) continue;
				if (current.getType() != item.getType()) continue;
				if (current.getItemMeta() == null) continue;
				if (!current.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))
					continue;
				player.getInventory().setItem(i, null);
			}
		}
	}

	private void giveItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, String>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, String> pair = pairs[i];
			if (pair == null) continue;
			if (pair.getThird() != null && !player.hasPermission(pair.getThird())) continue;
			player.getInventory().setItem(i, pair.getFirst());
		}
	}

	@Nonnull
	private Triple<ItemStack, Consumer<Player>, String>[] createItemPairs(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, String>[] pairs = new Triple[9];

		if (control) {
			pairs[3] = new Triple<>(
					new ItemBuilder(Material.CLOCK, Message.forName("item-menu-timer").asString()).build(),
					p -> p.performCommand("timer"),
					"challenges.timer"
			);
			pairs[4] = new Triple<>(
					new ItemBuilder(Material.BOOK, Message.forName("item-menu-challenges").asString()).build(),
					p -> p.performCommand("challenges"),
					"challenges.gui"
			);
			pairs[5] = new Triple<>(
					new ItemBuilder(Material.LIME_DYE, Message.forName("item-menu-start").asString()).build(),
					p -> p.performCommand("start"),
					"challenges.timer"
			);
		}
		if (stats) {
			pairs[0] = new Triple<>(
					new ItemBuilder(Material.GOLD_INGOT, Message.forName("item-menu-leaderboard").asString()).build(),
					p -> p.performCommand("leaderboard"),
					null
			);
			pairs[8] = new Triple<>(
					new SkullBuilder(player.getUniqueId(), player.getName(), Message.forName("item-menu-stats").asString()).build(),
					p -> player.performCommand("stats"),
					null
			);
		}

		return pairs;
	}

}
