package net.codingarea.challengesplugin.timer;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.challengetypes.Challenge;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ChallengeTimer {

	public static final int MAX_VALUE = 99*24*60*60 + 23*60*60 + 59*60 + 59;
	public static final int MIN_VALUE = 0;

	public enum TimerMode {

		UP,
		DOWN;

		public TimerMode reverse() {
			return this == UP ? DOWN : UP;
		}

	}

	@Getter private final Challenges plugin;

	@Getter private final TimerMenu menu;

	private boolean isPaused;
	private boolean isHided;

	private TimerMode mode = TimerMode.UP;
	private int maxSeconds;

	private Integer taskID;
	private int seconds;

	public ChallengeTimer(Challenges plugin) {

		this.plugin = plugin;
		isPaused = true;
		seconds = 0;

		menu = new TimerMenu(this);

	}

	public void start() {

		if (taskID != null) return;

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

			menu.updateTime();

			for (GeneralChallenge currentGeneralChallenge : plugin.getChallengeManager().getChallenges()) {
				if (currentGeneralChallenge instanceof ISecondExecutor) {
					((ISecondExecutor) currentGeneralChallenge).onSecond();
					if (Challenges.timerIsStarted()) {
						((ISecondExecutor) currentGeneralChallenge).onTimerSecond();
					}
				}
				if (Challenges.timerIsStarted()) {
					if (currentGeneralChallenge instanceof Challenge) {
						Challenge currentChallenge = (Challenge) currentGeneralChallenge;
						if (!currentChallenge.isEnabled()) continue;
						currentChallenge.handleOnSecond();
					}
				}
			}

			if (!isPaused) {

				if (mode == TimerMode.UP) {
					if (seconds > MAX_VALUE) {
						this.seconds = MIN_VALUE;
					}
					this.seconds++;
				} else {
					if (seconds <= MIN_VALUE) {
						plugin.getServerManager().handleChallengeEnd(null, ChallengeEndCause.TIMER_END);
					} else {
						this.seconds--;
					}
				}


			} else {
				for (Player currentOnlinePlayer : Bukkit.getOnlinePlayers()) {
					for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
						currentOnlinePlayer.playEffect(currentPlayer.getLocation(), Effect.ENDER_SIGNAL, 1);
					}
				}
			}

			sendActionbar();

		},0, 20);

	}

	public void stop() {
		if (taskID != null) {
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = null;
		}
	}

	public void resume(Player player) {

		if (!isPaused) return;

		Bukkit.broadcastMessage(plugin.getStringManager().TIMER_PREFIX + Translation.TIMER_STARTED.get().replace("%player%", player.getName()));

		plugin.getCloudnetManager().setIngame();

		for (GeneralChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (currentChallenge instanceof ITimerStatusExecutor) {
				((ITimerStatusExecutor)currentChallenge).onTimerStop();
			}
		}

		new AnimationSound(Sound.ENTITY_ENDER_DRAGON_GROWL, 0.4F, 1).broadcast();

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			currentPlayer.setGameMode(GameMode.SURVIVAL);
		}

		isPaused = false;

		if (seconds <= 0) {
			seconds = MIN_VALUE + 1;
		}

		sendActionbar();

	}

	public void stopTimer(Player player, boolean message) {

		if (isPaused) return;

		if (message) {
			Bukkit.broadcastMessage(plugin.getStringManager().TIMER_PREFIX + Translation.TIMER_PAUSED.get().replace("%player%", player != null ? player.getName() : "Console"));
		}

		plugin.getCloudnetManager().setLobby();

		for (GeneralChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (currentChallenge instanceof ITimerStatusExecutor) {
				((ITimerStatusExecutor)currentChallenge).onTimerStop();
			}
		}

		isPaused = true;
		seconds = maxSeconds;

		sendActionbar();

	}

	public void resetTimer(Player player) {

		if (isPaused) return;

		plugin.getCloudnetManager().setLobby();

		for (GeneralChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (currentChallenge instanceof ITimerStatusExecutor) {
				((ITimerStatusExecutor)currentChallenge).onTimerStop();
			}
		}

		isPaused = true;
		seconds = mode == TimerMode.UP ? 0 : maxSeconds;

		sendActionbar();

	}

	public void sendTimeActionbar() {
		sendBar((mode == TimerMode.UP ? plugin.getStringManager().ACTIONBAR_TIMER_TIME_UP : plugin.getStringManager().ACTIONBAR_TIMER_TIME_DOWN).replace("%time%", getTimeDisplay(seconds)));
	}

	public void sendPauseActionbar() {
		sendBar(plugin.getStringManager().ACTIONBAR_TIMER_STOP);
	}

	public void sendHidedBar() {
		sendBar(" ");
	}

	public void sendBar(String bar) {

		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			Utils.sendActionbar(currentPlayer, bar);
		}

	}

	public void sendActionbar() {

		if (isHided) {
			sendHidedBar();
			return;
		}

		if (mode == TimerMode.UP && isPaused || this.seconds == 0 && mode == TimerMode.DOWN) {
			sendPauseActionbar();
		} else {
			sendTimeActionbar();
		}
	}

	public void setMode(TimerMode mode, Player player) {

		if (mode == null) throw new NullPointerException("TimerMode cannot be null!");

		Bukkit.broadcastMessage(plugin.getStringManager().TIMER_PREFIX + Translation.TIMER_SET_MODE.get().replace("%mode%", mode.name().toLowerCase()).replace("%player%", player != null ? player.getName() : "Console"));
		AnimationSound.PLOP_SOUND.play(player);

		this.mode = mode;

		sendActionbar();

	}

	public TimerMode getMode() {
		return mode;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMaxSeconds() {
		return maxSeconds;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
		sendActionbar();
	}

	public void setMaxSeconds(int maxSeconds) {
		this.maxSeconds = maxSeconds;
		this.seconds = maxSeconds;
		sendActionbar();
	}

	public void addSeconds(int seconds) {
		this.seconds += seconds;
		if (this.seconds < 0) this.seconds = 0;
	}

	public void addMaxSeconds(int seconds) {
		this.maxSeconds += seconds;
		this.seconds = maxSeconds;
		if (this.seconds < 0) this.seconds = 0;
		if (this.maxSeconds < 0) this.maxSeconds = 0;
	}

	public static String getTimeDisplay(int seconds) {

		int minutes = seconds / 60;
		int hours = minutes / 60;
		int days = hours / 24;

		seconds %= 60;
		minutes %= 60;
		hours %= 24;

		return (days > 0 ?  (days > 9 ? days : "0" + days) + ":" : "")
				+ (hours > 0 ?  (hours > 9 ? hours : "0" + hours) + ":" : "")
				+ (minutes > 9 ? minutes : "0" + minutes) + ":"
				+ (seconds > 9 ? seconds : "0" + seconds);

	}

}
