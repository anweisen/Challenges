package net.codingarea.challenges.plugin.challenges.type;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeBossBar;
import net.codingarea.challenges.plugin.management.server.scoreboard.ChallengeScoreboard;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public abstract class AbstractChallenge implements IChallenge, Listener {

	private static final Map<Class<? extends AbstractChallenge>, AbstractChallenge> firstInstanceByClass = new HashMap<>();

	protected final Challenges plugin = Challenges.getInstance();
	protected final MenuType menu;
	protected final ChallengeBossBar bossbar = new ChallengeBossBar();
	protected final ChallengeScoreboard scoreboard = new ChallengeScoreboard();

	private String name;

	public AbstractChallenge(@Nonnull MenuType menu) {
		this.menu = menu;
		firstInstanceByClass.put(this.getClass(), this);
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
		ItemBuilder item = createSettingsItem();
		String[] description = getSettingsDescription();
		if (description != null && isEnabled()) {
			item.appendLore(" ");
			item.appendLore(description);
		}

		return item.build();
	}

	@Nullable
	protected String[] getSettingsDescription() {
		return null;
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
		return isEnabled() && ChallengeAPI.isStarted() && !ChallengeAPI.isWorldInUse();
	}

	@CheckReturnValue
	protected boolean ignorePlayer(@Nonnull Player player) {
		return player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE;
	}

	protected final void broadcast(@Nonnull Consumer<Player> action) {
		Bukkit.getOnlinePlayers().forEach(action);
	}

	@Nonnull
	protected final Document getGamestateData() {
		return plugin.getConfigManager().getGamestateConfig().getDocument(getName());
	}

	@Nonnull
	protected final Document getPlayerData(@Nonnull UUID player) {
		return getGamestateData().getDocument("player").getDocument(player.toString());
	}

	@Nonnull
	protected final Document getPlayerData(@Nonnull Player player) {
		return getPlayerData(player.getUniqueId());
	}

	@Nonnull
	public static <C extends AbstractChallenge> C getFirstInstance(@Nonnull Class<C> classOfChallenge) {
		return classOfChallenge.cast(firstInstanceByClass.get(classOfChallenge));
	}

}
