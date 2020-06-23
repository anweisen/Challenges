package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public enum Translation {

	SYNTAX("Please use §e%cmd%", "Bitte benutze §e%cmd%"),

	TIMER_STARTED("§7Timer was §astarted", "§7Der Timer wurde §agestartet"),
	TIMER_PAUSED("§7Timer was §cpaused", "§7Der Timer wurde §cpausiert"),
	TIMER_RESET("§7Timer was §creset", "§7Der Timer wurde §cresettet"),
	TIMER_SET_TIME("§7Timer has been set to §e%time%", "§7Der Timer wurde auf §e%time% §7gesetzt"),
	TIMER_SET_MODE("§7Timer mode has been set to §e%mode%", "§7Timer modus wurde auf §e%mode% §7gesetzt"),
	TIMER_ALREADY_STARTED("The timer is already §astarted", "Der Timer wurde schon §agestartet"),

	GAMEMODE_CHANGED("Your gamemode was changed to §e%mode%", "Dein Spielmodus wurde auf §e%mode% §7gesetzt"),
	HEALED("§7You've been healed", "§7Du wurdest geheilt"),

	COLLECT_ITEMS_ITEM_REGISTERED("Item §e%new% §7registred (%count%)", "Das Item §e%new% §7wurde registriert (%count%)"),
	COLLECT_DEATHS_DEATH_REGISTERED("Death §e%new% §7registered (%count%)", "Du bist an §e%new% §7gestorben (%count%)"),
	COLLECT_DIAMONDS_NEW_DIAMOND("New diamond §eregistered §7(%count%)", "Neuer Diamant §eregisitriert §7(%count%)"),

	SERVER_RESET_KICK(" \n §8» §cWorldreset \n §8━━━━━━━━━━━━━━━━━━━━━━━ \n §7The server is restarting \n §7Reset requested by §c%player% \n ",
							     " \n §8» §cWeltenreset \n §8━━━━━━━━━━━━━━━━━━━━━━━ \n §7Der Server startet jetzt neu \n §7Resetet durch §c%player% \n "),
	RESET_CONFIRM("§7Please confirm that you want to reset the world with §e/reset confirm", "§7Bitte bestätige dass du die Welten zurücksetzten willst mit §e/reset confirm"),
	PLAYER_DAMAGE("§e%player% §7got §e%damage% §7hearts damage §7by §e%cause%", "§e%player% §7hat durch §e%cause% §e%damage% §7Herzen Schaden bekommen"),

	POSITION_CREATE("§e%player% §7created the position §e%position%", "§e%player% §7hat die Position §e%position% §7erstellt"),
	POSITION_GET("§7The position §e%name% §7is at §e%position%", "§7Die Position §e%name% §7ist bei §e%position%"),

	VILLAGE_TELEPORT("§7You were teleported to a village", "§7Du wurdest zu einem Dorf teleportiert"),
	VILLAGE_TELEPORT_ERROR("§7There was no village found", "§7Es wurde kein Dorf gefunden"),

	BACK("§8« §7Back", "§8« §7Zurück"),
	NEXT("§8» §7Next", "§8» §7Weiter"),

	TIMER_STOPPED_ITEM("§7Timer is §cstopped", "§7Timer ist §cgestoppt"),
	TIMER_STARTED_ITEM("§7Timer is §astarted", "§7Timer ist §agestartet"),
	TIMER_MODE_DOWN_ITEM("§7Timer counting §cdown", "§7Timer zählt §crunter"),
	TIMER_MODE_UP_ITEM("§7Timer counting §aup", "§7Timer zählt §ahoch"),

	SECONDS("seconds", "Sekunden"),
	MINUTES("minutes", "Minuten"),
	HOURS("hours", "Stunden"),

	BACKBACKS_NOT_ACTIVE("§7Backpacks are currently disabled", "§7Rucksäcke sind derzeit ausgeschaltet"),
	BACKBACKS_OPEN("§7You opened the %backpack%", "§7Du hast den %backpack% §7geöffnet"),

	FEATURE_DISABLED("§7This feature is currrently disabled", "§7Diese Funktion ist derzeit deaktiviert"),
	UP_COMMAND_TELEPORT("§7You were teleported to the top", "§7Du wurdest nach oben teleportiert"),

	CHALLENGE_END_TIMER_END( " \n§c§lDie Challenge wurde beendet! \n§c§lGewinner: §7%winner% \n§c§lSeed: §7%seed% \n§c§lZeit benötigt: §7%time% \n ", " \n§c§lDie Challenge wurde beendet! \n§c§lGewinner: §7%winner% \n§c§lSeed: §7%seed% \n§c§lZeit benötigt: §7%time% \n "),
	CHALLENGE_END_GOAL_REACHED(" \n§c§lDie Challenge wurde beendet! \n§c§lSeed: §7%seed% \n§c§lTime needed: §7%time% \n ", " \n§c§lDie Challenge wurde beendet! \n§c§lSeed: §7%seed% \n§c§lZeit benötigt: §7%time% \n "),
	CHALLENGE_END_GOAL_FAILED(" \n§c§lDie Challenge wurde beendet! \n§c§lSeed: §7%seed% \n§c§lTime needed: §7%time% \n ", " \n§c§lDie Challenge wurde beendet! \n§c§lSeed: §7%seed% \n§c§lZeit benötigt: §7%time% \n ");

	private final String[] messages;

	Translation(String... messages) {
		this.messages = messages;
	}

	public String get(Language language) {
		try {
			return "§7" + messages[language.getID()];
		} catch (Exception ignored) {
			return "§4Translation: §4§l" + this.name() + " §4Language: §4§l" + language.name();
		}
	}

	public String get() {
		return get(LanguageManager.getLanguage());
	}

}
