package net.codingarea.challengesplugin.manager;

import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.bukkit.BukkitCloudNetHelper;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-04-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class CloudNetManager {

	public final boolean cloudnetSupport;
	private static boolean wasIngame;

	public CloudNetManager(boolean cloudnetSupport) {
		this.cloudnetSupport = cloudnetSupport;
	}

	public void setIngame() {

		try {
			if (cloudnetSupport) {
				if (!wasIngame) {
					BukkitCloudNetHelper.changeToIngame();
					wasIngame = true;
				} else
					BukkitCloudNetHelper.setState("INGAME");
				BridgeHelper.updateServiceInfo();
			}

		} catch (NoClassDefFoundError ex) {
			ex.printStackTrace();
		}

	}

	public void setLobby() {

		try {
			if (cloudnetSupport) {
				BukkitCloudNetHelper.setState("LOBBY");
				BridgeHelper.updateServiceInfo();
			}
		} catch (NoClassDefFoundError ex) {
			ex.printStackTrace();
		}

	}

	public static boolean wasAlreadyIngame() {
		return wasIngame;
	}

}
