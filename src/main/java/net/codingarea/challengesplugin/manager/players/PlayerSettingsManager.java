package net.codingarea.challengesplugin.manager.players;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.commons.Log;
import net.codingarea.engine.sql.constant.ConstSQL;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author anweisen
 * Challenges developed on 06-24-2020
 * https://github.com/anweisen
 */

public class PlayerSettingsManager {

	public static final char SETTING_SEPARATOR = ',', ARGUMENT_SEPARATOR = '=';

	private final Challenges plugin;
	private final boolean savePlayerConfigs;

	public PlayerSettingsManager(Challenges plugin) {
		this.plugin = plugin;
		savePlayerConfigs = plugin.getConfig().getBoolean("save-player-configs");
	}

	public String settingsToString() {

		StringBuilder builder = new StringBuilder();

		for (AbstractChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {

			if (!builder.toString().isEmpty()) builder.append(SETTING_SEPARATOR);
			builder.append(currentChallenge.getChallengeName())
				   .append(ARGUMENT_SEPARATOR)
				   .append(currentChallenge.toValue());
		}

		return builder.toString();

	}

	public void loadSettings(String string) {

		String[] settings = string.split(String.valueOf(SETTING_SEPARATOR));

		for (String currentSettingString : settings) {

			String[] args = currentSettingString.split(String.valueOf(ARGUMENT_SEPARATOR));
			if (args == null || args.length < 2) continue;

			try {
				AbstractChallenge currentChallenge = plugin.getChallengeManager().getChallengeByName(args[0]);
				if (currentChallenge == null) continue;
				currentChallenge.setValues(Integer.parseInt(args[1]));
			} catch (NumberFormatException | NullPointerException ex) {
				Log.severe("Could not load settings for input '" + string + "' :: " + ex.getMessage());
			}

		}

	}

	public void save(String playerName) throws SQLException {
		saveSettings(playerName, settingsToString());
	}

	/**
	 * @return returns if there were settings saved
	 */
	public boolean load(String playerName) throws SQLException {

		String uuid = Utils.getUUID(playerName);
		updatePlayerName(uuid, playerName);
		ResultSet result = ConstSQL.query("SELECT settings FROM user WHERE user = ?", uuid);

		if (!result.next()) return false;

		String settings = result.getString("settings");
		loadSettings(settings);

		return true;

	}

	public void saveSettings(String playerName, String settings) throws SQLException {
		String uuid = Utils.getUUID(playerName);
		if (ConstSQL.isSet("SELECT * FROM user WHERE user = '" + uuid + "'")) {
			ConstSQL.update("UPDATE user SET settings = '" + settings + "', player = '" + playerName + "' WHERE user = '" + uuid + "'");
		} else {
			ConstSQL.update("INSERT INTO user (user, player, settings) VALUES (?, ?, ?)", uuid, playerName, settings);
		}
	}

	public static void updatePlayerName(String uuid, String playerName) {
		try {
			ConstSQL.update("UPDATE user SET player = ? WHERE user = ?", playerName, uuid);
		} catch (SQLException ex) {
			Log.severe("Could not update playername: " + ex.getMessage());
		}
	}

	public boolean savePlayerConfigs() {
		return savePlayerConfigs;
	}

}
