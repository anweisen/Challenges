package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Challenge;
import net.codingarea.challengesplugin.utils.Utils;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class LanguageManager {

	private static Language language = Language.ENGLISH;

	public enum Language {

		GERMAN(1, "de"),
		ENGLISH(0, "en");

		private final int id;
		private final String identifier;

		Language(int id, String identifier) {
			this.id = id;
			this.identifier = identifier;
		}

		public int getID() {
			return id;
		}

		public String getIdentifier() {
			return identifier;
		}

		public static Language getLanguage(String string) {

			if (string == null) return null;

			for (Language currentLanguage : values()) {
				if (currentLanguage.getIdentifier().equalsIgnoreCase(string) || currentLanguage.name().equals(string.toUpperCase())) return currentLanguage;
			}

			return null;
		}

	}

	public static void onLanguageChange() {
		try {
			Challenges.getInstance().getMenuManager().getMenus().clear();
			Challenges.getInstance().getMenuManager().load();
			Challenges.getInstance().getChallengeTimer().getMenu().generateInventory();
			Challenges.getInstance().getChallengeTimer().getMenu().updateInventories();
		} catch (NullPointerException ignored) { }
	}

	public static void setLanguage(Language language) {
		if (language == null) return;
		LanguageManager.language = language;
	}

	public static Language getLanguage() {
		return language;
	}

	public static String syntax(String syntax) {
		return Translation.SYNTAX.get().replace("%cmd%", syntax);
	}

	public static void loadLanguageMessages() {
		try {

			File folder = new File(folder());
			if (!folder.exists()) folder.mkdir();

			File file = new File(folder + "/messages.properties");
			boolean template = !file.exists();

			if (template) {
				loadTemplate(language);
			} else {
				Properties properties = Utils.readProperties(file);
				for (Translation currentTranslation : Translation.values()) {
					String value = properties.getProperty(currentTranslation.name());
					currentTranslation.setCurrentMessage(value);
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void loadTemplate(Language language) throws IOException {

		File destination = new File(folder() + "/messages.properties");
		destination.createNewFile();
		Properties properties = Utils.readProperties(destination);

		for (Translation translation : Translation.values()) {
			String message = translation.getTemplateMessage(language);
			properties.setProperty(translation.name(), message);
			translation.setCurrentMessage(message);
		}

		Utils.saveProperties(properties, destination);

	}

	public static String folder() {
		return Challenges.getInstance().getDataFolder() + "/messages";
	}

}
