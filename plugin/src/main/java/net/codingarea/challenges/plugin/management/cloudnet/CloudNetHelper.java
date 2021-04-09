package net.codingarea.challenges.plugin.management.cloudnet;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class CloudNetHelper {

	private final boolean startNewService;
	private final boolean nameSupport;
	private final boolean resetToLobby;

	public CloudNetHelper() {
		Document config = Challenges.getInstance().getConfigDocument().getDocument("cloudnet-support");
		startNewService = config.getBoolean("start-new-service");
		nameSupport = config.getBoolean("name-rank-colors");
		resetToLobby = config.getBoolean("reset-to-lobby");

		ChallengeAPI.registerScheduler(this);
	}

	@TimerTask(status = TimerStatus.RUNNING, async = false)
	public void handleTimerStart() {

		if (!startNewService || !ChallengeAPI.isFresh()) return;

		try {
			de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper.changeToIngame();
		} catch (NoClassDefFoundError ex) {
			Logger.error("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
		}
	}

	@TimerTask(status = TimerStatus.PAUSED, async = false)
	public void handleTimerPause() {

		if (!resetToLobby || !startNewService) return;

		try {
			de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper.setState("LOBBY");
			de.dytanic.cloudnet.ext.bridge.BridgeHelper.updateServiceInfo();
		} catch (NoClassDefFoundError ex) {
			Logger.error("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
		}
	}

	@Nonnull
	public String getColoredName(@Nonnull Player player) {
		try {
			de.dytanic.cloudnet.driver.CloudNetDriver driver = de.dytanic.cloudnet.driver.CloudNetDriver.getInstance();
			de.dytanic.cloudnet.driver.permission.IPermissionUser user = driver.getPermissionManagement().getUser(player.getUniqueId());
			if (user == null) return player.getName();

			de.dytanic.cloudnet.driver.permission.IPermissionGroup group = driver.getPermissionManagement().getHighestPermissionGroup(user);
			String color = group.getColor();
			return color.replace('&', '§') + player.getName();
		} catch (NoClassDefFoundError ex) {
			Logger.error("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
			return player.getName();
		}
	}

	public boolean isNameSupport() {
		return nameSupport;
	}

}
