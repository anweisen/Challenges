package net.codingarea.challenges.plugin.language.loader;

import com.google.gson.*;
import net.anweisen.utilities.commons.common.IOUtils;
import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.anweisen.utilities.commons.misc.FileUtils;
import net.anweisen.utilities.commons.misc.GsonUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LanguageLoader extends ContentLoader {

	public static final String DEFAULT_LANGUAGE = "en";
	public static final String DIRECT_FILE_PATH = "direct-language-file";

	private static final JsonParser parser = new JsonParser();
	private static volatile boolean loaded = false;

	private String language;

	@Override
	protected void load() {
		Document config = Challenges.getInstance().getConfigDocument();
		if (config.contains(DIRECT_FILE_PATH)) {
			String path = config.getString(DIRECT_FILE_PATH);
			Logger.info("Using direct language file '{}'", path);
			readLanguage(new File(path));
			return;
		}

		loadDefault();
	}

	private void loadDefault() {
		download();
		init();
		read();
	}

	private void init() {

		language = Challenges.getInstance().getConfigDocument().getString("language", DEFAULT_LANGUAGE);
		File file = getMessageFile(language, "json");

		if (!file.exists()) {
			if (language.equalsIgnoreCase(DEFAULT_LANGUAGE)) return;
			ConsolePrint.unknownLanguage(language);
			language = DEFAULT_LANGUAGE;
		}

		Logger.debug("Language '{}' is currently selected", language);

	}

	private void download() {
		try {

			JsonArray languages = parser.parse(IOUtils.toString(getGitHubUrl("language/languages.json"))).getAsJsonArray();
			Logger.debug("Fetched languages {}", languages);
			for (JsonElement element : languages) {
				try {
					String name = element.getAsString();
					String url = getGitHubUrl("language/files/" + name + ".json");

					Document language = Document.parseJson(IOUtils.toString(url));
					File file = getMessageFile(name, "json");

					Logger.debug("Writing language {} to {}", name, file);
					verifyLanguage(language, file, name);
				} catch (Exception ex) {
					ex.printStackTrace();
					Logger.error("Could not download language for {}. {}: {}", element, ex.getClass().getSimpleName(), ex.getMessage());
				}
			}

		} catch (Exception ex) {
			Logger.error("Could not download languages", ex);
		}
	}

	private void verifyLanguage(@Nonnull Document download, @Nonnull File file, @Nonnull String name) throws IOException {
		Document existing = Document.readJsonFile(file);
		FileUtils.createFilesIfNecessary(file);
		download.forEach((key, value) -> {
			if (!existing.contains(key)) {
				Logger.debug("Overwriting message {} in {} with {}", key, name, String.valueOf(value).replace("\"", "§r\""));
				existing.set(key, value);
			}
		});
		existing.saveToFile(file);
	}

	private void read() {
		readLanguage(getMessageFile(language, "json"));
	}

	private void readLanguage(@Nonnull File file) {
		try {

			if (!file.exists()) {
				ConsolePrint.unableToGetLanguages();
				return;
			}

			int messages = 0;
			JsonObject read = parser.parse(FileUtils.newBufferedReader(file)).getAsJsonObject();
			for (Entry<String, JsonElement> entry : read.entrySet()) {
				Message message = Message.forName(entry.getKey());
				JsonElement element = entry.getValue();
				if (element.isJsonPrimitive()) {
					message.setValue(element.getAsString());
					messages++;
				} else if (element.isJsonArray()) {
					message.setValue(GsonUtils.convertJsonArrayToStringArray(element.getAsJsonArray()));
					messages++;
				} else {
					Logger.warn("Illegal type '{}' for {}", element.getClass().getName(), message.getName());
				}
			}

			loaded = true;
			Logger.info("Successfully loaded language '{}' from config file: {} message(s)", language, messages);

		} catch (Exception ex) {
			Logger.error("Could not read languages", ex);
		}
	}

	public static boolean isLoaded() {
		return loaded;
	}


}
