package net.codingarea.challengesplugin.manager.scoreboard;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ScoreboardManager {

	private static ScoreboardManager instance;

	private final List<ChallengeScoreboard> boardRegistered;

	private boolean show;

	private final Scoreboard scoreboard;

	private ChallengeScoreboard currentBoard;
	private final List<BossBar> activeBossBars;

	public ScoreboardManager() {
		instance = this;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		boardRegistered = new ArrayList<>();
		activeBossBars = new ArrayList<>();
	}

	public void hide() {
		show = false;
		if (currentBoard != null) {
			currentBoard.hide();
		}
	}

	public void show() {
		show = true;
		Utils.forEachPlayerOnline(this::showForPlayer);
	}

	public void showForPlayer(Player player) {
		player.setScoreboard(scoreboard);
	}

	public void setCurrentBoard(ChallengeScoreboard board) {
		this.currentBoard = board;
		if (board == null) {
			hide();
			return;
		}
		board.show();
	}

	public ChallengeScoreboard getCurrentBoard() {
		return currentBoard;
	}

	public ChallengeScoreboard getNewScoreboard(String name) {
		Objective objective = scoreboard.registerNewObjective(name, name, name);
		ChallengeScoreboard challengeScoreboard = new ChallengeScoreboard(objective);
		boardRegistered.add(challengeScoreboard);
		return challengeScoreboard;
	}

	public void destroyAllScoreboards() {
		for (ChallengeScoreboard currentBoard : boardRegistered) {
			currentBoard.destroyScoreboard();
		}
		Bukkit.getBossBars().forEachRemaining(currentBossBar -> {
			Bukkit.removeBossBar(currentBossBar.getKey());
		});
	}

	public void activateBossBar(BossBar bossBar) {
		activeBossBars.add(bossBar);
		Utils.forEachPlayerOnline(bossBar::addPlayer);
	}

	public void deactivateBossBar(BossBar bossBar) {
		activeBossBars.remove(bossBar);
		bossBar.removeAll();
	}

	public void handlePlayerConnect(PlayerJoinEvent event) {
		if (show) {
			showForPlayer(event.getPlayer());
		}
		for (BossBar currentBossBar : activeBossBars) {
			currentBossBar.addPlayer(event.getPlayer());
		}
	}

	public void handlePlayerDisconnect(PlayerQuitEvent event) {
		for (@NotNull Iterator<KeyedBossBar> iterator = Bukkit.getBossBars(); iterator.hasNext();) {
			BossBar currentBossBar = iterator.next();
			currentBossBar.removePlayer(event.getPlayer());
		}
	}

	public static ScoreboardManager getInstance() {
		return instance;
	}
}