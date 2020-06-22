package net.codingarea.challengesplugin.events.types;

import org.bukkit.entity.Player;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public interface IEvent {

	abstract class GeneralPlayerEvent implements IEvent {

		protected Player player;

		public Player getPlayer() {
			return player;
		}


	}

}
