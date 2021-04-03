package net.codingarea.challenges.plugin.management.inventory;

import net.anweisen.utilities.commons.common.Triple;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.loader.LanguageLoader;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
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

	protected static final String permission = "challenges.gui";

	private final boolean stats, control;
	private final boolean enabled;

	public PlayerInventoryManager() {
		Challenges.getInstance().registerListener(this);
		ChallengeAPI.subscribeLoader(LanguageLoader.class, () -> Bukkit.getOnlinePlayers().forEach(Challenges.getInstance().getPlayerInventoryManager()::updateInventoryAuto));

		Document config = Challenges.getInstance().getConfigDocument().getDocument("inventory-menu");
		control = config.getBoolean("control");
		stats = config.getBoolean("stats");
		enabled = stats || control;
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

		if (!hasItems(event.getPlayer())) return;
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = createItemPairs(event.getPlayer());

		int slot = event.getPlayer().getInventory().getHeldItemSlot();
		if (slot >= pairs.length) return;
		Triple<ItemStack, Consumer<Player>, Boolean> pair = pairs[slot];
		if (pair == null) return;
		if (pair.getThird() && !event.getPlayer().hasPermission(permission)) return;

		Consumer<Player> action = pair.getSecond();
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
		if (gamemode == GameMode.CREATIVE || gamemode == GameMode.SPECTATOR || !player.hasPermission("challenges.gui") || !enabled) {
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
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, Boolean> pair = pairs[i];
			ItemStack expected = pair == null ? null : pair.getFirst();
			ItemStack found = player.getInventory().getItem(i);
			if (expected != null && found == null) return false;
			if (expected == null) continue;
			if (found == null) continue;
			if (expected.getType() != found.getType()) return false;
		}
		return true;
	}

	private boolean canGiveItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, Boolean> pair = pairs[i];
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
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = createItemPairs(player);
		for (Triple<ItemStack, Consumer<Player>, Boolean> pair : pairs) {
			if (pair == null) continue;
			ItemStack item = pair.getFirst();
			if (item == null) continue;
			player.getInventory().remove(item);
		}
	}

	private void giveItems(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = createItemPairs(player);
		for (int i = 0; i < pairs.length; i++) {
			Triple<ItemStack, Consumer<Player>, Boolean> pair = pairs[i];
			if (pair == null) continue;
			if (pair.getThird() && !player.hasPermission(permission)) continue;
			player.getInventory().setItem(i, pair.getFirst());
		}
	}

	@Nonnull
	private Triple<ItemStack, Consumer<Player>, Boolean>[] createItemPairs(@Nonnull Player player) {
		Triple<ItemStack, Consumer<Player>, Boolean>[] pairs = new Triple[9];

		if (control) {
			pairs[3] = new Triple<>(
					new ItemBuilder(Material.CLOCK, Message.forName("item-menu-timer").asString()).build(),
					p -> p.performCommand("timer"),
					true
			);
			pairs[4] = new Triple<>(
					new ItemBuilder(Material.BOOK, Message.forName("item-menu-challenges").asString()).build(),
					p -> p.performCommand("challenges"),
					true
			);
			pairs[5] = new Triple<>(
					new ItemBuilder(Material.LIME_DYE, Message.forName("item-menu-start").asString()).build(),
					p -> p.performCommand("start"),
					true
			);
		}
		if (stats) {
			pairs[0] = new Triple<>(
					new ItemBuilder(Material.GOLD_INGOT, Message.forName("item-menu-leaderboard").asString()).build(),
					p -> p.performCommand("leaderboard"),
					false
			);
			pairs[8] = new Triple<>(
					new SkullBuilder(player.getUniqueId(), player.getName(), Message.forName("item-menu-stats").asString()).build(),
					p -> player.performCommand("stats"),
					false
			);
		}

		return pairs;
	}

}
