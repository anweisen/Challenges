package net.codingarea.challenges.plugin.core;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.readonly.ReadOnlyYamlDocument;
import net.codingarea.challenges.plugin.utils.logging.BukkitLoggerWrapper;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.logging.LogLevel;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import net.codingarea.challenges.plugin.utils.version.Version;
import net.codingarea.challenges.plugin.utils.version.VersionInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see net.codingarea.challenges.plugin.Challenges
 */
public abstract class BukkitModule extends JavaPlugin {

	private final Map<String, CommandExecutor> commands = new HashMap<>();
	private final List<Listener> listeners = new ArrayList<>();

	private Version version;
	private Document config;
	private boolean devMode;
	private boolean firstInstall;

	@Override
	public void onLoad() {
		version = VersionInfo.parse(this.getDescription().getVersion());
		if (firstInstall = !getDataFolder().exists()) {
			getLogger().info("Detected first install!");
		}
		if (devMode = getConfigDocument().getBoolean("dev-mode")) {
			getLogger().setLevel(Level.ALL);
			getLogger().log(LogLevel.DEBUG, "Devmode is enabled: Showing debug messages. This can be disabled in the plugin.yml ('dev-mode')");
		} else {
			getLogger().setLevel(Level.INFO);
		}
		saveDefaultConfig();
	}

	@Override
	public void onEnable() {
		commands.forEach((name, executor) -> registerCommand0(executor, name));
		listeners.forEach(listener -> registerListener(listener));
	}

	@Override
	public void onDisable() {
		commands.clear();
		listeners.clear();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getOpenInventory().close();
		}
	}

	public boolean isDevMode() {
		return devMode;
	}

	public boolean isFirstInstall() {
		return firstInstall;
	}

	@Nonnull
	@Override
	public Logger getLogger() {
		return new BukkitLoggerWrapper(super.getLogger());
	}

	@Nonnull
	public Document getConfigDocument() {
		return config != null ? config : (config = new ReadOnlyYamlDocument(super.getConfig()));
	}

	@Nonnull
	public Version getVersion() {
		return version;
	}

	@Nonnull
	@Override
	@Deprecated
	public FileConfiguration getConfig() {
		return super.getConfig();
	}

	@Override
	@Deprecated
	public void saveConfig() {
		super.saveConfig();
	}

	public final void registerCommand(@Nonnull CommandExecutor executor, @Nonnull String... names) {
		for (String name : names) {
			if (isEnabled()) {
				registerCommand0(executor, name);
			} else {
				commands.put(name, executor);
			}
		}
	}

	private void registerCommand0(@Nonnull CommandExecutor executor, @Nonnull String name) {
		PluginCommand command = getCommand(name);
		if (command == null) {
			getLogger().warning("Tried to register invalid command '" + name + "'");
		} else {
			command.setExecutor(executor);
		}
	}

	public final void registerListener(@Nonnull Listener... listeners) {
		if (isEnabled()) {
			for (Listener listener : listeners) {
				getServer().getPluginManager().registerEvents(listener, this);
			}
		} else {
			this.listeners.addAll(Arrays.asList(listeners));
		}
	}

	protected final boolean validate() {
		if (!Utils.isSpigot()) {
			ConsolePrint.notSpigot();
			return true;
		}
		return false;
	}

	public void runAsync(@Nonnull Runnable task) {
		Thread thread = new Thread(task);
		thread.setName("AsyncPluginTask");
		thread.start();
	}

}
