package net.codingarea.challenges.plugin;

import java.util.ArrayList;
import net.codingarea.challenges.plugin.challenges.type.abstraction.AbstractChallenge;
import net.codingarea.challenges.plugin.content.loader.ContentLoader;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.management.server.ChallengeEndCause;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ChallengeAPI {

	private ChallengeAPI() {}

	public static boolean isStarted() {
		return Challenges.getInstance().getChallengeTimer().isStarted();
	}

	public static boolean isPaused() {
		return Challenges.getInstance().getChallengeTimer().isPaused();
	}

	public static TimerStatus getTimerStatus() {
		return Challenges.getInstance().getChallengeTimer().getStatus();
	}

	public static void pauseTimer() {
		Challenges.getInstance().getChallengeTimer().pause(true);
	}

	public static void pauseTimer(boolean byPlayer) {
		Challenges.getInstance().getChallengeTimer().pause(byPlayer);
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

	public static boolean isWorldInUse() {
		return Challenges.getInstance().getWorldManager().isWorldInUse();
	}

	public static boolean isFresh() {
		return Challenges.getInstance().getServerManager().isFresh();
	}

	public static void registerScheduler(@Nonnull Object... scheduler) {
		Challenges.getInstance().getScheduler().register(scheduler);
	}

	public static void subscribeLoader(@Nonnull Class<? extends ContentLoader> classOfLoader, @Nonnull Runnable action) {
		Challenges.getInstance().getLoaderRegistry().subscribe(classOfLoader, action);
	}

	@Nonnull
	public static List<Material> getCustomDrops(@Nonnull Material block) {
		return Challenges.getInstance().getBlockDropManager().getCustomDrops(block);
	}

	public static boolean getDropChance(@Nonnull Material block) {
		return Challenges.getInstance().getBlockDropManager().getDropChance(block).getAsBoolean();
	}

	public static boolean getItemsDirectIntoInventory() {
		return Challenges.getInstance().getBlockDropManager().isItemsDirectIntoInventory();
	}

	@Nonnull
	public static String formatTime(long seconds) {
		return Challenges.getInstance().getChallengeTimer().getFormat().format(seconds);
	}

	/**
	 * @return all players that aren't ignored by the plugin
	 */
	@Nonnull
	public static List<Player> getPlayingPlayers() {
		List<Player> list = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!AbstractChallenge.ignorePlayer(player)) {
				list.add(player);
			}
		}
		return list;
	}

}
