package net.codingarea.challengesplugin.manager.checker;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.commons.IOUtils;
import net.codingarea.challengesplugin.utils.commons.Log;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class BlacklistChecker {

	private static boolean blocked = false;

	private static String getURL(@Nonnull String address) {
		return "http://api.coding-area.net/challenges/blacklist?ip=" + address;
	}

	public static void updateStatus() {
		try {
			URL url = new URL(getURL(Inet4Address.getLocalHost().getHostAddress()));
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			InputStream input = connection.getInputStream();
			String response = IOUtils.toString(input, StandardCharsets.UTF_8);
			blocked = Boolean.parseBoolean(response);
		} catch (Exception ex) {
			Log.severe("Could connect with coding-area api servers.");
		}
	}

	public static boolean isBlocked() {
		return blocked;
	}

	public static boolean validate() {

		if (!blocked) return true;

		Log.severe(" ");
		Log.severe("██████╗░██╗░░░░░░█████╗░░█████╗░██╗░░██╗███████╗██████╗░");
		Log.severe("██╔══██╗██║░░░░░██╔══██╗██╔══██╗██║░██╔╝██╔════╝██╔══██╗");
		Log.severe("██████╦╝██║░░░░░██║░░██║██║░░╚═╝█████═╝░█████╗░░██║░░██║");
		Log.severe("██╔══██╗██║░░░░░██║░░██║██║░░██╗██╔═██╗░██╔══╝░░██║░░██║");
		Log.severe("██████╦╝███████╗╚█████╔╝╚█████╔╝██║░╚██╗███████╗██████╔╝");
		Log.severe("╚═════╝░╚══════╝░╚════╝░░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░");
		Log.severe(" ");
		Log.severe("Your server's access is blocked.");
		Log.severe("For more information visit our discord server: https://coding-area.net/go/discord");
		Log.severe(" ");

		Bukkit.getPluginManager().disablePlugin(Challenges.getInstance());

		return false;

	}

}
