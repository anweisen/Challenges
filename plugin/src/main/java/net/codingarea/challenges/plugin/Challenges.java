package net.codingarea.challenges.plugin;

import net.codingarea.challenges.plugin.core.BukkitModule;
import net.codingarea.challenges.plugin.lang.loader.ContentLoader;
import net.codingarea.challenges.plugin.lang.loader.LanguageLoader;
import net.codingarea.challenges.plugin.lang.loader.PrefixLoader;
import net.codingarea.challenges.plugin.management.challenges.ChallengeManager;
import net.codingarea.challenges.plugin.management.challenges.ChallengeRegistry;
import net.codingarea.challenges.plugin.management.menu.MenuManager;
import net.codingarea.challenges.plugin.management.scheduler.ScheduleManager;
import net.codingarea.challenges.plugin.management.scheduler.timer.ChallengeTimer;
import net.codingarea.challenges.plugin.management.server.ServerManager;
import net.codingarea.challenges.plugin.spigot.command.ChallengesCommand;
import net.codingarea.challenges.plugin.spigot.command.PauseCommand;
import net.codingarea.challenges.plugin.spigot.command.StartCommand;
import net.codingarea.challenges.plugin.spigot.command.TimerCommand;
import net.codingarea.challenges.plugin.spigot.listener.InventoryListener;
import net.codingarea.challenges.plugin.spigot.listener.PlayerConnectionListener;
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

	private ChallengeManager challengeManager;
	private MenuManager menuManager;
	private ChallengeTimer timer;
	private ScheduleManager scheduler;
	private ServerManager serverManager;

	private boolean validationFailed = false;

	@Override
	public void onLoad() {
		instance = this;
		super.onLoad();

		if (validationFailed = validate()) return; // Handle in enable

		saveDefaultConfig();
		createManagers();

	}

	@Override
	public void onEnable() {
		super.onEnable();

		if (validationFailed) {
			Utils.disablePlugin();
			return;
		}

		enableManagers();

		registerCommands();
		registerListeners();

	}

	private void createManagers() {

		ContentLoader.executeLoaders(new LanguageLoader(), new PrefixLoader());

		serverManager = new ServerManager();
		scheduler = new ScheduleManager();
		timer = new ChallengeTimer();
		challengeManager = new ChallengeManager();
		menuManager = new MenuManager();

		new ChallengeRegistry().load(); // Register challenges, replace with better way later

	}

	private void enableManagers() {

		menuManager.generateMenus();
		scheduler.start();

	}

	private void registerCommands() {
		registerCommand(new ChallengesCommand(), "challenges");
		registerCommand(new TimerCommand(), "timer");
		registerCommand(new PauseCommand(), "pause");
		registerCommand(new StartCommand(), "start");
	}

	private void registerListeners() {
		registerListener(
				new InventoryListener(),
				new PlayerConnectionListener()
		);
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (scheduler != null) scheduler.stop();
		if (menuManager != null) menuManager.close();
		if (challengeManager != null) challengeManager.clear();
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
}
