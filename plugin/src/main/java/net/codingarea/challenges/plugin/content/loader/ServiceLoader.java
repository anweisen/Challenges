package net.codingarea.challenges.plugin.content.loader;

import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.anweisen.utilities.common.collection.IOUtils;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ServiceLoader extends ContentLoader {

	/**
	 * Pings the domain from the enpoint.txt on github to check if the current IP is blocked.
	 * (https://github.com/anweisen/Challenges/blob/master/endpoint.txt)
	 * Usually localhost but can be changed to block certain servers from using the plugin.
	 *
	 * //-
	 *
	 * As we were working for a long time on this project we didn't want people using OUR plugin as
	 * a gamemode or sorts on THEIR public servers.
	 * We established this security measure to block servers that weren't in possession of developers
	 * that would be able to remove this part of the code.
	 *
	 * This code part is completely disabled and DOESN'T ping any of coding-area servers as of the time this comment is written.
	 * This is done by putting 'localhost' inside the 'enpoint.txt' in the github repository.
	 *
	 * We are aware that many people as they saw this code part though that we'd save ips and other server information
	 * and as words spread many people thought bad about us and this project.
	 *
	 *
	 * We are very sorry about making people unsure or worried about their information and we want to ensure everyone of you about the following things:
	 *
	 * We do NOT want any information of the users of this plugin and we have NO INTEREST in saving ANYTHING inside our databases.
	 * We HAVE NEVER and we WILL NEVER save ANY information about any user of this plugin through this connection.
	 *
	 * //-
	 *
	 * Is planned to be replaced by a non pinging solution in a newer version.
	 *
	 */
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
