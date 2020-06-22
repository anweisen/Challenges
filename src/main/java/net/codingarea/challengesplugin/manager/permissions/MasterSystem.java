package net.codingarea.challengesplugin.manager.permissions;

import net.codingarea.challengesplugin.Challenges;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.wrapper.Wrapper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-05-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class MasterSystem {

	/*
	 * Config section:
	 * # This will toggle a system where the first user joined has the permissions to edit the challenges, reset the world, start/stop the timer and everything
	 * # you add to the permission list. When the master leaves, a random user will get promoted to the master
	 * master-system: false
	 * master-changed-message: '§7The master has changed from §e%old% §7to §e%new%'
	 * master-set-message: '§7The new master is §e%player%'
	 * master-permissions:
     * - 'challenges.gui'
     * - 'challenges.timer'
     * - 'challenges.start'
     * - 'challenges.reset'
     * - 'challenges.setmaster'
     * - 'challenges.heal'
     * - 'challenges.gamemode'
     * - 'challenges.teleport'
     * - 'challenges.village'
     * - 'minecraft.command.locate'
     * - 'minecraft.command.weather'
     * - 'minecraft.command.time'
     * - 'minecraft.command.teleport'
     * - 'minecraft.command.spawnpoint'
     * - 'minecraft.command.setworldspawn'
	 */

	private final Challenges plugin;

	@Getter private final boolean enabled;

	@Getter private List<String> permissions;
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

}