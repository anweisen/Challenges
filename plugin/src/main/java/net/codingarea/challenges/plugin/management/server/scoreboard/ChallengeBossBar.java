package net.codingarea.challenges.plugin.management.server.scoreboard;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeBossBar {

	private BossBar bossbar;

	private String title = "";
	private double progress = 1;
	private BarColor color = BarColor.WHITE;
	private BarStyle style = BarStyle.SOLID;

	private void createBossbar() {
		bossbar = Bukkit.createBossBar(title, color, style);
		bossbar.setProgress(progress);
	}

	public void applyShow(@Nonnull Player player) {
		if (bossbar == null) createBossbar();
		bossbar.addPlayer(player);
	}

	public void applyHide(@Nonnull Player player) {
		if (bossbar == null) return;
		bossbar.removePlayer(player);
	}

	public void setStyle(@Nonnull BarStyle style) {
		this.style = style;
		if (bossbar != null)
			bossbar.setStyle(style);
	}

	public void setColor(@Nonnull BarColor color) {
		this.color = color;
		if (bossbar != null)
			bossbar.setColor(color);
	}

	public void setProgress(double progress) {
		if (progress < 0 || progress > 1) throw new IllegalArgumentException("Progress must be between 0 and 1");
		this.progress = progress;
		if (bossbar != null)
			bossbar.setProgress(progress);
	}

	public void setTitle(@Nonnull String title) {
		this.title = title;
		if (bossbar != null)
			bossbar.setTitle(title);
	}

	public final void show() {
		Challenges.getInstance().getScoreboardManager().showBossBar(this);
	}

	public final void hide() {
		Challenges.getInstance().getScoreboardManager().hideBossBar(this);
	}

}
