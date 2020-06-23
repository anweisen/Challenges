package net.codingarea.challengesplugin.manager;

import lombok.Getter;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.logging.Level;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-03-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class WorldManager {

	@Getter private static WorldManager instance;

	private boolean restartOnReset;
	private boolean reseted;

	private final Challenges plugin;

	private String levelName;
	private boolean reset;

	public WorldManager(Challenges plugin) {
		this.plugin = plugin;
	}

	public void loadSettings() {

		reset = plugin.getConfigManager().getInternalConfig().getConfig().getBoolean("reset");
		levelName = plugin.getConfigManager().getInternalConfig().getConfig().getString("level-name");

		if (levelName == null) {
			levelName = "world";
		}

		restartOnReset = plugin.getConfig().getBoolean("restart-on-reset");

	}

	public void resetWorlds() {

		if (!reset) return;

		long millis = System.currentTimeMillis();
		plugin.getLogger().log(Level.INFO, "Resetting worlds..");

		String[] worldSuffix = { "", "_nether", "_the_end" };

		for (String currentSuffix : worldSuffix) {
			String currentWorldFolderName = levelName + currentSuffix;
			File worldFolder = new File(currentWorldFolderName);
			Utils.deleteWorld(worldFolder);
		}

		Utils.deleteWorld(new File("watermlg"));

		plugin.getConfigManager().getInternalConfig().getConfig().set("reset", false);
		plugin.getConfigManager().getInternalConfig().save();

		plugin.getLogger().log(Level.INFO, "World resetting completed in " + (System.currentTimeMillis() - millis) + "ms!");

	}

	public static void prepareReset(boolean shutdownAfter, CommandSender sender) {

		Challenges.getInstance().getLogger().log(Level.INFO, "Preparing server reset..");
		instance.reset = true;

		Challenges.getInstance().getChallengeTimer().stopTimer(sender instanceof Player ? (Player) sender : null, false);

		Challenges.getInstance().getConfigManager().getInternalConfig().getConfig().set("reset", true);
		try {
			Challenges.getInstance().getConfigManager().getInternalConfig().getConfig().set("level-name", Bukkit.getWorlds().get(0).getName());
		} catch (NullPointerException | IndexOutOfBoundsException ignored) { }
		Challenges.getInstance().getConfigManager().getInternalConfig().save();

		Challenges.getInstance().getConfigManager().getBackpackConfig().getConfig().set("team", null);
		Challenges.getInstance().getConfigManager().getBackpackConfig().getConfig().set("players", null);
		Challenges.getInstance().getConfigManager().getBackpackConfig().save();

		Challenges.getInstance().getConfigManager().getPositionConfig().getConfig().set("position", null);
		Challenges.getInstance().getConfigManager().getPositionConfig().save();

		if (!shutdownAfter) return;

		Challenges.getInstance().getLogger().log(Level.INFO, "Resetting server please wait..");

		Bukkit.setWhitelist(true);
		String kickMessage = Translation.SERVER_RESET_KICK.get().replace("%player%", (sender instanceof Player ? ((Player) sender).getDisplayName() : "Console"));

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			currentPlayer.kickPlayer(kickMessage);
			Challenges.getInstance().getPermissionsSystem().setPermissions(currentPlayer, false);
		}

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (Challenges.getInstance().getWorldManager().restartOnReset) {
				Bukkit.spigot().restart();
			} else {
				Bukkit.shutdown();
			}
		}, 10);

	}

	public boolean getReseted() {
		return reseted;
	}
}
