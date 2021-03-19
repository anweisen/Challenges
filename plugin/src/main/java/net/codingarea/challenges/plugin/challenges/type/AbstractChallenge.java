package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.ItemDescription;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractChallenge implements IChallenge, Listener {

	protected final Challenges plugin = Challenges.getInstance();

	protected final MenuType menu;
	protected final ChallengeBossBar bossbar = new ChallengeBossBar();
	protected final ChallengeScoreboard scoreboard = new ChallengeScoreboard();

	private String name;

	public AbstractChallenge(@Nonnull MenuType menu) {
		this.menu = menu;
	}

	@Nonnull
	@Override
	public final MenuType getType() {
		return menu;
	}

	protected final void updateItems() {
		ChallengeHelper.updateItems(this);
	}

	@Nonnull
	@Override
	public ItemStack getDisplayItem() {
		return createDisplayItem().build();
	}

	@Nonnull
	@Override
	public ItemStack getSettingsItem() {
		return createSettingsItem().build();
	}

	@Nonnull
	public abstract ItemBuilder createDisplayItem();

	@Nonnull
	public abstract ItemBuilder createSettingsItem();

	@Nonnull
	@Override
	public String getName() {
		return name != null ? name : (name = getClass().getSimpleName().toLowerCase()
				.replace("setting", "")
				.replace("challenge", "")
				.replace("modifier", "")
				.replace("goal", ""));
	}

	@Override
	public void writeGameState(@Nonnull Document document) {
	}

	@Override
	public void loadGameState(@Nonnull Document document) {
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
	}

	@CheckReturnValue
	protected boolean shouldExecuteEffect() {
		return isEnabled() && ChallengeAPI.isStarted();
	}

	public final void broadcast(@Nonnull Consumer<Player> action) {
		Bukkit.getOnlinePlayers().forEach(action);
	}

}
