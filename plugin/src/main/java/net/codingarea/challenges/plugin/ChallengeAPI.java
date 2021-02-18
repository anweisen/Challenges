package net.codingarea.challenges.plugin;

import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeAPI {

	private ChallengeAPI() { }

	public static boolean isStarted() {
		return Challenges.getInstance().getChallengeTimer().isStarted();
	}

	public static boolean isPaused() {
		return Challenges.getInstance().getChallengeTimer().isPaused();
	}

	public static void pauseTimer() {
		Challenges.getInstance().getChallengeTimer().pause();
	}

	public static void resumeTimer() {
		Challenges.getInstance().getChallengeTimer().resume();
	}

	public static void resetTimer() {
		Challenges.getInstance().getChallengeTimer().reset();
	}

	public static void endChallenge(@Nonnull ChallengeEndCause endCause) {
		Challenges.getInstance().getServerManager().endChallenge(endCause);
	}

}
