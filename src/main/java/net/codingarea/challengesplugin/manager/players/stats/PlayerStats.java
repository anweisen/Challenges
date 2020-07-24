package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.challengesplugin.utils.Utils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author anweisen
 * Challenges developed on 07-11-2020
 * https://github.com/anweisen
 */

public class PlayerStats {

	private int challengesPlayed,
				challengesWon,
				timesJumped,
				timeSneaked,
				entityKills,
				blocksBroken,
				itemsCollected;

	private double damageTaken,
				   damageDealt;

	private String savedName;

	public PlayerStats(int challengesPlayed, int challengesWon, double damageTaken, double damageDealt, int timesJumped, int timeSneaked, int entityKills, int blocksBroken, int itemsCollected) {
		this.challengesPlayed = challengesPlayed;
		this.challengesWon = challengesWon;
		this.damageTaken = damageTaken;
		this.damageDealt = damageDealt;
		this.timesJumped = timesJumped;
		this.timeSneaked = timeSneaked;
		this.entityKills = entityKills;
		this.blocksBroken = blocksBroken;
		this.itemsCollected = itemsCollected;
	}

	public PlayerStats(int challengesPlayed, int challengesWon, double damageTaken, double damageDealt, int timesJumped, int timeSneaked, int entityKills, int blocksBroken, int itemsCollected, String savedName) {
		this.challengesPlayed = challengesPlayed;
		this.challengesWon = challengesWon;
		this.timesJumped = timesJumped;
		this.timeSneaked = timeSneaked;
		this.entityKills = entityKills;
		this.blocksBroken = blocksBroken;
		this.itemsCollected = itemsCollected;
		this.damageTaken = damageTaken;
		this.damageDealt = damageDealt;
		this.savedName = savedName;
	}

	public String getSavedName() {
		return savedName;
	}

	public void addChallengePlayed() {
		challengesPlayed++;
	}

	public void addChallengeWon() {
		challengesWon++;
	}

	public void addTimeJumped() {
		timesJumped++;
	}

	public void addSecondSneaked() {
		timeSneaked++;
	}

	public void addEntityKill() {
		entityKills++;
	}

	public void addBlockBroken() {
		blocksBroken++;
	}

	public void addItemsCollected(int items) {
		itemsCollected += items;
	}

	public void addDamageDealt(double damage) {
		damageDealt += damage;
	}

	public void addDamageTaken(double damage) {
		damageTaken += damage;
	}

	public double getDamageDealt() {
		return damageDealt;
	}

	public int getChallengesPlayed() {
		return challengesPlayed;
	}

	public double getDamageTaken() {
		return damageTaken;
	}

	public int getBlocksBroken() {
		return blocksBroken;
	}

	public int getChallengesWon() {
		return challengesWon;
	}

	public int getEntityKills() {
		return entityKills;
	}

	public int getItemsCollected() {
		return itemsCollected;
	}

	public int getTimesJumped() {
		return timesJumped;
	}

	public int getTimeSneaked() {
		return timeSneaked;
	}

	public double getAttribute(String string) {
		switch (string) {
			case "played":
				return challengesPlayed;
			case "won":
				return challengesWon;
			case "damageTaken":
				return damageTaken;
			case "damageDealt":
				return damageDealt;
			case "jumps":
				return timesJumped;
			case "sneaked":
				return timeSneaked;
			case "kills":
				return entityKills;
			case "blocksBroken":
				return blocksBroken;
			case "itemsCollected":
				return itemsCollected;
			default:
				return -1;
		}
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	public JSONObject toJSON() {

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("played", challengesPlayed);
		jsonObject.put("won", challengesWon);
		jsonObject.put("damageTaken", damageTaken);
		jsonObject.put("damageDealt", damageDealt);
		jsonObject.put("jumps", timesJumped);
		jsonObject.put("sneaked", timeSneaked);
		jsonObject.put("kills", entityKills);
		jsonObject.put("blocksBroken", blocksBroken);
		jsonObject.put("itemsCollected", itemsCollected);

		return jsonObject;

	}

	public static PlayerStats fresh() {
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
			return fresh();
		}
	}

}
