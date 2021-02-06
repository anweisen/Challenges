package net.codingarea.challenges.plugin.lang;

import com.google.gson.*;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.misc.ConsolePrint;
import net.codingarea.challenges.plugin.utils.net.IOUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LanguageLoader {

	public static final String BASE_URL = "https://raw.githubusercontent.com/anweisen/Challenges/development/language/";

	private final JsonParser parser = new JsonParser();

	private File getFile(String name) {
		return new File(Challenges.getInstance().getDataFolder(), "messages/" + name + ".json");
	}

	public void download() {
		try {

			// Create folder
			getFile("Singing Challenge").getParentFile().mkdirs();

			JsonArray languages = parser.parse(IOUtils.toString(BASE_URL + "languages.json")).getAsJsonArray();
			for (JsonElement element : languages) {

				String name = element.getAsString();
				String url = BASE_URL + "files/" + name + ".json";

				JsonObject language = parser.parse(IOUtils.toString(url)).getAsJsonObject();
				File file = getFile(name);

				verifyLanguage(language, file);

			}

		} catch (Exception ex) {
			Challenges.getInstance().getLogger().log(Level.SEVERE, "Could not download languages", ex);
		}
	}

	private void verifyLanguage(@Nonnull JsonObject download, @Nonnull File file) throws IOException {

		if (!file.exists()) {
			file.createNewFile();
			new GsonDocument(download).save(file);
			return;
		}

		JsonObject existing = parser.parse(new FileReader(file)).getAsJsonObject();
		for (Entry<String, JsonElement> entry : download.entrySet()) {
			if (!existing.has(entry.getKey()))
				existing.add(entry.getKey(), entry.getValue());
		}
		new GsonDocument(existing).save(file);

	}

	public void read() {
		try {

			String language = Challenges.getInstance().getConfig().getString("language", "en");
			File file = getFile(language);

			if (!file.exists()) {
				ConsolePrint.unknownLanguage(language);
				language = "en";
				file = getFile(language);
			}

			JsonObject read = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
			for (Entry<String, JsonElement> entry : read.entrySet()) {

				try {
					Message message = Message.valueOf(entry.getKey());
					message.setValue(asString(entry.getValue()));
				} catch (IllegalArgumentException ex) {
				}

			}

		} catch (JsonParseException | IllegalStateException ex) {
			Challenges.getInstance().getLogger().severe("Could not read languages");
		} catch (Exception ex) {
			Challenges.getInstance().getLogger().log(Level.SEVERE, "Could not read languages", ex);
		}
	}

	private String asString(@Nonnull JsonElement element) {
		if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
			return element.getAsString();
		if (element.isJsonArray()) {
			String value = "";
			for (JsonElement current : element.getAsJsonArray()) {
				value += "\n" + asString(current);
			}
			return value;
		}
		return "N/A";
	}

}
