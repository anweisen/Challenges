package net.codingarea.challenges.plugin.lang.loader;

import com.google.gson.*;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.utils.common.IOUtils;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;
import net.codingarea.challenges.plugin.utils.misc.GsonUtils;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LanguageLoader extends ContentLoader {

	public static final String BASE_URL = "https://raw.githubusercontent.com/anweisen/Challenges/development/language/";
	public static final String DEFAULT_LANGUAGE = "en";

	private static final JsonParser parser = new JsonParser();
	private static volatile boolean loaded = false;

	@Override
	protected void load() {
		download();
		read();
	}

	private void download() {
		try {

			JsonArray languages = parser.parse(IOUtils.toString(BASE_URL + "languages.json")).getAsJsonArray();
			for (JsonElement element : languages) {
				try {
					String name = element.getAsString();
					String url = BASE_URL + "files/" + name + ".json";

					JsonObject language = parser.parse(IOUtils.toString(url)).getAsJsonObject();
					File file = getFile(name, "json");

					verifyLanguage(language, file);
				} catch (Exception ex) {
					Logger.severe("Could not download language for " + element + ". " + ex.getClass().getSimpleName() + ": " + ex.getMessage());
				}
			}

		} catch (Exception ex) {
			Logger.severe("Could not download languages", ex);
		}
	}

	private void verifyLanguage(@Nonnull JsonObject download, @Nonnull File file) throws IOException {

		if (!file.exists()) {
			FileUtils.createFilesIfNecessary(file);
			new GsonDocument(download).save(file);
			return;
		}

		JsonObject existing = parser.parse(FileUtils.newBufferedReader(file)).getAsJsonObject();
		for (Entry<String, JsonElement> entry : download.entrySet()) {
			if (!existing.has(entry.getKey()))
				existing.add(entry.getKey(), entry.getValue());
		}
		new GsonDocument(existing).save(file);

	}

	private void read() {
		try {

			String language = Challenges.getInstance().getConfigDocument().getString("language", DEFAULT_LANGUAGE);
			File file = getFile(language, "json");

			if (!file.exists()) {
				if (language.equalsIgnoreCase(DEFAULT_LANGUAGE)) {
					ConsolePrint.unableToGetLanguages();
					return;
				}
				ConsolePrint.unknownLanguage(language);
				language = DEFAULT_LANGUAGE;
				file = getFile(language, "json");
				if (!file.exists()) {
					ConsolePrint.unableToGetLanguages();
					return;
				}
			}

			JsonObject read = new JsonParser().parse(FileUtils.newBufferedReader(file)).getAsJsonObject();
			for (Entry<String, JsonElement> entry : read.entrySet()) {
				Message message = Message.forName(entry.getKey());
				JsonElement element = entry.getValue();
				if (element.isJsonPrimitive()) {
					message.setValue(element.getAsString());
				} else if (element.isJsonArray()) {
					message.setValue(GsonUtils.convertJsonArrayToStringArray(element.getAsJsonArray()));
				} else {
					Logger.warn("Illegal type '" + element.getClass().getName() + "' for " + message.getName());
				}
			}

			loaded = true;
			Challenges.getInstance().getMenuManager().generateMenus();
			Bukkit.getOnlinePlayers().forEach(Challenges.getInstance().getPlayerInventoryManager()::updateInventoryAuto);

			Logger.info("Successfully loaded language '" + language + "' from config file");

		} catch (Exception ex) {
			Logger.severe("Could not read languages", ex);
		}
	}

	public static boolean isLoaded() {
		return loaded;
	}

}
