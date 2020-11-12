package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Level;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-03-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class WorldManager {

	private static WorldManager instance;

	private boolean restartOnReset;
	private boolean reseted;

	private final Challenges plugin;

	private String levelName;
	private boolean reset;

	private World challengesWorld;
	private boolean worldIsInUse;

	public WorldManager(Challenges plugin) {
		instance = this;
		this.plugin = plugin;
	}

	public void loadWorld() {
		challengesWorld = Bukkit.createWorld(new WorldCreator("challenges-extra").type(WorldType.FLAT).generateStructures(false));
		challengesWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
	}

	public World getChallengesWorld() {
		return challengesWorld;
	}

	public void loadSettings() {

		reset = plugin.getConfigManager().getInternalConfig().toFileConfig().getBoolean("reset");
		levelName = plugin.getConfigManager().getInternalConfig().toFileConfig().getString("level-name");

		if (levelName == null) levelName = "world";

		restartOnReset = plugin.getConfig().getBoolean("restart-on-reset");

	}

	public void resetWorlds() {

		if (!reset) return;

		long millis = System.currentTimeMillis();
		plugin.getLogger().log(Level.INFO, "Resetting worlds..");

		String[] worlds = { levelName, levelName + "_nether", levelName + "_the_end", "watermlg" };

		for (String currentWorld : worlds) {
			File worldFolder = new File(currentWorld);
			Utils.deleteWorld(worldFolder);
		}

		try {
			FileUtils.cleanDirectory(new File(Challenges.getInstance().getChallengeManager().getSettingsFolder()));
		} catch (Exception ignored) { }

		plugin.getConfigManager().getInternalConfig().toFileConfig().set("reset", false);
		plugin.getConfigManager().getInternalConfig().save();

		plugin.getLogger().log(Level.INFO, "World resetting completed in " + (System.currentTimeMillis() - millis) + "ms!");

	}

	public static void prepareReset(@Nullable CommandSender sender) {

		Challenges.getInstance().getLogger().log(Level.INFO, "Preparing server reset..");
		instance.reseted = true;

		Challenges.getInstance().getChallengeTimer().stopTimer(sender instanceof Player ? (Player) sender : null, false);
		Challenges.getInstance().getChallengeTimer().stop();

		try {
			Challenges.getInstance().getConfigManager().getInternalConfig().toFileConfig().set("timer.seconds", 0);
			Challenges.getInstance().getConfigManager().getInternalConfig().toFileConfig().set("reset", true);
			Challenges.getInstance().getConfigManager().getInternalConfig().toFileConfig().set("level-name", Bukkit.getWorlds().get(0).getName());
		} catch (NullPointerException | IndexOutOfBoundsException ignored) { }
		Challenges.getInstance().getChallengeTimer().setMaxSeconds(0);
		Challenges.getInstance().getConfigManager().getInternalConfig().save();
		Challenges.getInstance().getConfigManager().reset();

		Challenges.getInstance().getLogger().log(Level.INFO, "Resetting server please wait..");

		Bukkit.setWhitelist(true);
		String kickMessage = Translation.SERVER_RESET_KICK.get().replace("%player%", (sender instanceof Player ? ((Player) sender).getDisplayName() : "Console"));

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			currentPlayer.kickPlayer(kickMessage);
		}

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (instance.restartOnReset) {
				try {
					Bukkit.spigot().restart();
				} catch (NoSuchMethodError ignored) {
					Log.severe("Could not restart server. Stopping server");
					Bukkit.shutdown();
				}
			} else {
				Bukkit.shutdown();
			}
		}, 10);

	}

	public boolean getReseted() {
		return reseted;
	}

	public boolean worldIsInUse() {
		return worldIsInUse;
	}

	public void setWorldIsInUse(boolean worldIsInUse) {
		this.worldIsInUse = worldIsInUse;
	}

	public static WorldManager getInstance() {
		return instance;
	}

	public static boolean isInExtraWorld(Player player) {
		return isInExtraWorld(player.getLocation());
	}

	public static boolean isInExtraWorld(Location location) {
		return isInExtraWorld(location.getWorld());
	}

	public static boolean isInExtraWorld(World world) {
		return world.getName().equals(getInstance().getChallengesWorld().getName());
	}

}
