package net.codingarea.challengesplugin.events.types;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-31-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public abstract class ScoreboardEvent implements IEvent {

	public static class ScoreboardShowEvent extends ScoreboardEvent {
	}

	public static class ScoreboardHideEvent extends ScoreboardEvent {
	}

}
