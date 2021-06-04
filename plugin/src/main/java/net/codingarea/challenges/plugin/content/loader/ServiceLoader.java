package net.codingarea.challenges.plugin.content.loader;

import net.anweisen.utilities.commons.common.IOUtils;
import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ServiceLoader extends ContentLoader {

	@Override
	protected void load() {
		try {

			String endpoint = IOUtils.toString(getGitHubUrl("endpoint.txt"));
			Logger.debug("Fetched service endpoint {}", endpoint);
			if (endpoint.equals("none")) return;

			HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(2500);
			connection.setReadTimeout(1000);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");

			String request = new GsonDocument()
					.set("address", new GsonDocument()
						.set("ip", Inet4Address.getLocalHost().getHostAddress())
						.set("port", Bukkit.getPort())
					).set("plugin", new GsonDocument()
						.set("version", Challenges.getInstance().getVersion().toString())
						.set("language", Challenges.getInstance().getConfigDocument().getString("language"))
						.set("database", Challenges.getInstance().getConfigDocument().getString("database.type"))
						.set("cloud-support", Challenges.getInstance().getConfigDocument().getString("cloud-support.type"))
					).set("server", new GsonDocument()
						.set("bound-ip", Bukkit.getIp())
						.set("name", Bukkit.getName())
						.set("version", Bukkit.getVersion())
						.set("minecraft-version", Challenges.getInstance().getServerVersion().toString())
						.set("bukkit-version", Bukkit.getBukkitVersion())
						.set("whitelist", new GsonDocument()
							.set("enabled", Bukkit.hasWhitelist())
							.set("size", Bukkit.getWhitelistedPlayers().size())
						).set("motd", Bukkit.getMotd())
						.set("plugins", Arrays.stream(Bukkit.getPluginManager().getPlugins())
								.map(Plugin::getDescription)
								.map(description -> new GsonDocument()
										.set("name", description.getName())
										.set("version", description.getVersion())
										.set("authors", description.getAuthors())
										.set("contributors", description.getContributors())
								).collect(Collectors.toList()))
					).toString();

			OutputStream output = connection.getOutputStream();
			output.write(request.getBytes(StandardCharsets.UTF_8));
			output.flush();
			output.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = reader.readLine()) != null) {
				response.append(responseLine.trim());
			}

			Document document = new GsonDocument(response.toString());
			Logger.debug("Received response {} from coding-area monitoring services", document);

			if (document.getBoolean("blocked")) {
				Challenges.getInstance().setValidationFailed();
				Challenges.getInstance().disable();
				ConsolePrint.accessBlocked();
			}

		} catch (Exception ex) {
			Logger.warn("Could not connect to monitoring services ({})", ex.getClass().getSimpleName());
		}
	}

}
