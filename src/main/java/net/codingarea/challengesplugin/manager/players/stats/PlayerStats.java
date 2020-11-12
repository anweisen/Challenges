package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.challengesplugin.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author anweisen
 * Challenges developed on 07-11-2020
 * https://github.com/anweisen
 */

public class PlayerStats {

	private final TreeMap<StatsAttribute, Double> stats = new TreeMap<>();
	private final String savedName;

	public PlayerStats(int challengesPlayed, int challengesWon, double damageTaken, double damageDealt, int timesJumped, int timeSneaked, int entityKills, int blocksBroken, int itemsCollected) {
		this(challengesPlayed, challengesWon, damageTaken, damageDealt, timesJumped, timeSneaked, entityKills, blocksBroken, itemsCollected, null);
	}

	public PlayerStats(int challengesPlayed, int challengesWon, double damageTaken, double damageDealt, int timesJumped, int timeSneaked, int entityKills, int blocksBroken, int itemsCollected, String savedName) {
		stats.put(StatsAttribute.PLAYED, (double) challengesPlayed);
		stats.put(StatsAttribute.WON, (double) challengesWon);
		stats.put(StatsAttribute.DAMAGE_TAKEN, damageTaken);
		stats.put(StatsAttribute.DAMAGE_DEALT, damageDealt);
		stats.put(StatsAttribute.JUMPS, (double) timesJumped);
		stats.put(StatsAttribute.ENTITIES_KILLED, (double) entityKills);
		stats.put(StatsAttribute.BLOCKS_BROKEN, (double) blocksBroken);
		stats.put(StatsAttribute.ITEMS_COLLECTED, (double) itemsCollected);
		stats.put(StatsAttribute.TIME_SNEAKED, (double) timeSneaked);
		this.savedName = savedName;
		calculateWinRate();
	}

	public String getSavedName() {
		return savedName;
	}

	public void add(StatsAttribute attribute, double value) {
		double stat = get(attribute) + value;
		stats.put(attribute, stat);
		if (attribute == StatsAttribute.WON || attribute == StatsAttribute.PLAYED) calculateWinRate();
	}

	private void set(StatsAttribute attribute, double value) {
		stats.put(attribute, value);
	}

	public double get(StatsAttribute attribute) {
		return stats.getOrDefault(attribute, 0D);
	}

	/*public double get(String string) {
		StatsAttribute attribute = StatsAttribute.byName(string);
		if (attribute == null) throw new IllegalArgumentException("No such attribute");
		return get(attribute);
	}*/

	@Override
	public String toString() {
		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("played", get(StatsAttribute.PLAYED));
		jsonObject.put("won", get(StatsAttribute.WON));
		jsonObject.put("damageTaken", get(StatsAttribute.DAMAGE_TAKEN));
		jsonObject.put("damageDealt", get(StatsAttribute.DAMAGE_DEALT));
		jsonObject.put("jumps", get(StatsAttribute.JUMPS));
		jsonObject.put("sneaked", get(StatsAttribute.TIME_SNEAKED));
		jsonObject.put("kills", get(StatsAttribute.ENTITIES_KILLED));
		jsonObject.put("blocksBroken", get(StatsAttribute.BLOCKS_BROKEN));
		jsonObject.put("itemsCollected", get(StatsAttribute.ITEMS_COLLECTED));

		return jsonObject;

	}

	public String asString(StatsAttribute attribute, boolean ending) {
		return attribute.format(get(attribute), ending);
	}

	public Set<StatsAttribute> getDeclaredAttributes() {
		return stats.keySet();
	}

	private void calculateWinRate() {

		double games = get(StatsAttribute.PLAYED);
		double wins = get(StatsAttribute.WON);

		double winRate = (games <= 0 ? 0 : (wins / games) * 100);
		stats.put(StatsAttribute.WINRATE, winRate);

	}

	public static PlayerStats empty() {
		return new PlayerStats(0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public static PlayerStats fromJSON(String json) {
		return fromJSON(json, null);
	}

	public static PlayerStats fromJSON(String json, String playerName) {
		try {

			JSONParser parser = new JSONParser();
			Object object = parser.parse(json);
			JSONObject jsonObject = (JSONObject) object;

			int challengesPlayed = Utils.getIntOrDefault(jsonObject, "played", 0);
			int challengesWon = Utils.getIntOrDefault(jsonObject, "won", 0);
			double damageTaken = (double) jsonObject.getOrDefault("damageTaken", 0);
			double damageDealt = (double) jsonObject.getOrDefault("damageDealt", 0);
			int jumps = Utils.getIntOrDefault(jsonObject, "jumps", 0);
			int sneaked = Utils.getIntOrDefault(jsonObject, "sneaked", 0);
			int kills = Utils.getIntOrDefault(jsonObject, "kills", 0);
			int blocksBroken = Utils.getIntOrDefault(jsonObject, "blocksBroken", 0);
			int itemsCollected = Utils.getIntOrDefault(jsonObject, "itemsCollected", 0);

			return new PlayerStats(challengesPlayed, challengesWon, damageTaken, damageDealt, jumps, sneaked, kills, blocksBroken, itemsCollected, playerName);

		} catch (Exception ex) {
			Log.severe("Could not parse player stats from input string '" + json + "' :: " + ex.getMessage());
			return empty();
		}
	}

}
