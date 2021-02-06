package net.codingarea.challenges.plugin;

import net.codingarea.challenges.plugin.lang.LanguageLoader;
import net.codingarea.challenges.plugin.management.challenges.ChallengeManager;
import net.codingarea.challenges.plugin.management.challenges.ChallengeRegistry;
import net.codingarea.challenges.plugin.management.menu.MenuManager;
import net.codingarea.challenges.plugin.management.timer.ChallengeTimer;
import net.codingarea.challenges.plugin.spigot.command.ChallengesCommand;
import net.codingarea.challenges.plugin.spigot.command.PauseCommand;
import net.codingarea.challenges.plugin.spigot.command.StartCommand;
import net.codingarea.challenges.plugin.spigot.command.TimerCommand;
import net.codingarea.challenges.plugin.spigot.listener.InventoryListener;
import net.codingarea.challenges.plugin.utils.misc.ConsolePrint;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public final class Challenges extends JavaPlugin {

	private static Challenges instance;

	@Nonnull
	public static Challenges getInstance() {
		return instance;
	}

	private ChallengeManager challengeManager;
	private MenuManager menuManager;
	private ChallengeTimer timer;

	private boolean validationFailed = false;

	@Override
	public void onLoad() {

		instance = this;
		if (validationFailed = validate()) return; // Handle in enable

		saveDefaultConfig();
		loadManagers();

	}

	@Override
	public void onEnable() {

		if (validationFailed) {
			Utils.disablePlugin();
			return;
		}

		enableManagers();

		registerCommands();
		registerListeners();

	}

	@Override
	public void onDisable() {
		if (timer != null) timer.stopScheduler();
		if (menuManager != null) menuManager.close();
	}

	private void loadManagers() {

		LanguageLoader languageLoader = new LanguageLoader();
		languageLoader.download();
		languageLoader.read();

		timer = new ChallengeTimer();
		challengeManager = new ChallengeManager();
		menuManager = new MenuManager();

		new ChallengeRegistry().load(); // Register challenges, replace with better way later

	}

	private void enableManagers() {

		menuManager.generateMenus();
		timer.startScheduler();

	}

	private void registerCommands() {
		registerCommand(new ChallengesCommand(), "challenges");
		registerCommand(new TimerCommand(), "timer");
		registerCommand(new PauseCommand(), "pause");
		registerCommand(new StartCommand(), "start");
	}

	private void registerListeners() {
		registerListener(new InventoryListener());
	}

	private void registerCommand(@Nonnull CommandExecutor executor, @Nonnull String... names) {
		for (String name : names) {
			PluginCommand command = getCommand(name);
			if (command != null)
				command.setExecutor(executor);
		}
	}

	private void registerListener(@Nonnull Listener... listeners) {
		for (Listener listener : listeners) {
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}

	private boolean validate() {
		if (!Utils.isSpigot()) {
			ConsolePrint.notSpigot();
			return true;
		}
		return false;
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

}
