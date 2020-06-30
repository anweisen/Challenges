package net.codingarea.challengesplugin.manager.permissions;

import net.codingarea.challengesplugin.Challenges;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.codingarea.challengesplugin.manager.lang.Translation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-05-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class MasterSystem {

	/*
	 * You found the master system so you can use it now lol you just have to find out how to activate it :)
	 */

	private final Challenges plugin;

	private final boolean enabled;

	private List<String> permissions;
	private final String masterChangedMassage;
	private final String masterSetMessage;
	private Player currentMaster;

	public MasterSystem(Challenges plugin, String masterChangedMassage, String masterSetMessage, boolean enabled) {
		this.plugin = plugin;
		permissions = new ArrayList<>();
		this.masterChangedMassage = masterChangedMassage;
		this.masterSetMessage = masterSetMessage;
		this.enabled = enabled;
	}

	public void loadPermissions() {
		permissions = plugin.getConfig().getStringList("master-permissions");
	}

	public void onEnable() {
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			plugin.getPermissionsSystem().setPermissions(currentPlayer, false);
		}
		setRandomMaster(null);
	}

	public void handlePlayerDisconnect(Player player) {

		if (!enabled) return;

		if (currentMaster.equals(player)) {
			try {
				plugin.getPlayerSettingsManager().save(player.getName());
				player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.SAVE_CONFIG_SUCCESS.get());
			} catch (SQLException ex) {
				player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.MYSQL_ERROR.get().replace("%error%", ex.getMessage()));
			}
			setRandomMaster(player);
		}

	}

	public void setRandomMaster(Player notMaster) {

		List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
		if (notMaster != null) list.remove(notMaster);
		if (list.size() == 0) {
			currentMaster = null;
			return;
		}
		Collections.shuffle(list);
		changeMasterTo(list.get(0));

	}

	public void handlePlayerConnect(Player player) {

		if (!enabled) return;

		if (currentMaster == null) {
			changeMasterTo(player);

			try {
				if (plugin.getPlayerSettingsManager().isEnabled() && plugin.getPlayerSettingsManager().load(player.getName())) {
					plugin.getMenuManager().loadMenus();
					player.sendMessage(plugin.getStringManager().CHALLENGE_PREFIX + Translation.LOAD_CONFIG_SUCCESS.get());
				}
			} catch (SQLException ignored) { }

		}

	}

	public void changeMasterTo(Player master) {

		if (master == null) return;

		if (currentMaster != null) {

			if (currentMaster.equals(master)) return;

			setPermissions(currentMaster, false);

			if (masterChangedMassage != null) {
				Bukkit.broadcastMessage(plugin.getStringManager().MASTER_PREFIX
						+ masterChangedMassage.replace("%old%", currentMaster.getName()).replace("%new%", master.getName()));
			}

		} else {

			if (masterSetMessage != null) {
				Bukkit.broadcastMessage(plugin.getStringManager().MASTER_PREFIX
						+ masterSetMessage.replace("%player%", master.getName()));
			}

		}

		setPermissions(master, true);
		Bukkit.getScheduler().runTaskLater(plugin, () -> master.sendMessage(Translation.MASTER_COMMANDS.get()), 1);

		currentMaster = master;

	}

	public void setPermissions(Player player, boolean give) {

		for (String currentPermission : permissions) {

			player.addAttachment(Challenges.getInstance(), currentPermission, give);

			try {

				IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(player.getUniqueId());

				if (permissionUser == null) return;
				Wrapper.getInstance().getServiceId().getTaskName();
				if (give) {
					permissionUser.addPermission(Wrapper.getInstance().getServiceId().getTaskName(), currentPermission);
				} else {
					permissionUser.removePermission(Wrapper.getInstance().getServiceId().getTaskName(), currentPermission);
				}

				CloudNetDriver.getInstance().getPermissionManagement().updateUser(permissionUser);

			} catch (NoClassDefFoundError ignored) { }

		}
		player.updateCommands();

	}

	public boolean isEnabled() {
		return enabled;
	}

	public List<String> getPermissions() {
		return permissions;
	}

}