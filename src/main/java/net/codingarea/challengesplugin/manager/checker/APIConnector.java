package net.codingarea.challengesplugin.manager.checker;

import net.codingarea.challengesplugin.utils.commons.IOUtils;
import net.codingarea.challengesplugin.utils.commons.Log;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.3
 */
public class APIConnector {

	public static final String url = "https://api.coding-area.net/challenges/monitoring/";

	public static void startup() {
		try {

			HttpURLConnection connection = IOUtils.createConnection(url);
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			connection.setInstanceFollowRedirects(true);

			connection.setRequestProperty("server-ip", Inet4Address.getLocalHost().getHostAddress());
			connection.setRequestProperty("server-port", Bukkit.getPort() + "");
			connection.setRequestProperty("server-name", Bukkit.getName());
			connection.setRequestProperty("server-version", Bukkit.getVersion());
			connection.setRequestProperty("plugin-version", UpdateChecker.getPluginVersion() + "");

			InputStream input = connection.getInputStream();
			String response = IOUtils.toString(input, StandardCharsets.UTF_8);
			Log.info("coding-area monitoring service answered with " + response);

		} catch (Exception ex) {
			Log.severe("Could not connect with coding-area monitoring service: " + ex.getMessage());
		}
	}

}
