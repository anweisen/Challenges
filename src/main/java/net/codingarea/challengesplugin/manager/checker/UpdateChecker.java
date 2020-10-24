package net.codingarea.challengesplugin.manager.checker;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.commons.IOUtils;
import net.codingarea.challengesplugin.utils.commons.Log;
import org.bukkit.util.NumberConversions;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class UpdateChecker {

	private static boolean configIsNewestVersion,
						   pluginIsNewestVersion;

	public static void updateStatus() {
		try {

			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=80548");
			InputStream input = url.openStream();
			String response = IOUtils.toString(input, StandardCharsets.UTF_8);

			double latestVersion = NumberConversions.toDouble(response);
			double pluginVersion = NumberConversions.toDouble(Challenges.getInstance().getDescription().getVersion());
			double configVersion = NumberConversions.toDouble(Challenges.getInstance().getConfig().getString("config-version"));

			pluginIsNewestVersion = pluginVersion >= latestVersion;
			configIsNewestVersion = configVersion >= pluginVersion;

			if (!pluginIsNewestVersion) {
				Log.warning("There is a new version available. The newest version is " + latestVersion + ", you have " + pluginVersion + ".");
				Log.warning("Download the latest version at https://www.spigotmc.org/resources/80548");
			}
			if (!configIsNewestVersion) {
				Log.warning("This plugin had an update (v" + latestVersion + "). There may be some changes to the config.yml.");
				Log.warning("Delete the config.yml file to see whats new inside.");
				Log.warning("To get rid of this message, you can manually update the config-version as well.");
			}

		} catch (Exception ex) {
			Log.warning("Unable to check versions..");
		}
	}

	public static boolean configIsNewestVersion() {
		return configIsNewestVersion;
	}

	public static boolean pluginIsNewestVersion() {
		return pluginIsNewestVersion;
	}
}
