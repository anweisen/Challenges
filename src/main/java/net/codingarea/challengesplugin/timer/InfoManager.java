package net.codingarea.challengesplugin.timer;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-15-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class InfoManager {

	private ConcurrentHashMap<Player, String> infos;

	public InfoManager() {
		infos = new ConcurrentHashMap<>();
	}

	/**
	 * @param info set it to null to hide the info line again
	 */
	public void setInfo(Player player, String info) {

		if (info == null) {
			infos.remove(player);
			return;
		}

		infos.put(player, info);

	}

	/**
	 * @return returns null when no info line is set
	 */
	public String getInfo(Player player) {
		return infos.get(player);
	}

}
