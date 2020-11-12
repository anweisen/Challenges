package net.codingarea.challengesplugin.manager.players.stats;

import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

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
			return getStatsByUUIDWithException(uuid);
		} catch (Exception ex) {
			Log.severe("Could not get player stats for uuid '" + uuid + "' :: " + ex.getMessage());
			return PlayerStats.empty();
		}
	}

	@NotNull
	public static PlayerStats getStatsByUUIDWithException(String uuid) throws SQLException {

		ResultSet result = MySQL.get("SELECT stats, player FROM user WHERE user = '" + uuid + "' LIMIT 1");
		if (result == null || !result.next()) return PlayerStats.empty();

		String statsJSON = result.getString("stats");
		String savedName = result.getString("player");

		result.close();

		return PlayerStats.fromJSON(statsJSON, savedName);

	}

	public static void storeStats(String uuid, PlayerStats stats) {
		try {

			if (uuid.equals("error")) throw new IllegalArgumentException("UUID cannot be equal to error");
			String statsJSON = stats.toJSON().toString();

			if (MySQL.isSet("SELECT stats FROM user WHERE user = '" + uuid + "'")) {
				MySQL.set("UPDATE user SET stats = '" + statsJSON + "' WHERE user = '" + uuid + "'");
			} else {
				MySQL.set("INSERT INTO user (user, stats) VALUES ('" + uuid + "', '" + statsJSON + "')");
			}

		} catch (Exception ex) {
			Log.severe("Could not save player stats for player " + uuid + " :: " + ex.getMessage());
		}
	}

}
