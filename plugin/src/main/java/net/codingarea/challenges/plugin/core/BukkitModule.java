package net.codingarea.challenges.plugin.core;

import net.codingarea.challenges.plugin.utils.misc.ConsolePrint;
import net.codingarea.challenges.plugin.utils.misc.Utils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class BukkitModule extends JavaPlugin {

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public final void registerCommand(@Nonnull CommandExecutor executor, @Nonnull String... names) {
		for (String name : names) {
			PluginCommand command = getCommand(name);
			if (command != null)
				command.setExecutor(executor);
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

}
