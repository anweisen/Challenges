package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.WorldDependentChallenge;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled;
import net.codingarea.challenges.plugin.management.scheduler.Scheduled.TimerPolicy;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.bukkit.jumpgeneration.RandomJumpGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static net.codingarea.challenges.plugin.utils.misc.RandomizeUtils.choose;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class JumpAndRunChallenge extends WorldDependentChallenge {

	private final Random random = new Random();
	private final List<UUID> lastPlayers = new ArrayList<>();

	private int jumps = 4;
	private int currentJump;
	private int jumpsDone;

	private Block targetBlock;
	private Block lastBlock;

	private UUID currentPlayer;

	public JumpAndRunChallenge() {
		super(MenuType.CHALLENGES, 10);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return null;
	}

	@Override
	@Scheduled(ticks = 20, async = false)
	public void onSecond() {
		super.onSecond();
	}

	@Override
	protected int getSecondsUntilNextActivation() {
//		return RandomizeUtils.getAround(random, 60 * getValue(), 30);
		return 10;
	}

	@Override
	protected void onTimeActivation() {
		startJumpAndRun();
	}

	protected void startJumpAndRun() {
		currentJump = 0;
		currentPlayer = getNextPlayer().getUniqueId();
		lastPlayers.add(currentPlayer);

		buildNextJump();
		teleportToWorld(true, (player, index) -> {
			player.setGameMode(player.getUniqueId().equals(currentPlayer) ? GameMode.SURVIVAL : GameMode.SPECTATOR);
			player.teleport(new Location(getExtraWorld(), 0.5, 101, 0.5));
			SoundSample.TELEPORT.play(player);
		});
	}

	protected void buildNextJump() {

		if (lastBlock != null) lastBlock.setType(Material.AIR);

		lastBlock = targetBlock != null ? targetBlock : getExtraWorld().getBlockAt(0, 100, 0);
		lastBlock.setType(Material.GOLD_BLOCK);

		Material type = getRandomBlockType();
		targetBlock = new RandomJumpGenerator().next(random, lastBlock, type == Material.BLUE_TERRACOTTA, type != Material.COBBLESTONE_WALL);
		targetBlock.setType(type);

	}

	@Nonnull
	protected Material getRandomBlockType() {
		Material[] materials = {
			Material.BLUE_TERRACOTTA,
			Material.BLUE_TERRACOTTA,
			Material.END_ROD,
			Material.COBBLESTONE_WALL
		};
		return materials[random.nextInt(materials.length)];
	}

	@Nonnull
	protected Player getNextPlayer() {
		List<? extends Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		players.removeIf(player -> player.getGameMode() == GameMode.SPECTATOR);
		players.removeIf(player -> lastPlayers.contains(player.getUniqueId()));

		if (players.isEmpty()) {
			players = new ArrayList<>(Bukkit.getOnlinePlayers());
			lastPlayers.clear();
		}
		return choose(random, players);
	}

	protected void endJumpAndRun() {
		teleportBack();
		jumps++;
		jumpsDone++;

		if (lastBlock != null) lastBlock.setType(Material.AIR);
		if (targetBlock != null) targetBlock.setType(Material.AIR);

		lastBlock = null;
		targetBlock = null;

		SoundSample.KLING.broadcast();
		restartTimer();
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
		super.writeGameState(document);
		document.set("jumps", jumps);
		document.set("jumpsDone", jumpsDone);
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
		super.loadGameState(document);
		jumps = document.getInt("jumps", jumps);
		jumpsDone = document.getInt("jumpsDone", jumpsDone);
	}

	@Scheduled(ticks = 20, timerPolicy = TimerPolicy.ALWAYS)
	public void spawnParticles() {
		if (!isEnabled()) return;
		if (!isInExtraWorld()) return;
		if (targetBlock == null) return;

		ParticleUtils.spawnParticleCircle(targetBlock.getLocation().add(0.5, 1.05, 0.5), Particle.SPELL_INSTANT, 13, 0.45);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!event.getPlayer().getUniqueId().equals(currentPlayer)) return;
		if (targetBlock == null) return;
		if (event.getTo() == null) return;
		if (!BlockUtils.isSameBlock(event.getTo(), targetBlock.getLocation().add(0, 1, 0))) return;

		if (++currentJump >= jumps) {
			endJumpAndRun();
		} else {
			SoundSample.PLOP.broadcast();
			buildNextJump();
		}
	}

}
