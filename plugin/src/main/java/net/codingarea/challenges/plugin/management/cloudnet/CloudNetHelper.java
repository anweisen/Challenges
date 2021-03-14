package net.codingarea.challenges.plugin.management.cloudnet;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.config.Document;
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

	public CloudNetHelper() {
		Document config = Challenges.getInstance().getConfigDocument().getDocument("cloudnet-support");
		startNewService = config.getBoolean("start-new-service");
		nameSupport = config.getBoolean("name-rank-colors");
	}

	public void handleTimerStart() {

		if (!startNewService || !Challenges.getInstance().getServerManager().isFresh()) return;

		try {
			de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper.changeToIngame();
		} catch (NoClassDefFoundError ex) {
			Logger.severe("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
		}
	}

	public void handleTimerPause() {
		try {
			de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper.setState("LOBBY");
			de.dytanic.cloudnet.ext.bridge.BridgeHelper.updateServiceInfo();
		} catch (NoClassDefFoundError ex) {
			Logger.severe("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
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
			Logger.severe("CloudNet Support is enabled, but no CloudNet dependencies could be loaded!");
			return player.getName();
		}
	}

	public boolean isNameSupport() {
		return nameSupport;
	}

}
