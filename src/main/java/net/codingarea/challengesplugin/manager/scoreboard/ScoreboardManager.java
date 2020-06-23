package net.codingarea.challengesplugin.manager.scoreboard;

import net.codingarea.challengesplugin.Challenges;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ScoreboardManager {

	private Challenges plugin;

	private boolean show;

	private Scoreboard scoreboard;

	private ChallengeScoreboard currentBoard;

	public ScoreboardManager(Challenges plugin) {
		this.plugin = plugin;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	}

	public void hide() {
		if (!show) return;
		show = false;

		if (currentBoard == null) return;

		currentBoard.hide();

	}

	public void show() {
		if (show) return;
		show = true;

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			showForPlayer(currentPlayer);
		}

	}

	public void showForPlayer(Player player) {
		player.setScoreboard(scoreboard);
	}

	public void handleJoin(Player player) {
		if (!show) return;
		showForPlayer(player);
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
		return new ChallengeScoreboard(objective);
	}

}