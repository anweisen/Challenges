package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public enum Translation {

	SYNTAX("§7Please use §e%cmd%", "Bitte benutze §e%cmd%"),
	UNDEFINED("undefined", "Undefiniert"),

	STATS_CHALLENGES_PLAYED("Challenges played", "Challenges gespielt"),
	STATS_CHALLENGES_WON("Challenges won", "Challenges gewonnen"),
	STATS_DAMAGE_DEALT("Damage dealt", "Schaden gemacht"),
	STATS_DAMAGE_TAKEN("Damage taken", "Schaden bekommen"),
	STATS_BLOCKS_MINED("Blocks mined", "Blöcke abbgebaut"),
	STATS_KILLS("Mobs killed", "Mobs getötet"),
	STATS_ITEMS_COLLECTED("Items collected", "Items aufgesammelt"),
	STATS_TIMES_JUMPED("Times jumped", "Gesprungen"),
	STATS_TIME_SNEAKED("Time sneaked", "Zeit gesneaked"),

	TIMER_NOT_STARTED("§7The timer is §cnot started", "§7Der Timer ist §cnicht gestartet"),
	TIMER_STARTED("§7Timer was §astarted", "§7Der Timer wurde §agestartet"),
	TIMER_PAUSED("§7Timer was §cpaused", "§7Der Timer wurde §cpausiert"),
	TIMER_RESET("§7Timer was §creset", "§7Der Timer wurde §cresettet"),
	TIMER_SET_TIME("§7Timer has been set to §e%time%", "§7Der Timer wurde auf §e%time% §7gesetzt"),
	TIMER_SET_MODE("§7Timer mode has been set to §e%mode%", "§7Timer modus wurde auf §e%mode% §7gesetzt"),
	TIMER_ALREADY_STARTED("The timer is already §astarted", "Der Timer wurde schon §agestartet"),
	TIMER_STOPPED_ITEM("§7Timer is §cstopped", "§7Timer ist §cgestoppt"),
	TIMER_STARTED_ITEM("§7Timer is §astarted", "§7Timer ist §agestartet"),
	TIMER_MODE_DOWN_ITEM("§7Timer counting §cdown", "§7Timer zählt §crunter"),
	TIMER_MODE_UP_ITEM("§7Timer counting §aup", "§7Timer zählt §ahoch"),
	TIMER_SHOW("§7The timer is now §avisible", "§7Der Timer ist nun §asichtbar"),
	TIMER_HIDE("§7The timer is §cno longer §7visible", "§7Der Timer ist §cnicht mehr §7sichtbar"),
	TIMER_ALREADY_HIDE("§7The timer is already §cinvisible", "§7Der Timer ist bereits §cversteckt"),
	TIMER_ALREADY_SHOW("§7The timer is already §avisible", "§7Der Timer ist bereits §asichtbar"),

	PREGAME_MOVEMENT_DENY_TITLE("§cWait..", "§cWarte.."),
	PREGAME_MOVEMENT_DENY_SUBTITLE("§8• §7We are waiting.. §8", "§8• §7Wir warten noch.. §8•"),

	GAMEMODE_CHANGED("Your gamemode was changed to §e%mode%", "Dein Spielmodus wurde auf §e%mode% §7gesetzt"),
	GAMEMODE_CHANGED_OTHERS("§7You changed the gamemode of §e%players% §7player(s)", "§7Du hast den Spielmodus von §e%players% §7Spieler(n) geändert"),
	HEALED("§7You've been healed", "§7Du wurdest geheilt"),
	HEALED_OTHERS("§7You healed §e%players% §7player(s)", "§7Du hast §e%players% §7Spieler geheilt"),

	FORCE_HEIGHT_COMPLETE("§7Every player was on the §acorrect §7height", "§7Alle Spieler waren auf der §arichtigen §7Höhe"),
	FORCE_HEIGHT_FAIL("§e%player% §7was on the §cwrong height §8(§e%height%§8)", "§e%player% §7war auf der §cfalschen Höhe §8(§e%height%§8)"),

	FORCE_BLOCK_COMPLETE("§7Every player was on the §acorrent §7block", "§7Alle Spieler waren auf dem §arichtgen §7Block"),
	FORCE_BLOCK_FAIL("§e%player% §7was on the §cwrong block §8(§e%block%§8)", "§e%player% §7war auf dem §cfalschen Block §8(§e%block%§8)"),
	FORCE_BLOCK_SKIP("§7You §askipped §7the current block", "§7Du hast den aktuellen Block §ageskippt"),

	SNAKE_FAIL("§e%player% §7crossed §cthe snake", "§e%player% §7hat die Snake §cgekreuzt"),

	COLLECT_ITEMS_ITEM_REGISTERED("Item §e%new% §7registred §8(§e%count%§8)", "Das Item §e%new% §7wurde registriert §8(§e%count%§8)"),
	COLLECT_DEATHS_DEATH_REGISTERED("Death §e%new% §7registered §8(§e%count%§8)", "Du bist an §e%new% §7gestorben §8(§e%count%§8)"),
	COLLECT_DIAMONDS_NEW_DIAMOND("New diamond §eregistered §8(§e%count%§8)", "Neuer Diamant §eregistriert §8(§e%count%§8)"),

	SERVER_RESET_KICK(" \n §8» §cWorldreset \n §8━━━━━━━━━━━━━━━━━━━━━━━ \n §7The server is restarting \n §7Reset requested by §c%player% \n ",
							     " \n §8» §cWeltenreset \n §8━━━━━━━━━━━━━━━━━━━━━━━ \n §7Der Server startet jetzt neu \n §7Resetet durch §c%player% \n "),
	RESET_CONFIRM("§7Please confirm that you want to reset the world with §e/reset confirm", "§7Bitte bestätige dass du die Welten zurücksetzten willst mit §e/reset confirm"),
	PLAYER_DAMAGE("§e%player% §7got §e%damage% §7hearts damage §7by §e%cause%", "§e%player% §7hat durch §e%cause% §e%damage% §7Herzen Schaden bekommen"),
	PLAYER_DEATH("§e%player% §7died to §e%cause%", "§e%player% §7starb durch §e%cause%"),

	POSITION_CREATED("§e%player% §7created the position §e%position%", "§e%player% §7hat die Position §e%position% §7erstellt"),
	POSITION_GET("§e%name% §8• §7%position%", "§e%name% §8• §7%position%"),
	POSITION_EMPTY("§7There were no positions found", "§7Es wurden keine Positionen gefunden"),
	POSITION_CREATE("§7You can create one with §e/position <name>", "§7Du kannst eine mit §e/position <name> §7erstellen"),
	POSITION_LIST("§7The following §epositions §7are set:", "§7Die folgenden §ePositionen §7sind gesetzt:"),

	VILLAGE_TELEPORT("§7You were teleported to a village", "§7Du wurdest zu einem Dorf teleportiert"),
	VILLAGE_TELEPORT_ERROR("§7There was no village found", "§7Es wurde kein Dorf gefunden"),

	BACK("§8« §7Back", "§8« §7Zurück"),
	NEXT("§8» §7Next", "§8» §7Weiter"),

	GUESS_THE_FLAG_RIGHT("§e%player% §7got it right §8➟ §a%flag%", "§e%player% §7hat richtig getippt §8➟ §a%flag%"),
	GUESS_THE_FLAG_WRONG("§e%player% §7tipped wrong §➟ §c%flag%", "§e%player% §7hat falsch getippt §8➟ §c%flag%"),
	GUESS_THE_FLAG_NEW("§7Guess the §eflag§7. You have §e%time% §7to get it right", "§7Errate die §eFlagge§7. Du hast §e%time% §7um sie zu erraten!"),
	GUESS_THE_FLAG_TIME_IS_UP("§7Nobody guessed the flag.", "§7Niemand hat die §eFlagge §7erraten!"),
	GUESS_THE_FLAG_NOT_STARTED("§7You dont currently have to guess the flag", "§7Du musst derzeit keine Flagge erraten"),
	GUESS_THE_FLAG_FLAG_WAS("The flag was the flag of §e%flag%" , "Die Flagge war von §e%flag%"),
	GUESS_THE_FLAG_CURRENTFLAG("§7The current flag is §e%flag%", "§7Die derzeitige Flagge ist §e%flag%"),

	NO_SNEAK_PLAYER_SNEAKED("§e%player% §7just sneaked", "§e%player% §7hat gerade gesneakt"),
	NO_JUMP_PLAYER_JUMPED("§e%player% §7just jumped", "§e%player% §7ist gerade gesprungen"),

	FOOD_ONCE_FAIL("§e%player% ate §e%food%", "§e%player% hat §e%food% §7gegessen"),

	ONLY_DIRT_FAIL("§e%player% §7walked on §e%block%", "§e%player% §7trat auf §e%block%"),

	JUMP_AND_RUN_TELEPORT_IN("§7The next §5JumpAndRun §7is in §e%time%", "§7Das nächste §5JumpAndRun §7ist in §e%time%"),
	JUMP_AND_RUN_FAIL("§e%player% §cfailed §7the §5JumpAndRun", "§e%player% §7hat beim §5JumpAndRun §cversagt"),
	JUMP_AND_RUN_COMPLETE("§e%player% §acompleted §7the §5JumpAndRun", "§e%player% §7hat das §5JumpAndRun §ageschafft"),

	SECONDS("seconds", "Sekunden"),
	SECOND("second", "Sekunde"),
	MINUTES("minutes", "Minuten"),
	MINUTE("minute", "Minute"),
	HOURS("hours", "Stunden"),
	HOUR("hour", "Stunde"),

	CONFIG_OLD_VERSION("§7There was a §enew update §7which is now available to your server. You have to delete the §econfig.yml §7and restart the server after that. There will be new content available in the config", "§7Es gab ein §eneues Update§7 welches jetzt verfügbar ist. Lösche die §econfig.yml §7und restarte den Server um den neuen Inhalt in der Config zu sehen."),

	BACKBACKS_NOT_ACTIVE("§7Backpacks are currently disabled", "§7Rucksäcke sind derzeit ausgeschaltet"),
	BACKBACKS_OPEN("§7You opened the %backpack%", "§7Du hast den %backpack% §7geöffnet"),

	FEATURE_DISABLED("§7This feature is currently §cdisabled", "§7Diese Funktion ist derzeit §cdeaktiviert"),
	UP_COMMAND_TELEPORT("§7You were teleported to the top", "§7Du wurdest nach oben teleportiert"),

	MYSQL_ERROR("§7We could not access the databank. The following error has occurred §e%error% §7Please contact the server administrators about this", "§7Wir konnten deine Einstellungen nicht speichern. Der folgende Fehler ist aufgetreten: §e%error% §7Bitte kontaktiere die Serveradministratoren über diesen Fehler"),

	SAVE_CONFIG_SUCCESS("§7You config was §asuccessfully §7saved", "§7Deine Einstellungen wurden §agespeichert"),
	LOAD_CONFIG_SUCCESS("§7Your config was §asuccessfully §7loaded", "§7Deine Einstellungen wurden §ageladen"),
	NO_CONFIG_FOUND("§7You do not have a saved config", "§7Du hast keine gespeicherten Einstellungen"),

	BINGO_ITEM_YES("§8» §aYou already found this item", "§8» §aDu hast dieses Item schon gefunden"),
	BINGO_ITEM_NO("§8» §cYou have not found this item yet", "§8» §cDu hast dieses Item noch nicht gefunden"),
	BINGO_RESET("§7The items were rerolled", "§7Die Items wurden neu ausgelost"),
	BINGO_TEAM_JOIN("§7You joined the team §e%team%", "§7Du bist dem Team §e%team% §7beigetreten"),

	MASTER_COMMANDS("\n§7You are the §emaster§7 of the game.\nYou have §eaccess §7to the following commands: \n" +
			"§8● §e/challenges §8» §7Open the setting menu \n" +
			"§8● §e/start §8» §7Start the challengetimer \n" +
			"§8● §e/timer §8» §7More timer options \n" +
			"§8● §e/village §8» §7Teleports you to into a village \n" +
			"§8● §e/setmaster §8» §7Set the master to an other player \n" +
			"§8● §e/config §8» §7Save/load you challenge settings \n" +
			"§8● §e/reset §8» §7Resets and restarts the server. Be careful\n ",
			"\n§7Du bist der §eMaster§7 von dieser §eChallenge§7.\nDu hast §eZugriff§7 auf die folgenden Commands: \n" +
			"§8● §e/challenges §8» §7Öffnet das Einstellungs-Menu \n" +
			"§8● §e/start §8» §7Startet die Challenge \n" +
			"§8● §e/timer §8» §7Mehr Timereinstellungen \n" +
			"§8● §e/village §8» §7Teleportiert dich in ein Dorf \n" +
			"§8● §e/setmaster §8» §7Setzt einen anderen Spieler als Master \n" +
			"§8● §e/config §8» §7Speichert/Läd deine Einstellungen \n" +
			"§8● §e/reset §8» §7Resetet den Server und startet den Server neu\n "),

	WORLD_IN_USE_WAIT("§7Waiting for the extra world to §cget free", "§7Die Extrawelt wird gerade §cbenutzt§7."),

	ADVANCEMENT_DONE("§e%player% §7made the advancement §e%advancement%", "§e%player% §7hat den Erfolg §e%advancement% §7erzielt"),

	PLAYER_NOT_FOUND("§7The player §e%player% §7is not online", "§7Der Spieler §e%player% §7ist nicht online"),

	CORONA_MASK("§f§lMouth protection", "§f§lMundschutz"),

	WATER_MLG_SUCCESS("§7Every played did the §9WaterMLG", "§7Jeder hat den §9WaterMLG §ageschafft"),

	CHALLENGE_END_TIMER_END( " \n§c§lThe challenge ended! \n§c§lWinner: §7%winner% \n§c§lSeed: §7%seed% \n§c§lTime bygone: §7%time% \n ", " \n§c§lDie Challenge wurde beendet! \n§c§lGewinner: §7%winner% \n§c§lSeed: §7%seed% \n§c§lZeit vergangen: §7%time% \n "),
	CHALLENGE_END_GOAL_REACHED(" \n§c§lThe challenge has ended! \n§c§lSeed: §7%seed% \n§c§lTime needed: §7%time% \n ", " \n§c§lDie Challenge wurde beendet! \n§c§lSeed: §7%seed% \n§c§lZeit benötigt: §7%time% \n "),
	CHALLENGE_END_GOAL_FAILED(" \n§c§lThe challenge has ended! \n§c§lSeed: §7%seed% \n§c§lTime needed: §7%time% \n ", " \n§c§lDie Challenge ist vorbei! \n§c§lSeed: §7%seed% \n§c§lZeit verschwendet: §7%time% \n ");

	private final String[] messages; // These are the templates for all languages - this may be replaced with an extra file or a download
	private String currentMessage;

	Translation(String... messages) {
		this.messages = messages;
	}

	public String getTemplateMessage(Language language) {
		try {
			return messages[language.getID()];
		} catch (Exception ignored) {
			return "§4Translation: §4§l" + this.name();
		}
	}

	public void setCurrentMessage(String currentMessage) {
		this.currentMessage = currentMessage;
	}

	public String get() {
		if (currentMessage != null) {
			return currentMessage;
		} else {
			return getTemplateMessage(LanguageManager.getLanguage());
		}
	}

	@Override
	public String toString() {
		return get();
	}
}
