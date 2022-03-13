package net.codingarea.challenges.plugin.content.loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.collection.IOUtils;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import org.bukkit.Bukkit;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ServiceLoader extends ContentLoader {

	@Override
	protected void load() {
		URL url;
		HttpsURLConnection connection;

		try {
			String endpoint = IOUtils.toString(getGitHubUrl("endpoint.txt"));
			Logger.debug("Fetched service endpoint {}", endpoint);
			if (endpoint.equals("none")) return;

			url = new URL(endpoint);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");

			String request = Document.create()
					.set("address", Document.create()
						.set("ip", Inet4Address.getLocalHost().getHostAddress())
						.set("port", Bukkit.getPort())
					).toString();

			try (OutputStream output = connection.getOutputStream()) {
				output.write(request.getBytes(StandardCharsets.UTF_8));
				output.flush();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = reader.readLine()) != null) {
				response.append(responseLine.trim());
			}

			Document document = Document.parseJson(response.toString());
			Logger.debug("Received response {} from coding-area monitoring services", document);

			if (document.getBoolean("blocked")) {
				Challenges.getInstance().setRequirementsFailed();
				Challenges.getInstance().disablePlugin();
				ConsolePrint.accessBlocked();
			}

			connection.getInputStream().close();
			connection.disconnect();
		} catch (Exception ex) {
			Logger.warn("Could not connect to monitoring services ({})", ex.getClass().getSimpleName());
		}
	}
}
