package net.codingarea.challenges.plugin.management.server;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.wrapper.FileDocumentWrapper;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class WorldManager {

	private boolean shutdownBecauseOfReset = false;

	private final boolean restartOnReset;
	private final String levelName;

	private World world;
	private boolean worldIsInUse;

	public WorldManager() {
		Document config = Challenges.getInstance().getConfigDocument();
		restartOnReset = config.getBoolean("restart-on-reset");
		levelName = config.getString("level-name", "world");
	}

	public void prepareWorldReset(@Nullable CommandSender requestedBy) {

		shutdownBecauseOfReset = true;

		// Stop all tasks to prevent them from overwriting configs
		Challenges.getInstance().getScheduler().stop();

		FileDocumentWrapper sessionConfig = Challenges.getInstance().getConfigManager().getSessionConfig();
		sessionConfig.clear();
		sessionConfig.set("reset", true);
		try {
			sessionConfig.set("level-name", Bukkit.getWorlds().get(0).getName());
		} catch (Exception ex) {
		}
		sessionConfig.save();

		String requester = requestedBy instanceof Player ? ((Player)requestedBy).getDisplayName() : "§4§lConsole";
		String kickMessage = Message.SERVER_RESET.asString(requester);
		Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(kickMessage));

		stopServer();

	}

	public void enable()  {
		loadWorld();
		executeWorldResetIfNecessary();
	}

	private void loadWorld() {
		world = Bukkit.createWorld(new WorldCreator("challenges-extra").type(WorldType.FLAT).generateStructures(false));
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
	}

	private void executeWorldResetIfNecessary() {
		if (Challenges.getInstance().getConfigManager().getSessionConfig().getBoolean("reset"))
			executeWorldReset();
	}

	public void executeWorldReset() {

		String[] worlds = { levelName, levelName + "_nether", levelName + "_the_end", "challenges-extra" };
		for (String world : worlds) {
			File folder = new File(world);
			FileUtils.deleteWorldFolder(folder);
		}

		FileDocumentWrapper sessionConfig = Challenges.getInstance().getConfigManager().getSessionConfig();
		sessionConfig.set("reset", false);
		sessionConfig.save();

	}

	private void stopServer() {
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

	public boolean isShutdownBecauseOfReset() {
		return shutdownBecauseOfReset;
	}

	public boolean isWorldInUse() {
		return worldIsInUse;
	}

	public void setWorldIsInUse(boolean worldIsInUse) {
		this.worldIsInUse = worldIsInUse;
	}

	public World getExtraWorld() {
		return world;
	}
}
