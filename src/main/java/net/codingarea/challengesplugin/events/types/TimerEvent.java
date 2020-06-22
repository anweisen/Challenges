package net.codingarea.challengesplugin.events.types;

import net.codingarea.challengesplugin.events.types.IEvent.GeneralPlayerEvent;
import org.bukkit.entity.Player;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public abstract class TimerEvent extends GeneralPlayerEvent {

	public enum TimerStopCause {
		TIME_IS_UP,
		STOPPED_BY_PLAYER;
	}

	public static class TimerStopEvent extends TimerEvent{

		private TimerStopCause cause;

		public TimerStopEvent(Player player) {
			this.player = player;
		}

		public TimerStopCause getCause() {
			return cause;
		}

	}

	public static class TimerStartEvent extends TimerEvent {

		public TimerStartEvent(Player player) {
			this.player = player;
		}

	}

	public static class TimerResetEvent extends TimerEvent {

		public TimerResetEvent(Player player) {
			this.player = player;
		}

	}

	public static class TimerResumeEvent extends TimerEvent {

		public TimerResumeEvent(Player player) {
			this.player = player;
		}

	}

}
