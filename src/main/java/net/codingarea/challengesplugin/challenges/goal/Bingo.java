package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedGoal;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.items.ItemBuilder.SimpleEnchantment;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen
 * Challenges developed on 07-05-2020
 * https://github.com/anweisen
 */

public class Bingo extends AdvancedGoal implements CommandExecutor, TabCompleter, Listener {

	public static class BingoItem {

		private final Material material;
		private final List<SimpleEnchantment> enchantments;

		public BingoItem(Material material, SimpleEnchantment... enchantments) {
			this.material = material;
			this.enchantments = new ArrayList<>(Arrays.asList(enchantments));
			this.enchantments.removeIf(currentEnchantment -> currentEnchantment.getLevel() <= 0);
		}

		public BingoItem(Material material) {
			this.material = material;
			enchantments = new ArrayList<>();
		}

		public Material getMaterial() {
			return material;
		}

		public List<SimpleEnchantment> getEnchantments() {
			return enchantments;
		}

		public boolean containsEnchantment(SimpleEnchantment enchantment) {
			for (SimpleEnchantment currentEnchantment : enchantments) {
				if (currentEnchantment.getEnchantment() == enchantment.getEnchantment()) {
					if (currentEnchantment.getLevel() >= enchantment.getLevel()) {
						return true;
					}
				}
			}
			return false;
		}

		public boolean isSimilar(BingoItem item) {

			if (item == null || item.material != this.material) return false;

			boolean containsEnchantments = true;
			for (SimpleEnchantment currentEnchantment : enchantments) {
				if (!item.containsEnchantment(currentEnchantment)) containsEnchantments = false;
			}

			return containsEnchantments;

		}

		public ItemBuilder toItemBuilder() {
			return new ItemBuilder(material).setLore(enchantmentLore()).hideAttributes();
		}

		public List<String> enchantmentLore() {

			if (enchantments == null || enchantments.size() == 0) return new ArrayList<>();

			List<String> lore = new ArrayList<>();

			for (SimpleEnchantment currentEnchantment : enchantments) {
				lore.add("§8➟ §7" + Utils.getEnumName(currentEnchantment.getEnchantment().getKey().getKey()) + " §e" + Utils.getRomanNumbers(currentEnchantment.getLevel()));
			}

			return lore;

		}

		public static BingoItem fromItem(ItemStack item) {

			SimpleEnchantment[] enchantments = new SimpleEnchantment[item.getEnchantments().size()];

			int i = 0;
			for (Entry<Enchantment, Integer> currentEnchantment : item.getEnchantments().entrySet()) {
				SimpleEnchantment enchantment = new SimpleEnchantment(currentEnchantment.getKey(), currentEnchantment.getValue());
				enchantments[i] = enchantment;
				i++;
			}

			return new BingoItem(item.getType(), enchantments);

		}

	}

	private final Random random;

	private List<BingoItem> items;

	private final String title = "§8» §dBingo";
	private final String titleTeams = title + " §8• §9Teams";

	/*private final Color[] team_colors = {
			Color.AQUA, Color.BLUE, Color.GRAY, Color.LIME, Color.PURPLE, Color.RED, Color.YELLOW, Color.GREEN, Color.WHITE,
			Color.BLACK, Color.FUCHSIA, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.SILVER, Color.TEAL
	};*/
	/*private final ChatColor[] team_chat_colors = {
		ChatColor.AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.GREEN, ChatColor.DARK_PURPLE, ChatColor.RED, ChatColor.YELLOW, ChatColor.DARK_GREEN, ChatColor.WHITE,
		ChatColor.BLACK, ChatColor.LIGHT_PURPLE, ChatColor.DARK_RED, ChatColor.BLUE, ChatColor.DARK_GREEN, ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_AQUA
	};*/

	private final ChatColor[] team_colors = {
		ChatColor.WHITE, ChatColor.LIGHT_PURPLE, ChatColor.DARK_GREEN, ChatColor.GREEN, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.DARK_RED,
		ChatColor.DARK_PURPLE, ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.AQUA, ChatColor.GRAY, ChatColor.DARK_GRAY
	};
	private final int[] slots = {
			     3,      5,
		10, 11, 12, 13, 14, 15, 16,
		    20, 21, 22, 23, 24, 25
	};

	private final Map<Integer, List<BingoItem>> collectedItems; // Items the team has collected
	private final Map<UUID, Integer> teams;                     // Saves which player is in which team

	public Bingo() {
		super(MenuType.GOALS, 1);
		for (int i = 0; i < slots.length; i++) slots[i] += 9;   // Adds 9 slots to each slot because we want to have an empty row
		random = new Random();
		collectedItems = new HashMap<>();
		teams = new HashMap<>();
		setRandomItems();
	}

	@Override
	public void onValueChange(ChallengeEditEvent event) { }

	@Override
	public void onEnable(ChallengeEditEvent event) {
		teams.clear();
		Utils.forEachPlayerOnline(currentPlayer -> {
			int team = getBestTeamToJoin();
			teams.put(currentPlayer.getUniqueId(), team);
			sendTeamJoinedMessage(currentPlayer, team);
		});
		updateInventories();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public List<Player> getWinners() {
		if (value == 2) {
			return new ArrayList<>(Bukkit.getOnlinePlayers());
		}

		List<Integer> bestTeams = new ArrayList<>();
		for (Entry<Integer, List<BingoItem>> currentTeamItemsEntry : collectedItems.entrySet()) {
			if (currentTeamItemsEntry.getValue().size() >= 9) {
				bestTeams.add(currentTeamItemsEntry.getKey());
			}
		}
		List<Player> winner = new ArrayList<>();
		for (Entry<UUID, Integer> currentPlayerTeamEntry : teams.entrySet()) {
			if (bestTeams.contains(currentPlayerTeamEntry.getValue())) {
				Player currentWinningPlayer = Bukkit.getPlayer(currentPlayerTeamEntry.getKey());
				winner.add(currentWinningPlayer);
			}
		}

		return winner;
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.TRIPWIRE_HOOK, "§eBingo").hideAttributes().build();
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (!isCurrentGoal) {
			sender.sendMessage(Prefix.BINGO.get() + Translation.FEATURE_DISABLED);
			return true;
		}

		if (label.equalsIgnoreCase("resetbingo")) {
			setRandomItems();
			sender.sendMessage(Prefix.BINGO.get() + Translation.BINGO_RESET);
			return true;
		}

		if (!(sender instanceof Player)) return true;
		Player player = (Player) sender;

		if (label.equalsIgnoreCase("teams")) {
			player.openInventory(getTeamsInventory(player.getUniqueId()));
			AnimationSound.OPEN_SOUND.play(player);
			return true;
		}

		player.openInventory(getBingoInventory(player));
		AnimationSound.OPEN_SOUND.play(player);

		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		return null;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (!isCurrentGoal) return;
		int team = getBestTeamToJoin();
		teams.put(event.getPlayer().getUniqueId(), team);
		sendTeamJoinedMessage(event.getPlayer(), team);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null) return;
		if (!event.getView().getTopInventory().equals(event.getClickedInventory())) return;
		if (!event.getView().getTitle().contains(title)) return;
		if (!(event.getWhoClicked() instanceof Player)) return;
		if (event.getCurrentItem() == null) return;
		Player player = (Player) event.getWhoClicked();
		event.setCancelled(true);

		if (!event.getView().getTitle().equals(titleTeams)) return;

		if (event.getCurrentItem().getType().name().contains("WOOL")) {
			if (Challenges.timerIsStarted()) {
				player.sendMessage(Prefix.BINGO.get() + Translation.TIMER_ALREADY_STARTED);
				AnimationSound.OFF_SOUND.play(player);
			} else {
				handleTeamInventoryClick(event.getSlot(), player);
			}
		} else {
			AnimationSound.STANDARD_SOUND.play(player);
		}

	}

	private void handleTeamInventoryClick(int slot, Player player) {
		AnimationSound.PLOP_SOUND.play(player);
		int team = getTeamBySlot(slot);
		teams.put(player.getUniqueId(), team);
		sendTeamJoinedMessage(player, team);
		updateInventories();
	}

	private void updateInventories() {
		Utils.forEachPlayerOnline(currentPlayer -> {
			if (!currentPlayer.getOpenInventory().getTitle().equals(titleTeams)) return;
			currentPlayer.openInventory(getTeamsInventory(currentPlayer.getUniqueId()));
		});
	}

	private Inventory getBingoInventory(Player player) {

		Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, title);

		for (int i = 0; i < 9; i++) {

			BingoItem currentItem = items.get(i);
			boolean has = hasItemCollected(currentItem, player);

			ItemBuilder builder = currentItem.toItemBuilder();

			String prefix = has ? "§2✔" : "§c✖";
			String lore = has ? Translation.BINGO_ITEM_YES.get() : Translation.BINGO_ITEM_NO.get();
			builder.setDisplayName(prefix + " §8┃ §7" + Utils.getEnumName(currentItem.getMaterial().name()));
			builder.addLore(" ", lore);

			if (has) builder.addEnchant(new SimpleEnchantment(Enchantment.ARROW_INFINITE, 1));

			inventory.setItem(i, builder.build());

		}

		return inventory;

	}

	private boolean hasItemCollected(BingoItem item, Player player) {

		List<BingoItem> items = getItemsCollectedForPlayer(player.getUniqueId());
		for (BingoItem currentCollectedItem : items) {
			if (currentCollectedItem.isSimilar(item)) {
				return true;
			}
		}

		return false;

	}

	private void setRandomItems() {

		List<BingoItem> allItems = getAllItems();
		List<BingoItem> items = new ArrayList<>();

		for (int i = 0; i < 9; i++) {
			items.add(allItems.remove(random.nextInt(allItems.size())));
		}

		this.items = items;
		this.collectedItems.clear();

	}

	// TODO Add some more items
	private List<BingoItem> getAllItems() {

		Random random = new Random();
		List<BingoItem> items = new ArrayList<>();

		// Adding 1.16 items
		try {
			items.add(new BingoItem(Material.NETHERITE_INGOT));
			items.add(new BingoItem(Material.NETHERITE_SHOVEL, new SimpleEnchantment(Enchantment.DIG_SPEED, random.nextInt(3))));
			items.add(new BingoItem(Material.NETHERITE_PICKAXE, new SimpleEnchantment(Enchantment.DIG_SPEED, random.nextInt(3))));
		} catch (Error | Exception ignored) { }

		// Adding 1.13 items
		items.add(new BingoItem(Material.DIAMOND));
		items.add(new BingoItem(Material.DROPPER));
		items.add(new BingoItem(Material.CAKE));
		items.add(new BingoItem(Material.GREEN_DYE));
		items.add(new BingoItem(Material.MELON_SLICE));
		items.add(new BingoItem(Material.RABBIT_FOOT));
		items.add(new BingoItem(Material.GOLDEN_SWORD, new SimpleEnchantment(Enchantment.DAMAGE_ALL, random.nextInt(2))));
		items.add(new BingoItem(Material.ENCHANTING_TABLE));
		items.add(new BingoItem(Material.GOLDEN_APPLE));
		items.add(new BingoItem(Material.BONE));
		items.add(new BingoItem(Material.ANVIL));
		items.add(new BingoItem(Material.ACACIA_BOAT));
		items.add(new BingoItem(Material.WHEAT_SEEDS));
		items.add(new BingoItem(Material.WHEAT));
		items.add(new BingoItem(Material.EGG));
		items.add(new BingoItem(Material.RAIL));
		items.add(new BingoItem(Material.POWERED_RAIL));
		items.add(new BingoItem(Material.MINECART));
		items.add(new BingoItem(Material.IRON_PICKAXE, new SimpleEnchantment(Enchantment.DURABILITY, random.nextInt(3))));
		items.add(new BingoItem(Material.BOOK));
		items.add(new BingoItem(Material.BOOKSHELF));
		items.add(new BingoItem(Material.GOLD_ORE));
		items.add(new BingoItem(Material.EMERALD));
		items.add(new BingoItem(Material.FLINT));
		items.add(new BingoItem(Material.ROTTEN_FLESH));
		items.add(new BingoItem(Material.VINE));
		items.add(new BingoItem(Material.STONE));
		items.add(new BingoItem(Material.IRON_BLOCK));
		items.add(new BingoItem(Material.GOLDEN_SHOVEL));
		items.add(new BingoItem(Material.CACTUS));
		items.add(new BingoItem(Material.TRIPWIRE_HOOK));
		items.add(new BingoItem(Material.STRING));
		items.add(new BingoItem(Material.DARK_OAK_LOG));
		items.add(new BingoItem(Material.JUNGLE_FENCE));
		items.add(new BingoItem(Material.LEATHER_HELMET, new SimpleEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, random.nextInt(3))));
		items.add(new BingoItem(Material.MELON_SEEDS));
		items.add(new BingoItem(Material.BLUE_CARPET));
		items.add(new BingoItem(Material.GLOWSTONE_DUST));
		items.add(new BingoItem(Material.STONE_BUTTON));
		items.add(new BingoItem(Material.OAK_LEAVES));
		items.add(new BingoItem(Material.IRON_ORE));

		Collections.shuffle(items, random);
		return items;

	}

	public int getPlayerTeam(UUID uuid) {
		return teams.getOrDefault(uuid, -1);
	}

	public List<BingoItem> getItemsCollectedForPlayer(UUID uuid) {
		return getItemsCollectedForTeam(getPlayerTeam(uuid));
	}

	public List<BingoItem> getItemsCollectedForTeam(int team) {
		return collectedItems.getOrDefault(team, new ArrayList<>());
	}

	private Inventory getTeamsInventory(UUID playerUUID) {

		Inventory inventory = Bukkit.createInventory(null, 5*9, titleTeams);

		int playerTeam = getPlayerTeam(playerUUID);
		for (int i = 0; i < team_colors.length; i++) {
			ItemBuilder currentTeamItem = getTeamItem(i);
			if (playerTeam == i) currentTeamItem.setDisplayName(currentTeamItem.getDisplayName() + " §8(§eYou§8)");
			List<String> lore = new ArrayList<>();
			for (Player currentPlayerOnTeam : getPlayersOnTeam(i)) {
				lore.add("§8• §7" + currentPlayerOnTeam.getName() + (currentPlayerOnTeam.getUniqueId().equals(playerUUID) ? " " : ""));
			}
			currentTeamItem.setLore(lore);
			inventory.setItem(slots[i], currentTeamItem.build());
		}
		Utils.replaceItems(inventory, Material.AIR, ItemBuilder.FILL_ITEM);

		return inventory;

	}

	private int getTeamBySlot(int slot) {
		for (int i = 0; i < team_colors.length; i++) {
			if (slots[i] == slot) return i;
		}
		return -1;
	}

	private List<Player> getPlayersOnTeam(int team) {
		List<Player> players = new ArrayList<>();
		for (Entry<UUID, Integer> currentPlayerTeamEntry : teams.entrySet()) {
			if (currentPlayerTeamEntry.getValue() == team) {
				players.add(Bukkit.getPlayer(currentPlayerTeamEntry.getKey()));
			}
		}
		return players;
	}

	private ItemBuilder getTeamItem(int team) {
		ItemBuilder builder = new ItemBuilder(Utils.getWool(team_colors[team])).hideAttributes();
		builder.setDisplayName(getTeamName(team, true));
		return builder;
	}

	private String getTeamName(int team, boolean prefix) {
		return team_colors[team] + (prefix ? "Team " : "") + "#" + (team + 1);
	}

	private int getBestTeamToJoin() {

		Map<Integer, List<UUID>> playersOnTeam = new HashMap<>();
		for (Entry<UUID, Integer> currentPlayerTeamEntry : teams.entrySet()) {
			List<UUID> currentTeamPlayers = playersOnTeam.getOrDefault(currentPlayerTeamEntry.getValue(), new ArrayList<>());
			currentTeamPlayers.add(currentPlayerTeamEntry.getKey());
			playersOnTeam.put(currentPlayerTeamEntry.getValue(), currentTeamPlayers);
		}

		int best = 0;
		int playersOnBestTeam = -1;
		for (int i = 0; i < team_colors.length; i++) {

			int playersOnCurrentTeam = (playersOnTeam.containsKey(i) ? playersOnTeam.get(i).size() : 0);

			if (playersOnBestTeam > playersOnCurrentTeam) {
				best = i;
				playersOnBestTeam = playersOnCurrentTeam;
			}

		}

		return best;

	}

	private void sendTeamJoinedMessage(Player player, int team) {
		player.sendMessage(Prefix.BINGO + Translation.BINGO_TEAM_JOIN.get().replace("%team%", getTeamName(team, false)));
	}

}
