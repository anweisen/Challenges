package net.codingarea.challenges.plugin.core;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.readonly.ReadOnlyYamlDocument;
import net.codingarea.challenges.plugin.utils.logging.BukkitLoggerWrapper;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.logging.LogLevel;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import net.codingarea.challenges.plugin.utils.version.Version;
import net.codingarea.challenges.plugin.utils.version.VersionInfo;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see net.codingarea.challenges.plugin.Challenges
 */
public abstract class BukkitModule extends JavaPlugin {

	private Version version;
	private Document config;
	private boolean devMode;

	@Override
	public void onLoad() {
		version = VersionInfo.parse(this.getDescription().getVersion());
		if (devMode = getConfigDocument().getBoolean("dev-mode")) {
			getLogger().setLevel(Level.ALL);
			getLogger().log(LogLevel.DEBUG, "Devmode is enabled: Showing debug messages. This can be disabled in the plugin.yml ('dev-mode')");
		} else {
			getLogger().setLevel(Level.INFO);
		}
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public boolean isDevMode() {
		return devMode;
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
			PluginCommand command = getCommand(name);
			if (command == null) {
				getLogger().warning("Tried to register invalid command '" + name + "'");
			} else {
				command.setExecutor(executor);
			}
		}
	}

	public final void registerListener(@Nonnull Listener... listeners) {
		for (Listener listener : listeners) {
			getServer().getPluginManager().registerEvents(listener, this);
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
