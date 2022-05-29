package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.item.ItemUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.config.Document;
import net.anweisen.utilities.common.config.document.GsonDocument;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.abstraction.ForceBattleGoal;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.3
 */
@Since("2.1.3")
public class ForceItemBattleGoal extends ForceBattleGoal {

	private final Map<UUID, List<Material>> foundItems = new HashMap<>();
	private final Map<UUID, Material> currentItem = new HashMap<>();


	private Material[] itemsPossibleToFind;

	public ForceItemBattleGoal() {
		super(MenuType.GOAL, Message.forName("menu-force-item-battle-goal-settings"));

		registerSetting("give-item", new BooleanSubSetting(
				() -> new ItemBuilder(Material.CHEST, Message.forName("item-force-item-battle-goal-give-item")),
				false
		));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		List<Material> materials = new ArrayList<>(Arrays.asList(Material.values()));
		materials.removeIf(material -> !material.isItem());
		materials.removeIf(material -> !ItemUtils.isObtainableInSurvival(material));
		itemsPossibleToFind = materials.toArray(new Material[0]);

		scoreboard.setContent((board, player) -> {
			List<Player> ingamePlayers = ChallengeAPI.getIngamePlayers();
			int emptyLinesAvailable = 15 - ingamePlayers.size();

			if (emptyLinesAvailable > 0) {
				board.addLine("");
				emptyLinesAvailable--;
			}

			for (int i = 0; i < ingamePlayers.size() && i < 15; i++) {
				Player ingamePlayer = ingamePlayers.get(i);
				Material material = currentItem.get(ingamePlayer.getUniqueId());
				String display = material == null ? Message.forName("none").asString()
						: StringUtils.getEnumName(material);
				board.addLine(NameHelper.getName(ingamePlayer) + " §8» §e" + display);
			}

			if (emptyLinesAvailable > 0) {
				board.addLine("");
			}
		});
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		itemsPossibleToFind = null;
	}

	@Override
	public void writeGameState(@NotNull Document document) {
		List<Document> playersDocuments = new LinkedList<>();
		for (Entry<UUID, Material> entry : currentItem.entrySet()) {
			List<Material> foundItems = this.foundItems.get(entry.getKey());
			int jokerUsed = this.jokerUsed.getOrDefault(entry.getKey(), 0);
			GsonDocument playerDocument = new GsonDocument();
			playerDocument.set("uuid", entry.getKey());
			playerDocument.set("currentItem", entry.getValue());
			playerDocument.set("foundItems", foundItems);
			playerDocument.set("jokerUsed", jokerUsed);
			playersDocuments.add(playerDocument);
		}

		document.set("players", playersDocuments);
	}

	@Override
	public void loadGameState(@NotNull Document document) {
		super.loadGameState(document);
		this.currentItem.clear();
		this.foundItems.clear();

		List<Document> players = document.getDocumentList("players");
		for (Document player : players) {
			UUID uuid = player.getUUID("uuid");

			Material currentItem = player.getEnum("currentItem", Material.class);
			if (currentItem != null) {
				this.currentItem.put(uuid, currentItem);
			}
			List<Material> foundItems = player.getEnumList("foundItems", Material.class);
			this.foundItems.put(uuid, foundItems);
		}
	}

	@Override
	public void getWinnersOnEnd(@NotNull List<Player> winners) {

		Bukkit.getScheduler().runTask(plugin, () -> {
			int place = 0;
			int placeValue = -1;

			List<Entry<UUID, List<Material>>> list = foundItems.entrySet().stream()
					.sorted(Comparator.comparingInt(value -> value.getValue().size()))
					.collect(Collectors.toList());
			Collections.reverse(list);

			Message.forName("force-item-battle-leaderboard").broadcast(Prefix.CHALLENGES);

			for (Entry<UUID, List<Material>> entry : list) {
				if (entry.getValue().size() != placeValue) {
					place++;
					placeValue = entry.getValue().size();
				}
				UUID uuid = entry.getKey();
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				ChatColor color = getPlaceColor(place);
				Message.forName("force-item-battle-leaderboard-entry")
						.broadcast(Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
			}

		});

	}

	@Override
	public void sendResult(@NotNull Player player) {

		Bukkit.getScheduler().runTask(plugin, () -> {
			int place = 0;
			int placeValue = -1;

			List<Entry<UUID, List<Material>>> list = foundItems.entrySet().stream()
					.sorted(Comparator.comparingInt(value -> value.getValue().size()))
					.collect(Collectors.toList());
			Collections.reverse(list);

			Message.forName("force-item-battle-leaderboard").send(player, Prefix.CHALLENGES);

			for (Entry<UUID, List<Material>> entry : list) {
				if (entry.getValue().size() != placeValue) {
					place++;
					placeValue = entry.getValue().size();
				}
				UUID uuid = entry.getKey();
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				ChatColor color = getPlaceColor(place);
				Message.forName("force-item-battle-leaderboard-entry")
						.send(player, Prefix.CHALLENGES, color, place, NameHelper.getName(offlinePlayer), entry.getValue().size());
			}

		});

	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.END_CRYSTAL, Message.forName("item-force-item-battle-goal"));
	}

	@Override
	public void handleTargetFound(Player player) {
		Material foundItem = currentItem.get(player.getUniqueId());
		if (foundItem != null) {
			List<Material> list = foundItems
					.computeIfAbsent(player.getUniqueId(), uuid -> new LinkedList<>());
			list.add(foundItem);
			Message.forName("force-item-battle-found")
					.send(player, Prefix.CHALLENGES, StringUtils.getEnumName(foundItem));
		}
		setRandomTarget(player);
	}

	@Override
	public void setRandomTargetIfCurrentlyNone(Player player) {
		if (currentItem.containsKey(player.getUniqueId())) {
			return;
		}
		setRandomTarget(player);
	}

	@Override
	public void setRandomTarget(Player player) {
		Material material = globalRandom.choose(itemsPossibleToFind);
		currentItem.put(player.getUniqueId(), material);
		scoreboard.update();
		updateDisplayStand(player);
		Message.forName("force-item-battle-new-item")
				.send(player, Prefix.CHALLENGES, StringUtils.getEnumName(material));
		SoundSample.PLING.play(player);
	}

	@Override
	public void handleJokerUse(Player player) {
		super.handleJokerUse(player);
		if(giveItemOnSkip()) {
			InventoryUtils.dropOrGiveItem(player.getInventory(), player.getLocation(), currentItem.get(player.getUniqueId()));
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onClick(PlayerInventoryClickEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (event.getClickedInventory() == null) return;
		if (event.getCurrentItem() == null) return;
		Material material = currentItem.get(event.getPlayer().getUniqueId());
		if (material == null) return;
		if (material == event.getCurrentItem().getType()) {
			handleTargetFound(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPickup(PlayerPickupItemEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		Material material = currentItem.get(event.getPlayer().getUniqueId());
		if (material == null) return;
		if (material == event.getItem().getItemStack().getType()) {
			handleTargetFound(event.getPlayer());
		}
	}

	@Override
	public void handleDisplayStandUpdate(@NotNull Player player, @NotNull ArmorStand armorStand) {
		Material item = currentItem.get(player.getUniqueId());
		if (item == null) {
			item = Material.AIR;
		}

		ItemStack helmet = armorStand.getEquipment().getHelmet();
		if (helmet == null || helmet.getType() != item) {
			armorStand.getEquipment().setHelmet(new ItemStack(item));
		}
	}

	@Override
	public double getDisplayStandYOffset() {
		return 1.5D;
	}

	private boolean giveItemOnSkip() {
		return getSetting("give-item").getAsBoolean();
	}
}
