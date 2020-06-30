package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.settings.TimberSetting.LogType;
import net.codingarea.challengesplugin.challengetypes.CollectGoal;
import net.codingarea.challengesplugin.manager.ServerManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen
 * Challenges developed on 06-24-2020
 * https://github.com/anweisen
 */

public class CollectAllWood extends CollectGoal<LogType> implements Listener {

	private List<Player> winners = new ArrayList<>();

	public CollectAllWood() {
		menu = MenuType.GOALS;
		name = "collectwood";
		scoreboard = Challenges.getInstance().getScoreboardManager().getNewScoreboard(name);
	}

	@Override
	public String getChallengeName() {
		return "collectwood";
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.OAK_LOG, ItemTranslation.COLLECT_WOOD).build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		if (Challenges.timerIsStarted()) showScoreboard();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		hideScoreboard();
	}

	@Override
	public void onTimerStart() {
		if (!isCurrentGoal) return;
		points = new ConcurrentHashMap<>();
		showScoreboard();
	}

	@EventHandler
	public void onPlayerItemCollect(PlayerPickupItemEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		LogType log = LogType.getType(event.getItem().getItemStack().getType());
		handleNewPoint(event.getPlayer(), log, log.name(), Translation.COLLECT_ITEMS_ITEM_REGISTERED);
		checkEnd();

	}

	@EventHandler
	public void onPlayerCraftItem(InventoryClickEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getCurrentItem() == null) return;
		if (event.getClickedInventory() == null) return;
		Player player = (Player) event.getWhoClicked();
		if (!event.getClickedInventory().equals(player.getInventory()) && event.getClickedInventory().getType() == InventoryType.CHEST) return;

		LogType log = LogType.getType(event.getCurrentItem().getType());
		handleNewPoint(player, log, log.name(), Translation.COLLECT_ITEMS_ITEM_REGISTERED);
		checkEnd();

	}

	private void checkEnd() {
		for (Entry<UUID, List<LogType>> currentEntry : points.entrySet()) {
			if (currentEntry.getValue().size() == LogType.values().length) {
				winners.add(Bukkit.getPlayer(currentEntry.getKey()));
			}
		}
		if (winners.size() >= 1) {
			ServerManager.simulateChallengeEnd(null, ChallengeEndCause.PLAYER_CHALLENGE_GOAL_REACHED);
		}
	}

}
