package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author anweisen & Dominik
 * Challenges developed on 08-14-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class WaterMLG extends AdvancedChallenge implements Listener {

	private static class PlayerState {

		private final Location location;
		private final int slot;
		private final ItemStack[] inventory;
		private final GameMode gamemode;

		public PlayerState(Location location, int slot, ItemStack[] inventory, GameMode gamemode) {
			this.location = location;
			this.slot = slot;
			this.inventory = inventory;
			this.gamemode = gamemode;
		}
	}

	private final Random random = new Random();
	private final ArrayList<UUID> inMLG = new ArrayList<>();
	private final HashMap<UUID, PlayerState> before = new HashMap<>();
	private final ArrayList<Block> placedBlocks = new ArrayList<>();

	public WaterMLG() {
		super(MenuType.CHALLENGES, 10);
		value = maxValue;
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		setNextSeconds();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		endMLG(true);
	}

	@Override
	public void onValueChange(ChallengeEditEvent event) {
		if (nextActionInSeconds > (value*60)) {
			setNextSeconds();
		}
	}

	@Override
	public void onTimeActivation() {

		if (WorldManager.getInstance().worldIsInUse()) {
			nextActionInSeconds = 1;
			return;
		}

		nextActionInSeconds = -1;
		WorldManager.getInstance().setWorldIsInUse(true);

		for (int i = 0; i < Bukkit.getOnlinePlayers().size(); i++) {
			getMLGLocation(i).getChunk().load(true);
		}

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), this::joinMLG, 20);

	}

	private void joinMLG() {

		placedBlocks.clear();

		int i = 0;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

			if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;

			PlayerState state = new PlayerState(currentPlayer.getLocation(), currentPlayer.getInventory().getHeldItemSlot(), currentPlayer.getInventory().getContents(), currentPlayer.getGameMode());
			before.put(currentPlayer.getUniqueId(), state);
			inMLG.add(currentPlayer.getUniqueId());

			currentPlayer.setGameMode(GameMode.SURVIVAL);
			currentPlayer.getInventory().clear();
			currentPlayer.getInventory().setItem(4, new ItemStack(Material.WATER_BUCKET));
			currentPlayer.teleport(getMLGLocation(i));

			AnimationSound.TELEPORT_SOUND.playDelayed(Challenges.getInstance(), currentPlayer, 1);

			i++;
		}

	}

	private void checkEnd() {

		List<UUID> reallyInMLG = new ArrayList<>();
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (inMLG.contains(currentPlayer.getUniqueId())) {
				reallyInMLG.add(currentPlayer.getUniqueId());
			}
		}

		if (reallyInMLG.size() == 0) {
			endMLG(false);
		}

	}

	private void endMLG(boolean reset) {
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			restore(currentPlayer);
			before.remove(currentPlayer.getUniqueId());
			if (!reset) AnimationSound.KLING_SOUND.playDelayed(Challenges.getInstance(), currentPlayer, 1);
		}
		if (!reset) Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.WATER_MLG_SUCCESS.get());
		for (Block currentBlock : placedBlocks) {
			currentBlock.setType(Material.AIR, true);
		}
		setNextSeconds();
		WorldManager.getInstance().setWorldIsInUse(false);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!inMLG.contains(event.getPlayer().getUniqueId())) return;
		if (event.getTo() == null) return;
		Block to = event.getTo().clone().add(0, 0.7, 0).getBlock();
		if (to.isLiquid()) {
			handleMLGComplete(event.getPlayer());
		}
	}

	private void handleMLGComplete(Player player) {
		inMLG.remove(player.getUniqueId());
		checkEnd();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (inMLG.remove(event.getPlayer().getUniqueId())) {
			checkEnd();
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (inMLG.remove(event.getEntity().getUniqueId())) {
			endMLG(false);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (before.containsKey(event.getPlayer().getUniqueId())) {
			restore(event.getPlayer());
			before.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!event.getBlock().getWorld().equals(WorldManager.getInstance().getChallengesWorld())) return;
		placedBlocks.add(event.getBlock());
	}

	@EventHandler
	public void onPlaceWater(PlayerBucketEmptyEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!event.getBlock().getWorld().equals(WorldManager.getInstance().getChallengesWorld())) return;
		placedBlocks.add(event.getBlock());
	}

	private void restore(Player player) {

		PlayerState state = before.get(player.getUniqueId());
		if (state == null) return;

		player.setFallDistance(0);
		player.setGameMode(state.gamemode);
		player.teleport(state.location);
		player.getInventory().setContents(state.inventory);
		player.getInventory().setHeldItemSlot(state.slot);

	}

	private Location getMLGLocation(int player) {
		return new Location(WorldManager.getInstance().getChallengesWorld(), 50*player, 100, 0);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.WATER_BUCKET, ItemTranslation.WATER_MLG).build();
	}

	private void setNextSeconds() {
		nextActionInSeconds = getRandomSeconds();
	}

	private int getRandomSeconds() {
		int max = Utils.getRandomSecondsUp(value*60);
		int min = Utils.getRandomSecondsDown(value*60);
		return random.nextInt(max - min) + min;
	}

}
