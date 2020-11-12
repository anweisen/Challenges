package net.codingarea.challengesplugin.timer;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.CloudNetManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.YamlConfig;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
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

	private final Challenges plugin;
	private final TimerMenu menu;

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

		for (World currentWold : Bukkit.getWorlds()) {
			currentWold.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		}

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

			menu.updateTime();

			if (seconds != 0 && (seconds % 10) == 0) {
				saveTimerDataToSessionConfig();
				plugin.getChallengeManager().saveChallengeConfigurations();
				if (plugin.getStatsManager().isEnabled()) {
					plugin.getStatsManager().storeAll();
				}
			}

			for (AbstractChallenge currentGeneralChallenge : plugin.getChallengeManager().getChallenges()) {
				if (!isPaused) {
					currentGeneralChallenge.handleTimerSecond();
				}
				if (currentGeneralChallenge instanceof ISecondExecutor) {
					((ISecondExecutor) currentGeneralChallenge).onSecond();
					if (Challenges.timerIsStarted()) {
						((ISecondExecutor) currentGeneralChallenge).onTimerSecond();
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
						plugin.getServerManager().handleChallengeEnd(null, ChallengeEndCause.TIMER_END, null);
					} else {
						this.seconds--;
					}
				}

				if (plugin.getStatsManager().isEnabled()) {
					Utils.forEachPlayerOnline(currentPlayer -> plugin.getStatsManager().onSecond(currentPlayer));
				}

			} else {
				for (Player currentOnlinePlayer : Bukkit.getOnlinePlayers()) {
					for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
						if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
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

		saveTimerDataToSessionConfig();

		for (World currentWold : Bukkit.getWorlds()) {
			currentWold.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
		}

		if (!isPaused) return;

		Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_STARTED.get().replace("%player%", player.getName()));

		if (!CloudNetManager.wasAlreadyIngame()) {
			plugin.getStatsManager().forEveryPlayerStats(stats -> stats.add(StatsAttribute.PLAYED, 1));
		}
		plugin.getCloudnetManager().setIngame();

		for (AbstractChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (currentChallenge instanceof ITimerStatusExecutor) {
				((ITimerStatusExecutor)currentChallenge).onTimerStart();
			}
			if (currentChallenge instanceof Goal) {
				((Goal)currentChallenge).onTimerStarted();
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

		saveTimerDataToSessionConfig();
		if (isPaused) return;

		if (message) {
			Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_PAUSED.get().replace("%player%", player != null ? player.getName() : "Console"));
		}

		plugin.getCloudnetManager().setLobby();

		for (AbstractChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
			if (currentChallenge instanceof ITimerStatusExecutor) {
				((ITimerStatusExecutor)currentChallenge).onTimerStop();
			}
		}
		for (World currentWorld : Bukkit.getWorlds()) {
			for (Entity currentEntity : currentWorld.getEntities()) {
			}
		}

		isPaused = true;
		sendActionbar();

	}

	public void resetTimer(Player player) {

		saveTimerDataToSessionConfig();

		if (isPaused) return;

		plugin.getCloudnetManager().setLobby();

		for (AbstractChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {
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
		Utils.forEachPlayerOnline(player -> Utils.sendActionbar(player, bar));
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
		saveTimerDataToSessionConfig();

		Bukkit.broadcastMessage(Prefix.TIMER + Translation.TIMER_SET_MODE.get().replace("%mode%", mode.name().toLowerCase()).replace("%player%", player != null ? player.getName() : "Console"));
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

	public boolean isHided() {
		return isHided;
	}

	public void setHided(boolean hided) {
		isHided = hided;
		sendActionbar();
		saveTimerDataToSessionConfig();
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
		sendActionbar();
		saveTimerDataToSessionConfig();
	}

	public void setMaxSeconds(int maxSeconds) {
		this.maxSeconds = maxSeconds;
		this.seconds = maxSeconds;
		sendActionbar();
		saveTimerDataToSessionConfig();
	}

	public void addSeconds(int seconds) {
		this.seconds += seconds;
		if (this.seconds < 0) this.seconds = 0;
		saveTimerDataToSessionConfig();
	}

	public void addMaxSeconds(int seconds) {
		this.maxSeconds += seconds;
		this.seconds = maxSeconds;
		if (this.seconds < 0) this.seconds = 0;
		if (this.maxSeconds < 0) this.maxSeconds = 0;
		saveTimerDataToSessionConfig();
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

	public Challenges getPlugin() {
		return plugin;
	}

	public Integer getTaskID() {
		return taskID;
	}

	public TimerMenu getMenu() {
		return menu;
	}

	public void saveTimerDataToSessionConfig() {

		YamlConfig sessionConfig = plugin.getConfigManager().getInternalConfig();
		FileConfiguration config = sessionConfig.toFileConfig();

		config.set("timer.mode", mode.name());
		config.set("timer.seconds", seconds);
		config.set("timer.hide", isHided);

		sessionConfig.save();

	}

	public void loadTimerDataFromSessionConfig() {

		FileConfiguration config = plugin.getConfigManager().getInternalConfig().toFileConfig();

		seconds = config.getInt("timer.seconds");
		try {
			mode = TimerMode.valueOf(config.getString("timer.mode"));
		} catch (Exception ignored) { }
		isHided = config.getBoolean("timer.hide");

	}

}
