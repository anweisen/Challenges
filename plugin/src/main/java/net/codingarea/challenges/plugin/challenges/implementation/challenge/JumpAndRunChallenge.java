package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.commons.common.IRandom;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.AbstractChallenge;
import net.codingarea.challenges.plugin.challenges.type.WorldDependentChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.policy.ExtraWorldPolicy;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import net.codingarea.challenges.plugin.utils.bukkit.jumpgeneration.RandomJumpGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class JumpAndRunChallenge extends WorldDependentChallenge {

	private final IRandom random = IRandom.create();
	private final List<UUID> lastPlayers = new ArrayList<>();

	private int jumps = 4;
	private int currentJump;
	private int jumpsDone;

	private Block targetBlock;
	private Block lastBlock;

	private UUID currentPlayer;

	public JumpAndRunChallenge() {
		super(MenuType.CHALLENGES, 1, 10, 5, false);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ACACIA_STAIRS, Message.forName("item-jump-and-run-challenge"));
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return Message.forName("item-time-seconds-range-description").asArray(getValue() * 60 - 30, getValue() * 60 + 30);
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeSecondsRangeValueChangeTitle(this, getValue() * 60 - 30, getValue() * 60 + 30);
	}

	@Override
	public void onDisable() {
		if (isInExtraWorld())
			exitJumpAndRun();
	}

	@Override
	protected void handleCountdown() {
		switch (getSecondsLeftUntilNextActivation()) {
			case 1:
				Message.forName("jnr-countdown-one").broadcast(Prefix.CHALLENGES);
				SoundSample.BASS_OFF.broadcast();
				break;
			case 2: case 3: case 5:
				Message.forName("jnr-countdown").broadcast(Prefix.CHALLENGES, getSecondsLeftUntilNextActivation());
				SoundSample.BASS_OFF.broadcast();
				break;
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onQuit(@Nonnull PlayerQuitEvent event) {
		if (currentPlayer == null) return;
		if (!currentPlayer.equals(event.getPlayer().getUniqueId())) return;
		exitJumpAndRun();
	}

	@Override
	protected int getSecondsUntilNextActivation() {
		return random.around(getValue() * 60, 30);
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
		targetBlock = new RandomJumpGenerator().next(random, lastBlock, type == Material.CYAN_TERRACOTTA || type == Material.EMERALD_BLOCK, type != Material.COBBLESTONE_WALL && type != Material.SPRUCE_FENCE);
		targetBlock.setType(type);

	}

	@Nonnull
	protected Material getRandomBlockType() {
		if (currentJump == jumps - 1) return Material.EMERALD_BLOCK;

		Material[] materials = {
			Material.CYAN_TERRACOTTA,
			Material.CYAN_TERRACOTTA,
			Material.CYAN_TERRACOTTA,
			Material.CYAN_TERRACOTTA,
			Material.END_ROD,
			Material.COBBLESTONE_WALL,
			Material.SPRUCE_FENCE
		};
		return random.choose(materials);
	}

	@Nonnull
	protected Player getNextPlayer() {
		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		players.removeIf(AbstractChallenge::ignorePlayer);
		players.removeIf(player -> lastPlayers.contains(player.getUniqueId()));

		if (players.isEmpty()) {
			players = new ArrayList<>(Bukkit.getOnlinePlayers());
			lastPlayers.clear();
		}
		return random.choose(players);
	}

	protected void finishJumpAndRun() {
		jumps++;
		jumpsDone++;

		Message.forName("jnr-finished").broadcast(Prefix.CHALLENGES, Optional.ofNullable(currentPlayer).map(Bukkit::getPlayer).map(NameHelper::getName).orElse("?"));
		exitJumpAndRun();
		SoundSample.KLING.broadcast();
	}

	protected void exitJumpAndRun() {
		currentPlayer = null;
		teleportBack();
		breakJumpAndRun();
		restartTimer();
	}

	protected void breakJumpAndRun() {
		if (lastBlock != null) lastBlock.setType(Material.AIR);
		if (targetBlock != null) targetBlock.setType(Material.AIR);

		lastBlock = null;
		targetBlock = null;
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

	@ScheduledTask(ticks = 20, timerPolicy = TimerPolicy.ALWAYS, worldPolicy = ExtraWorldPolicy.USED)
	public void spawnParticles() {
		if (targetBlock == null) return;
		ParticleUtils.spawnParticleCircle(targetBlock.getLocation().add(0.5, 1.05, 0.5), Particle.SPELL_INSTANT, 13, 0.35);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!event.getPlayer().getUniqueId().equals(currentPlayer)) return;
		if (targetBlock == null) return;
		if (event.getTo() == null) return;

		if (BlockUtils.isSameBlock(event.getTo(), targetBlock.getLocation().add(0, 1, 0))) {
			if (++currentJump >= jumps) {
				finishJumpAndRun();
			} else {
				SoundSample.PLOP.broadcast();
				buildNextJump();
			}
		} else if (event.getTo().getBlockY() < targetBlock.getY() - 2) {
			exitJumpAndRun();
			ChallengeAPI.endChallenge(ChallengeEndCause.GOAL_FAILED);
		}

	}

}
