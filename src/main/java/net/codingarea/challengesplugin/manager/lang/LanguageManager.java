package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.Challenges;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
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

}
