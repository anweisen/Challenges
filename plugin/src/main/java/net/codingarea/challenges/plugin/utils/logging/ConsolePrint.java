package net.codingarea.challenges.plugin.utils.logging;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ConsolePrint {

	private ConsolePrint() {}

	public static void notSpigot() {
		log("");
		log("=============================================================================================");
		log("");
		log("Your server does NOT run an instance of spigot (Your server: " + Bukkit.getVersion() + ")");
		log("Please use an instance of spigot to support all features!");
		log("");
		log("=============================================================================================");
		log("");
	}

	public static void unknownLanguage(@Nullable String language) {
		log("Found unknown language '" + language + "'!");
		log("Defaulting to en (English)");
	}

	public static void unableToGetLanguages() {
		log("No languages found to load");
	}

	public static void alreadyExecutingContentLoader() {
		log("Cannot load contents; Already loading contents");
	}

	public static void noMongoDependencies() {
		log("");
		log("=============================================================================================");
		log("");
		log("Cannot use MongoDB as database without the Challenges-MongoConnector dependency plugin.");
		log("Please add this plugin in order to be able to access a mongodb database.");
		log("");
		log("=============================================================================================");
		log("");
	}

	private static void log(@Nonnull String message) {
		Logger.severe(message);
	}

}
