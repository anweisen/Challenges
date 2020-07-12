package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;

/**
 * @author anweisen
 * Challenges developed on 07-11-2020
 * https://github.com/anweisen
 */

public class StatsWrapper {

	@NotNull
	public static PlayerStats getStatsByName(String playerName) {
		return getStatsByUUID(Utils.getUUID(playerName));
	}

	@NotNull
	public static PlayerStats getStatsByUUID(String uuid) {
		try {

			ResultSet result = MySQL.get("SELECT stats FROM user WHERE user = '" + uuid + "'");
			if (result == null || !result.next()) return PlayerStats.fresh();

			String statsJSON = result.getString("stats");

			return PlayerStats.fromJSON(statsJSON);

		} catch (Exception ex) {
			Challenges.getInstance().getLogger().severe("Could not get player stats for uuid '" + uuid + "' :: " + ex.getMessage());
			return PlayerStats.fresh();
		}
	}

	public static void storeStats(String uuid, PlayerStats stats) {
		try {

			String statsJSON = stats.toJSON().toString();

			if (MySQL.isSet("SELECT stats FROM user WHERE user = '" + uuid + "'")) {
				MySQL.set("UPDATE user SET stats = '" + statsJSON + "' WHERE user = '" + uuid + "'");
			} else {
				MySQL.set("INSERT INTO user (user, stats) VALUES ('" + uuid + "', '" + statsJSON + "')");
			}

			Challenges.getInstance().getLogger().info("Stats saved! " + statsJSON);

		} catch (Exception ex) {
			Challenges.getInstance().getLogger().severe("Could not save player stats for player " + uuid + " :: " + ex.getMessage());
		}
	}

}
