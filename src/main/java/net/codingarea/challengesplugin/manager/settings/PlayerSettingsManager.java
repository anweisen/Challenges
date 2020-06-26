package net.codingarea.challengesplugin.manager.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.utils.sql.MySQL;
import org.bukkit.Bukkit;
import sun.java2d.loops.FillRect.General;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author anweisen
 * Challenges developed on 06-24-2020
 * https://github.com/anweisen
 */

public class PlayerSettingsManager {

	public static final String DEFAULT =
			"damagedisplay=1,notrading=0,soups=0,unberakabletools=0,keepinventory=0,backpack=1,timber=0,pvp=0,upcommand=0," +
			"hydra=0,dupedspawn=0,jumpheigher=0,floorislava=0,floorhole=0,bedrockwall=0,bedrockpath=0,watermlg=0,anvilrain=0," +
			"noexp=0,onlydirt=0,damageclear=0,onedurability=0,blockrandomizer=0,craftingrandomizer=0,mobrandomizer=0,nosneak=0," +
			"nojump=0,forceheight=0,forceblock=0,chunkdeconstruction=0,dragonkill=1,killwither=0,collectdeaths=0,collectitems=0," +
			"collectwood=0,breakblocks=0,minediamonds=0,difficulty=4,regeneration=2,splithealth=0,maxhealth=20,damagemultiplier=1," +
			"respawn=0,teamlife=1,nohunger=0,§7armor=1,goldenapple=1,craftingtable=1,chest=1,furnace=1,enchantingtable=1," +
			"anvil=1,fletchingtable=1,brewingstand=1,bow=1,snowball=1,flintandsteel=1,firedamage=1,lavadamage=1,falldamage=1," +
			"explosiondamage=1,attackdamage=1,magicdamage=1,blockdamage=1";

	private Challenges plugin;

	public PlayerSettingsManager(Challenges plugin) {
		this.plugin = plugin;
	}

	public String settingsToString() {

		StringBuilder builder = new StringBuilder();

		for (GeneralChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (!builder.toString().isEmpty()) {
				builder.append(",");
			}
			builder.append(currentChallenge.getChallengeName())
				   .append("=")
				   .append(currentChallenge.toValue());
		}

		return builder.toString();

	}

	public void loadSettings(String string) {

		String[] settings = string.split(",");

		for (String currentSettingString : settings) {

			String[] args = currentSettingString.split("=");

			try {
				GeneralChallenge currentChallenge = plugin.getChallengeManager().getChallengeByName(args[0]);
				currentChallenge.setValues(Integer.parseInt(args[1]));
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
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

		ResultSet result = MySQL.get("SELECT settings FROM user WHERE user = '" + playerName +  "'");

		if (!result.next()) return false;

		String settings = result.getString("settings");
		loadSettings(settings);

		return true;

	}

	public void saveSettings(String playerName, String settings) throws SQLException {
		if (MySQL.isSet("SELECT * FROM user WHERE user = '" + playerName + "'")) {
			MySQL.set("UPDATE user SET settings = '" + settings + "' WHERE user = '" + playerName + "'");
		} else {
			MySQL.set("INSERT INTO user (user, settings) VALUES ('" + playerName + "', '" + settings + "')");
		}
	}

}
