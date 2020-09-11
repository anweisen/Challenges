package net.codingarea.challengesplugin.manager;

import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;
import net.codingarea.challengesplugin.Challenges;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-04-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class CloudNetManager {

	public final boolean cloudnetSupport;
	private boolean startNewServer;
	private static boolean wasIngame; // Why static? - So the plugin keeps the setting when reloading the server

	public CloudNetManager(boolean cloudnetSupport) {
		this.cloudnetSupport = cloudnetSupport;
	}

	public void setIngame() {

		try {
			if (cloudnetSupport) {
				if (!wasIngame && startNewServer) {
					BukkitCloudNetHelper.changeToIngame();
				} else {
					BukkitCloudNetHelper.setState("INGAME");
				}
				setWasIngame(true);
				BridgeHelper.updateServiceInfo();
			}
		} catch (NoClassDefFoundError ignored) { }

	}

	public void setLobby() {

		try {
			if (cloudnetSupport) {
				BukkitCloudNetHelper.setState("LOBBY");
				BridgeHelper.updateServiceInfo();
			}
		} catch (NoClassDefFoundError ignored) { }

	}

	public static void loadIngameFromConfig() {
		Challenges.getInstance().getCloudnetManager().startNewServer = Challenges.getInstance().getConfig().getBoolean("start-new-server");
		if (Challenges.getInstance().getConfigManager().getInternalConfig() == null) return;
		wasIngame = Challenges.getInstance().getConfigManager().getInternalConfig().toFileConfig().getBoolean("ingame");
	}

	public static boolean wasAlreadyIngame() {
		return wasIngame;
	}

	public static void setWasIngame(boolean wasIngame) {
		CloudNetManager.wasIngame = wasIngame;
		if (Challenges.getInstance().getConfigManager().getInternalConfig() == null) return;
		Challenges.getInstance().getConfigManager().getInternalConfig().toFileConfig().set("ingame", wasIngame);
		Challenges.getInstance().getConfigManager().getInternalConfig().save();
	}
}
