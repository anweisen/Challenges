package net.codingarea.challenges.plugin;

import net.codingarea.challenges.plugin.core.BukkitModule;
import net.codingarea.challenges.plugin.lang.loader.ContentLoader;
import net.codingarea.challenges.plugin.lang.loader.LanguageLoader;
import net.codingarea.challenges.plugin.lang.loader.PrefixLoader;
import net.codingarea.challenges.plugin.lang.loader.UpdateLoader;
import net.codingarea.challenges.plugin.management.blocks.BlockDropManager;
import net.codingarea.challenges.plugin.management.challenges.ChallengeLoader;
import net.codingarea.challenges.plugin.management.challenges.ChallengeManager;
import net.codingarea.challenges.plugin.management.cloudnet.CloudNetHelper;
import net.codingarea.challenges.plugin.management.database.DatabaseManager;
import net.codingarea.challenges.plugin.management.files.ConfigManager;
import net.codingarea.challenges.plugin.management.inventory.PlayerInventoryManager;
import net.codingarea.challenges.plugin.management.menu.MenuManager;
import net.codingarea.challenges.plugin.management.scheduler.ScheduleManager;
import net.codingarea.challenges.plugin.management.scheduler.timer.ChallengeTimer;
import net.codingarea.challenges.plugin.management.server.ScoreboardManager;
import net.codingarea.challenges.plugin.management.server.ServerManager;
import net.codingarea.challenges.plugin.management.server.TitleManager;
import net.codingarea.challenges.plugin.management.server.WorldManager;
import net.codingarea.challenges.plugin.management.stats.StatsManager;
import net.codingarea.challenges.plugin.spigot.command.*;
import net.codingarea.challenges.plugin.spigot.listener.CheatListener;
import net.codingarea.challenges.plugin.spigot.listener.InventoryListener;
import net.codingarea.challenges.plugin.spigot.listener.PlayerConnectionListener;
import net.codingarea.challenges.plugin.spigot.listener.RestrictionListener;
import net.codingarea.challenges.plugin.utils.misc.Utils;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public final class Challenges extends BukkitModule {

	private static Challenges instance;

	@Nonnull
	public static Challenges getInstance() {
		return instance;
	}

	private PlayerInventoryManager playerInventoryManager;
	private ScoreboardManager scoreboardManager;
	private ChallengeManager challengeManager;
	private BlockDropManager blockDropManager;
	private ChallengeLoader challengeLoader;
	private DatabaseManager databaseManager;
	private CloudNetHelper cloudNetHelper;
	private ServerManager serverManager;
	private ConfigManager configManager;
	private ScheduleManager scheduler;
	private StatsManager statsManager;
	private WorldManager worldManager;
	private TitleManager titleManager;
	private MenuManager menuManager;
	private ChallengeTimer timer;

	private boolean validationFailed = false;

	@Override
	public void onLoad() {
		instance = this;
		super.onLoad();

		if (validationFailed = (validate() || validationFailed)) return; // Handle in enable

		createManagers();
		loadManagers();

	}

	@Override
	public void onEnable() {
		if (validationFailed) {
			disable();
			return;
		}

		super.onEnable();

		enableManagers();

		registerCommands();
		registerListeners();

	}

	private void createManagers() {

		ContentLoader.executeLoaders(new LanguageLoader(), new PrefixLoader(), new UpdateLoader());

		configManager = new ConfigManager();
		configManager.loadConfigs();

		databaseManager = new DatabaseManager();
		worldManager = new WorldManager();
		serverManager = new ServerManager();
		scheduler = new ScheduleManager();
		scoreboardManager = new ScoreboardManager();
		cloudNetHelper = new CloudNetHelper();
		titleManager = new TitleManager();
		timer = new ChallengeTimer();
		blockDropManager = new BlockDropManager();
		challengeManager = new ChallengeManager();
		challengeLoader = new ChallengeLoader();
		menuManager = new MenuManager();
		playerInventoryManager = new PlayerInventoryManager();
		statsManager = new StatsManager();

	}

	private void loadManagers() {

		challengeLoader.load();

	}

	private void enableManagers() {

		worldManager.executeWorldResetIfNecessary();
		databaseManager.connectIfCreated();
		timer.loadSession();
		challengeManager.enable();
		statsManager.register();
		scheduler.start();
		playerInventoryManager.enable();

	}

	private void registerCommands() {
		registerCommand(new ChallengesCommand(), "challenges");
		registerCommand(new TimerCommand(), "timer");
		registerCommand(new PauseCommand(), "pause");
		registerCommand(new StartCommand(), "start");
		registerCommand(new ResetCommand(), "reset");
		registerCommand(new StatsCommand(), "stats");
	}

	private void registerListeners() {
		registerListener(
				new InventoryListener(),
				new PlayerConnectionListener(),
				new RestrictionListener(),
				new CheatListener(),
				new BlockDropListener()
		);
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (timer != null && worldManager != null && !worldManager.isShutdownBecauseOfReset()) timer.saveSession(false);
		if (scheduler != null) scheduler.stop();
		if (menuManager != null) menuManager.close();
		if (databaseManager != null) databaseManager.disconnectIfConnected();
		if (scoreboardManager != null) scoreboardManager.disable();

		if (challengeManager != null) {
			challengeManager.saveLocalSettings(false);
			if (worldManager != null && !worldManager.isShutdownBecauseOfReset())
				challengeManager.saveGamestate(false);
			challengeManager.clearChallengeCache();
		}
	}

	@Nonnull
	public ChallengeManager getChallengeManager() {
		return challengeManager;
	}

	@Nonnull
	public MenuManager getMenuManager() {
		return menuManager;
	}

	@Nonnull
	public ChallengeTimer getChallengeTimer() {
		return timer;
	}

	@Nonnull
	public ServerManager getServerManager() {
		return serverManager;
	}

	@Nonnull
	public ScheduleManager getScheduler() {
		return scheduler;
	}

	@Nonnull
	public ConfigManager getConfigManager() {
		return configManager;
	}

	@Nonnull
	public PlayerInventoryManager getPlayerInventoryManager() {
		return playerInventoryManager;
	}

	@Nonnull
	public WorldManager getWorldManager() {
		return worldManager;
	}

	@Nonnull
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	@Nonnull
	public StatsManager getStatsManager() {
		return statsManager;
	}

	@Nonnull
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}

	@Nonnull
	public CloudNetHelper getCloudNetHelper() {
		return cloudNetHelper;
	}

	@Nonnull
	public BlockDropManager getBlockDropManager() {
		return blockDropManager;
	}

	@Nonnull
	public TitleManager getTitleManager() {
		return titleManager;
	}

}
