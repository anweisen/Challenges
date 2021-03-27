package net.codingarea.challenges.plugin.management.server;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.wrapper.FileDocumentWrapper;
import net.anweisen.utilities.commons.misc.FileUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class WorldManager {

	public static class WorldSettings {

		private boolean placeBlocks = false;
		private boolean destroyBlocks = false;

		public void setDestroyBlocks(boolean destroyBlocks) {
			this.destroyBlocks = destroyBlocks;
		}

		public void setPlaceBlocks(boolean placeBlocks) {
			this.placeBlocks = placeBlocks;
		}

		public boolean isDestroyBlocks() {
			return destroyBlocks;
		}

		public boolean isPlaceBlocks() {
			return placeBlocks;
		}

	}

	private boolean shutdownBecauseOfReset = false;

	private final boolean restartOnReset;
	private final boolean enableFreshReset;
	private final String levelName;

	private WorldSettings settings = new WorldSettings();
	private World world;
	private boolean worldIsInUse;

	public WorldManager() {
		Document pluginConfig = Challenges.getInstance().getConfigDocument();
		restartOnReset = pluginConfig.getBoolean("restart-on-reset");
		enableFreshReset = pluginConfig.getBoolean("enable-fresh-reset");

		Document sessionConfig = Challenges.getInstance().getConfigManager().getSessionConfig();
		levelName = sessionConfig.getString("level-name", "world");
	}

	public void load()  {
		executeWorldResetIfNecessary();
	}

	public void enable() {
		loadExtraWorld();
	}

	public void prepareWorldReset(@Nullable CommandSender requestedBy) {

		shutdownBecauseOfReset = true;

		// Stop all tasks to prevent them from overwriting configs
		Challenges.getInstance().getScheduler().stop();

		resetConfigs();

		String requester = requestedBy instanceof Player ? NameHelper.getName((Player) requestedBy) : "§4§lConsole";
		String kickMessage = Message.forName("server-reset").asString(requester);
		Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(kickMessage));

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), this::stopServerNow, 3);

	}

	private void resetConfigs() {

		FileDocumentWrapper sessionConfig = Challenges.getInstance().getConfigManager().getSessionConfig();
		sessionConfig.clear();
		sessionConfig.set("reset", true);
		try {
			sessionConfig.set("level-name", Bukkit.getWorlds().get(0).getName());
		} catch (Exception ex) {
		}
		sessionConfig.save();

		FileDocumentWrapper gamestateConfig = Challenges.getInstance().getConfigManager().getGamestateConfig();
		gamestateConfig.clear();
		gamestateConfig.save();

	}

	private void loadExtraWorld() {
		if (!Challenges.getInstance().isReload())
			deleteWorld("challenges-extra");

		world = new WorldCreator("challenges-extra").type(WorldType.FLAT).generateStructures(false).createWorld();
		if (world == null) return;
		world.setSpawnFlags(false, false);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.DISABLE_RAIDS, true);
		world.setGameRule(GameRule.MOB_GRIEFING, false);
	}

	private void executeWorldResetIfNecessary() {
		if (Challenges.getInstance().getConfigManager().getSessionConfig().getBoolean("reset"))
			executeWorldReset();
	}

	public void executeWorldReset() {

		Logger.info("Deleting worlds..");

		String[] worlds = { levelName, levelName + "_nether", levelName + "_the_end" };
		for (String world : worlds) {
			deleteWorld(world);
		}

		FileDocumentWrapper sessionConfig = Challenges.getInstance().getConfigManager().getSessionConfig();
		sessionConfig.set("reset", false);
		sessionConfig.save();

	}

	private void deleteWorld(@Nonnull String name) {
		File folder = new File(name);
		FileUtils.deleteWorldFolder(folder);
		Logger.info("Deleted world " + name);
	}

	private void stopServerNow() {
		if (!restartOnReset) {
			Bukkit.shutdown();
			return;
		}

		try {
			Bukkit.spigot().restart();
		} catch (NoSuchMethodError ex) {
			Bukkit.shutdown();
		}
	}

	public boolean isEnableFreshReset() {
		return enableFreshReset;
	}

	public boolean isShutdownBecauseOfReset() {
		return shutdownBecauseOfReset;
	}

	public boolean isWorldInUse() {
		return worldIsInUse;
	}

	public void setWorldIsInUse(boolean worldIsInUse) {
		if (!worldIsInUse) settings = new WorldSettings();
		this.worldIsInUse = worldIsInUse;
	}

	@Nonnull
	public World getExtraWorld() {
		return world;
	}

	@Nonnull
	public WorldSettings getSettings() {
		return settings;
	}

}
