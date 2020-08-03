package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.NumberFormatter;

import java.util.Arrays;

import static net.codingarea.challengesplugin.utils.commons.NumberFormatter.*;

/**
 * @author anweisen
 * Challenges developed on 08-02-2020
 * https://github.com/anweisen
 */

public enum StatsAttribute {

	PLAYED("Challenges gespielt", "Challenges", "<:games:739218209844101221>", true, MIDDLE_NUMBER, "challenges", "plays", "spiele", "gespielt"),
	WON("Challenges gewonnen", "Wins", "<:wins:739218214222823424>", true, MIDDLE_NUMBER, "wins", "won", "gewonnen"),
	WINRATE("Winrate", "Winrate", "<:winrate:739218225174413362>", false, PERCENTAGE),
	DAMAGE_DEALT("Schaden ausgeteilt", "Schaden", "<:sword:739218222317961216>", true, MIDDLE_NUMBER, 2F, "Herzen", "schaden", "schaden gemacht", "schaden ausgeteilt", "ausgeteilt", "gemacht"),
	DAMAGE_TAKEN("Schaden genommen", "HP", "<:hp:739455715651092500>", true, MIDDLE_NUMBER, 2F, "Herzen", "bekommen", "schaden bekommen", "damage taken"),
	ENTITIES_KILLED("Mobs getötet", "Kills", "<:kills:739218210116730981>", true, BIG_NUMBER, "kills", "killed", "mobs", "entities"),
	BLOCKS_BROKEN("Blöcke abgebaut", "Blöcke", "<:block:739454234633699328>", true, BIG_NUMBER, "blocks", "blöcke", "abgebaut", "broken"),
	ITEMS_COLLECTED("Items aufgesammelt", "Items", "<:points:739218210326446181>", true, BIG_NUMBER, "items", "collected"),
	JUMPS("Sprünge", "Jumps", "<:jumps:739577516863520808>", true, MIDDLE_NUMBER, "jumps", "sprünge"),
	TIME_SNEAKED("Zeit gesnaked", "Sneaked", "<:time:739449230564917249>", true, TIME, "time", "sneaked", "gesneaked", "sneak");

	private final String name, shortName, emoji;
	private final String[] alias;
	private final NumberFormatter formatter;
	private final float multiplied;
	private final String ending;
	private final boolean database;

	StatsAttribute(String name, String shortName, String emoji, boolean database, NumberFormatter formatter, String... alias) {
		this.name = name;
		this.alias = alias;
		this.formatter = formatter;
		this.multiplied = 1F;
		this.ending = "";
		this.emoji = emoji;
		this.database = database;
		this.shortName = shortName;
	}

	StatsAttribute(String name, String shortName, String emoji, boolean database, NumberFormatter formatter, float multiplied, String ending, String... alias) {
		this.name = name;
		this.alias = alias;
		this.formatter = formatter;
		this.multiplied = multiplied;
		this.ending = ending;
		this.emoji = emoji;
		this.database = database;
		this.shortName = shortName;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public String[] getAlias() {
		return alias;
	}

	public String getEmoji() {
		return emoji;
	}

	public String format(double value, boolean ending) {
		return formatter.format(value / multiplied) + (ending && this.ending != null ? " " + this.ending : "");
	}

	public boolean isSavedInDatabase() {
		return database;
	}

	public static StatsAttribute byName(String name) {
		for (StatsAttribute currentAttribute : values()) {
			if (!currentAttribute.database) continue;
			if (currentAttribute.name.equalsIgnoreCase(name)
				|| currentAttribute.shortName.equalsIgnoreCase(name)
				|| Utils.listContainsStringIgnoreCase(Arrays.asList(currentAttribute.alias), name)) {
					return currentAttribute;
			}
		}
		return null;
	}

}
