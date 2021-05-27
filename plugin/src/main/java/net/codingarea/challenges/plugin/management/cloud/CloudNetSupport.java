package net.codingarea.challenges.plugin.management.cloud;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class CloudNetSupport implements CloudSupport {

	@Nonnull
	@Override
	public String getColoredName(@Nonnull Player player) {
		return getColoredName(player.getUniqueId());
	}

	@Nonnull
	@Override
	public String getColoredName(@Nonnull UUID uuid) {
		IPermissionManagement management = CloudNetDriver.getInstance().getPermissionManagement();
		IPermissionUser user = management.getUser(uuid);
		IPermissionGroup group = management.getHighestPermissionGroup(user);
		String color = group.getColor();
		return color.replace('&', '§') + user.getName();
	}

	@Override
	public boolean hasNameFor(@Nonnull UUID uuid) {
		return CloudNetDriver.getInstance().getPermissionManagement().getUser(uuid) != null;
	}

	@Override
	public void startNewService() {
		BukkitCloudNetHelper.changeToIngame();
		BridgeHelper.updateServiceInfo();
	}

	@Override
	public void setIngame() {
		BukkitCloudNetHelper.setState("INGAME");
		BridgeHelper.updateServiceInfo();
	}

	@Override
	public void setLobby() {
		BukkitCloudNetHelper.setState("LOBBY");
		BridgeHelper.updateServiceInfo();
	}

}
