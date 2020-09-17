package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.manager.WorldManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.timer.ChallengeTimer;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen & KxmischesDomi
 * Challenges developed on 11.08.2020
 * https://www.github.com/anweisen
 * https://www.github.com/KxmischesDomi
 */

public class JumpAndRun extends AdvancedChallenge implements Listener, ISecondExecutor {

	private final Random random = new Random();
	private final List<UUID> last = new ArrayList<>();
	private UUID current;

	private int jumpsDone;
	private int jumps = 4;

	private Block target;
	private Block start;

	private boolean waiting;

	private HashMap<UUID, Location> beforeTeleport = new HashMap<>();

	public JumpAndRun() {
		super(MenuType.CHALLENGES, 10);
		value = maxValue;
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		setNextSeconds();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onValueChange(ChallengeEditEvent event) {
		if (nextActionInSeconds > (value*60)) {
			setNextSeconds();
		}
	}

	@Override
	public void onTimeActivation() {
		nextActionInSeconds = -1;
		start();
	}

	private void start() {

		waiting = false;
		WorldManager.getInstance().setWorldIsInUse(true);
		current = getNextPlayer().getUniqueId();
		last.add(current);

		buildFirstJump();

		Location teleport = start.getLocation().clone().add(0.5, 1,0.5);
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {

			beforeTeleport.put(currentPlayer.getUniqueId(), currentPlayer.getLocation());
			currentPlayer.teleport(teleport);
			AnimationSound.KLING_SOUND.playDelayed(Challenges.getInstance(), currentPlayer, 1);

			if (currentPlayer.getUniqueId().equals(current)) {
				currentPlayer.setGameMode(GameMode.SURVIVAL);
			} else {
				currentPlayer.setGameMode(GameMode.SPECTATOR);
			}

		}

		Player player = Bukkit.getPlayer(current);
		Utils.lookAt(player.getLocation(), target.getLocation().clone().add(0, 1, 0), false, false);

	}

	private void buildFirstJump() {
		start = new Location(WorldManager.getInstance().getChallengesWorld(), 0, 100, 0).getBlock();
		buildNextJump(true);
	}

	private void buildNextJump(boolean first) {

		if (!first) {
			start.setType(Material.AIR);
			start = target;
		}

		int x = random.nextInt(3)+2;
		int y = random.nextInt(2);
		int z = random.nextInt(4)+1;

		if (random.nextBoolean()) {
			if (random.nextBoolean()) {
				x = 4;
			} else {
				z = 4;
			}
		} else if (random.nextBoolean()) {
			if (random.nextBoolean()) {
				x = 3;
			} else {
				z = 3;
			}
		}

		if (x == 4 || z == 4) {
			if (y == 1) y = 0;
		}

		if (x == 4) {
			if (z > 1) {
				z = 1;
			}
		}
		if (z == 4) {
			if (x > 1) {
				x = 1;
			}
		}

		if (x == 3) {
			if (z > 2) {
				z = 2;
			}
		}
		if (z == 3) {
			if (x > 2) {
				x = 2;
			}
		}

		if (random.nextBoolean()) {
			x = -x;
		}
		if (random.nextBoolean()) {
			z = -z;
		}

		target = start.getRelative(x, y, z);

		if ((jumpsDone+1) >= jumps) {
			target.setType(Material.EMERALD_BLOCK);
		} else {
			target.setType(Material.CYAN_TERRACOTTA);
		}

		start.setType(Material.GOLD_BLOCK);

	}

	private Player getNextPlayer() {

		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		players.removeIf(player -> player.getGameMode() == GameMode.SPECTATOR);
		if (players.isEmpty()) return null;

		players.removeIf(player -> last.contains(player.getUniqueId()));

		if (players.isEmpty()) {
			for (UUID currentUUID : last) {
				players.add(Bukkit.getPlayer(currentUUID));
			}
			last.clear();
		}

		return players.get(random.nextInt(players.size()));

	}

	private void setNextSeconds() {
		nextActionInSeconds = getRandomSeconds();
	}

	private int getRandomSeconds() {
		int max = Utils.getRandomSecondsUp(value*60);
		int min = Utils.getRandomSecondsDown(value*60);
		return random.nextInt(max - min) + min;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!event.getPlayer().getUniqueId().equals(current)) return;
		if (event.getTo() == null) return;

		if (event.getTo().getBlock().getLocation().equals(target.getRelative(0, 1, 0).getLocation())) {
			handleReachTarget();
			return;
		}

		if (target != null) {
			if (event.getTo().getBlockY() < (target.getLocation().getBlockY()-2)) {
				handleJNRFail();
			}
		}

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		if (event.getPlayer().getUniqueId().equals(current)) {
			handleJNREnd();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (beforeTeleport.containsKey(event.getPlayer().getUniqueId())) {
			event.getPlayer().teleport(beforeTeleport.get(event.getPlayer().getUniqueId()));
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (current == null) return;
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (current == null) return;
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		event.setCancelled(true);
	}

	private void handleReachTarget() {
		AnimationSound.PLOP_SOUND.play(Bukkit.getPlayer(current));
		jumpsDone++;
		if (jumpsDone >= jumps) {
			Player player = Bukkit.getPlayer(current);
			Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.JUMP_AND_RUN_COMPLETE.get().replace("%player%", player.getName()));
			handleJNREnd();
			Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> Utils.forEachPlayerOnline(current -> current.setGameMode(GameMode.SURVIVAL)), 1);
		} else {
			buildNextJump(false);
		}
	}

	private void handleJNREnd() {

		teleportBack();
		WorldManager.getInstance().setWorldIsInUse(false);

		jumpsDone = 0;
		jumps++;
		start.setType(Material.AIR);
		target.setType(Material.AIR);
		start = null;
		target = null;
		current = null;
		setNextSeconds();

	}

	private void handleJNRFail() {
		Player failed = Bukkit.getPlayer(current);
		handleJNREnd();
		Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.JUMP_AND_RUN_FAIL.get().replace("%player%", failed.getName()));
		failed.damage(failed.getHealth()*2);
	}

	private void teleportBack() {
		List<UUID> teleported = new ArrayList<>();
		for (Entry<UUID, Location> currentEntry : beforeTeleport.entrySet()) {
			Player player = Bukkit.getPlayer(currentEntry.getKey());
			if (player == null) continue;
			teleported.add(currentEntry.getKey());
			player.teleport(currentEntry.getValue());
			AnimationSound.TELEPORT_SOUND.play(player);
		}
		for (UUID currentUUID : teleported) {
			beforeTeleport.remove(currentUUID);
		}
	}

	@Override
	public void onTimerSecond() {
		if (!enabled) return;
		if (target != null) {
			Utils.spawnParticleCircle(target.getLocation().clone().add(0.5, 1, 0.5), Particle.SPELL_INSTANT, 15, 0.33);
		} else {
			if (nextActionInSeconds == 1 && WorldManager.getInstance().worldIsInUse()) {
				nextActionInSeconds = 1;
				if (!waiting) {
					waiting = true;
					Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.WORLD_IN_USE_WAIT.get());
				}
				return;
			}
			if (nextActionInSeconds == 0 || nextActionInSeconds <= -1) return;
			if (nextActionInSeconds <= 3 || nextActionInSeconds == 5 || nextActionInSeconds == 10) {
				Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.JUMP_AND_RUN_TELEPORT_IN.get().replace("%time%", ChallengeTimer.getTimeDisplay(nextActionInSeconds)));
				AnimationSound.OFF_SOUND.broadcast();
			}
		}
	}

	@NotNull
	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.ACACIA_STAIRS, ItemTranslation.JUMP_AND_RUN).build();
	}
}
