package net.codingarea.challengesplugin.challengetypes.extra;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-15-2020
 * https://github.com/anweisen
 * https://github.com/Kxmisches
 */

public interface ITimerStatusExecutor {

	default void onTimerStart() { }
	default void onTimerStop() { }

}
