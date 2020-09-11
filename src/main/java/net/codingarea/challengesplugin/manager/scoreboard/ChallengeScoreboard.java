package net.codingarea.challengesplugin.manager.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ChallengeScoreboard {

	public static final String STANDARD_DISPLAYNAME = "   §f» §f§lChallenge   ";

	private Objective objective;
	private Objective update;

	private String name;
	private String displayName = STANDARD_DISPLAYNAME;

	private String[] lines;

	protected ChallengeScoreboard(Objective objective) {

		if (objective == null) throw new NullPointerException("Objective cannot be null!");
		this.objective = objective;
		this.objective.setRenderType(RenderType.INTEGER);
		this.name = objective.getName();

		lines = new String[15];

	}

	public void show() {

		if (objective == null) {
			objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, name, name);
		}

		checkUpdate();

		objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	}

	public void hide() {

		if (objective == null) {
			objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, name, name);
		}

		checkUpdate();

		objective.setDisplaySlot(null);

	}

	public ChallengeScoreboard setDisplayName(String name) {
		displayName = name;
		return this;
	}

	public ChallengeScoreboard applyChanges() {

		checkUpdate();

		if (lines != null) {
			for (int i = 0; i < lines.length; i++) {

				if (lines[i] == null) continue;

				update.getScore(lines[i]).setScore(i);

			}
		}

		update.setDisplayName(displayName);
		update.setDisplaySlot(DisplaySlot.SIDEBAR);

		objective.unregister();
		objective = update;
		update = null;
		return this;

	}

	public ChallengeScoreboard setLine(int line, String text) {
		if (objective == null) throw new NullPointerException("Objective is null!");
		lines[line] = text;
		return this;
	}

	public void checkUpdate() {

		if (objective == null) {
			objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, name, name);
		}

		if (update != null) return;

		String name = objective.getName();

		if (name.endsWith("+")) {
			name = name.substring(0, name.length() - 1);
		} else {
			name += "+";
		}

		try {
			update = objective.getScoreboard().registerNewObjective(name, name, name);
		} catch (IllegalArgumentException ignored) {
			name = name.substring(0, 3);
			update = objective.getScoreboard().registerNewObjective(name, name, name);
		}

		update.setDisplayName(displayName);

	}

	public Objective getObjective() {
		return objective;
	}

	public Objective getUpdateObjective() {
		return update;
	}

	public Score getScore(String player) {
		checkUpdate();
		return update.getScore(player);
	}

	public void setObjective(Objective objective) {
		if (objective == null) return;
		if (this.objective != null) this.objective.unregister();
		this.objective = objective;
	}

	public void destroyScoreboard() {

		if (objective != null) {
			objective.unregister();
			objective = null;
		}
		if (update != null) {
			update.unregister();
			update = null;
		}

	}
}
