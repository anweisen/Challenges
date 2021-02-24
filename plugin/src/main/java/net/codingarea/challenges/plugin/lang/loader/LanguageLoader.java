package net.codingarea.challenges.plugin.lang.loader;

import com.google.gson.*;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.utils.common.IOUtils;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.misc.ConsolePrint;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LanguageLoader extends ContentLoader {

	public static final String BASE_URL = "https://raw.githubusercontent.com/anweisen/Challenges/development/language/";
	public static final String DEFAULT_LANGUAGE = "en";

	private final JsonParser parser = new JsonParser();

	@Override
	protected void load() {
		download();
		read();
	}

	private void download() {
		try {

			JsonArray languages = parser.parse(IOUtils.toString(BASE_URL + "languages.json")).getAsJsonArray();
			for (JsonElement element : languages) {

				String name = element.getAsString();
				String url = BASE_URL + "files/" + name + ".json";

				JsonObject language = parser.parse(IOUtils.toString(url)).getAsJsonObject();
				File file = getFile(name, "json");

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

		JsonObject existing = parser.parse(FileUtils.createReader(file)).getAsJsonObject();
		for (Entry<String, JsonElement> entry : download.entrySet()) {
			if (!existing.has(entry.getKey()))
				existing.add(entry.getKey(), entry.getValue());
		}
		new GsonDocument(existing).save(file);

	}

	private void read() {
		try {

			String language = Challenges.getInstance().getConfig().getString("language", DEFAULT_LANGUAGE);
			File file = getFile(language, "json");

			if (!file.exists()) {
				ConsolePrint.unknownLanguage(language);
				language = DEFAULT_LANGUAGE;
				file = getFile(language, "json");
			}

			JsonObject read = new JsonParser().parse(FileUtils.createReader(file)).getAsJsonObject();
			for (Entry<String, JsonElement> entry : read.entrySet()) {

				try {

					Message message = Message.valueOf(entry.getKey());

					JsonElement element = entry.getValue();
					if (element.isJsonPrimitive()) {
						message.setValue(element.getAsString());
					} else if (element.isJsonArray()) {
						JsonArray array = element.getAsJsonArray();
						String[] value = new String[array.size()];
						for (int i = 0; i < value.length; i++) {
							value[i] = array.get(i).getAsString();
						}
						message.setValue(value);
					}
				} catch (IllegalArgumentException | IllegalStateException ex) {
					// Unknown Message
				}

			}

		} catch (JsonParseException | IllegalStateException ex) {
			Challenges.getInstance().getLogger().severe("Could not read languages");
		} catch (Exception ex) {
			Challenges.getInstance().getLogger().log(Level.SEVERE, "Could not read languages", ex);
		}
	}

}
