package net.codingarea.challengesplugin.events.types;

import net.codingarea.challengesplugin.events.types.IEvent.GeneralPlayerEvent;
import org.bukkit.entity.Player;

/**
 * @author anweisen
 * Challenges developed on 06-05-2020
 * https://github.com/anweisen
 */

public abstract class MasterEvent extends GeneralPlayerEvent {

	public static class MasterChangeEvent extends MasterEvent {

		private Player from;

		public MasterChangeEvent(Player from, Player to) {
			this.from = from;
			this.player = to;
		}

		public Player getFrom() {
			return from;
		}
	}

	public static class MasterDisconnectEvent extends MasterEvent {

		private Player newMaster;

		public MasterDisconnectEvent(Player old, Player newMaster) {
			this.player = old;
			this.newMaster = newMaster;
		}

		public Player getNewMaster() {
			return newMaster;
		}
	}

}
