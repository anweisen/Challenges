package net.codingarea.challenges.plugin.management.server.scoreboard;

import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeBossBar {

	public final class BossBarInstance {

		private String title = "";
		private double progress = 1;
		private BarColor color = BarColor.WHITE;
		private BarStyle style = BarStyle.SOLID;
		private boolean visible = true;

		private BossBarInstance() {}

		@Nonnull
		public BossBarInstance setTitle(@Nonnull String title) {
			this.title = title;
			return this;
		}

		@Nonnull
		public BossBarInstance setProgress(double progress) {
			if (progress < 0 || progress > 1) throw new IllegalArgumentException("Progress must be between 0 and 1; Got " + progress);
			this.progress = progress;
			return this;
		}

		@Nonnull
		public BossBarInstance setColor(@Nonnull BarColor color) {
			this.color = color;
			return this;
		}

		@Nonnull
		public BossBarInstance setStyle(@Nonnull BarStyle style) {
			this.style = style;
			return this;
		}
		@Nonnull
		public BossBarInstance setVisible(boolean visible) {
			this.visible = visible;
			return this;
		}

	}

	private final Map<Player, BossBar> bossbars = new ConcurrentHashMap<>();
	private BiConsumer<BossBarInstance, Player> content = (bossbar, player) -> {};

	private BossBar createBossbar(@Nonnull BossBarInstance instance) {
		BossBar bossbar = Bukkit.createBossBar(instance.title, instance.color, instance.style);
		bossbar.setProgress(instance.progress);
		return bossbar;
	}

	private void apply(@Nonnull BossBar bossbar, @Nonnull BossBarInstance instance) {
		bossbar.setTitle(instance.title);
		bossbar.setColor(instance.color);
		bossbar.setStyle(instance.style);
		bossbar.setProgress(instance.progress);
		bossbar.setVisible(instance.visible);
	}

	public void setContent(@Nonnull BiConsumer<BossBarInstance, Player> content) {
		this.content = content;
	}

	public void applyHide(@Nonnull Player player) {
		BossBar bossbar = bossbars.get(player);
		if (bossbar == null) return;
		bossbar.removePlayer(player);
	}

	public void update() {
		Bukkit.getOnlinePlayers().forEach(this::update);
	}

	public void update(@Nonnull Player player) {
		if (!isShown()) {
			Logger.warn("Tried to update bossbar which is not shown");
			return;
		}

		try {

			BossBarInstance instance = new BossBarInstance();
			content.accept(instance, player);

			BossBar bossbar = bossbars.computeIfAbsent(player, key -> createBossbar(instance));
			apply(bossbar, instance);

			if (!bossbar.getPlayers().contains(player))
				bossbar.addPlayer(player);

		} catch (Exception ex) {
			Logger.error("Unable to update bossbar for player '{}'", player.getName(), ex);
		}
	}

	public final void show() {
		Challenges.getInstance().getScoreboardManager().showBossBar(this);
	}

	public final void hide() {
		Challenges.getInstance().getScoreboardManager().hideBossBar(this);
	}

	public final boolean isShown() {
		return Challenges.getInstance().getScoreboardManager().isShown(this);
	}

}
