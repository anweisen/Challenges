package net.codingarea.challenges.plugin.spigot.listener;

import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.content.Prefix;
import net.codingarea.challenges.plugin.content.loader.UpdateLoader;
import net.codingarea.challenges.plugin.utils.misc.DatabaseHelper;
import net.codingarea.challenges.plugin.utils.misc.NameHelper;
import net.codingarea.challenges.plugin.utils.misc.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class PlayerConnectionListener implements Listener {

	private final boolean messages;
	private final boolean timerPausedInfo;
	private final boolean startTimerOnJoin;
	private final boolean resetOnLastQuit;
	private final boolean pauseOnLastQuit;
	private final boolean restoreDefaultsOnLastQuit;
	private final boolean groupsAndPermissions;

	public PlayerConnectionListener() {
		Document config = Challenges.getInstance().getConfigDocument();
		messages = config.getBoolean("join-quit-messages");
		timerPausedInfo = config.getBoolean("timer-is-paused-info");
		startTimerOnJoin = config.getBoolean("start-on-first-join");
		resetOnLastQuit = config.getBoolean("reset-on-last-leave");
		pauseOnLastQuit = config.getBoolean("pause-on-last-leave");
		restoreDefaultsOnLastQuit = config.getBoolean("restore-defaults-on-last-leave");
		groupsAndPermissions = config.getBoolean("groups-and-permissions");
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJoin(@Nonnull PlayerJoinEvent event) {

		Player player = event.getPlayer();

		player.getLocation().getChunk().load(true);
		ParticleUtils.spawnUpGoingParticleCircle(Challenges.getInstance(), player.getLocation(), Particle.SPELL_MOB, 17, 1, 2);
		Challenges.getInstance().getScoreboardManager().handleJoin(player);

		if (player.hasPermission("challenges.gui")) {
			if (Challenges.getInstance().isFirstInstall()) {
				player.sendMessage("");
				player.sendMessage(Prefix.CHALLENGES + "§7Thanks for downloading §e§lChallenges§7!");
				player.sendMessage(Prefix.CHALLENGES + "§7You can change the language in the §econfig.yml");
				player.sendMessage(Prefix.CHALLENGES + "§7For more join our discord §ediscord.gg/74Ay5zF");
			}

			if (timerPausedInfo && !startTimerOnJoin && ChallengeAPI.isPaused()) {
				player.sendMessage("");
				Message.forName("timer-paused-message").send(player, Prefix.CHALLENGES);
			}
		}

		if (Challenges.getInstance().getStatsManager().isNoStatsAfterCheating() && Challenges.getInstance().getServerManager().hasCheated()) {
			player.sendMessage("");
			Message.forName("cheats-already-detected").send(player, Prefix.CHALLENGES);
		}


		if (startTimerOnJoin) {
			player.sendMessage("");
			ChallengeAPI.resumeTimer();
		}

		if (player.hasPermission("challenges.gui")) {
			if (!UpdateLoader.isNewestConfigVersion()) {
				player.sendMessage("");
				Message.forName("deprecated-config-version").send(player, Prefix.CHALLENGES, UpdateLoader.getDefaultConfigVersion().format(), UpdateLoader.getCurrentConfigVersion().format());
			}

			List<String> missingConfigSettings = Challenges.getInstance().getConfigManager().getMissingConfigSettings();
			if (!missingConfigSettings.isEmpty()) {
				player.sendMessage("");
				String separator = Message.forName("missing-config-settings-separator").asString();
				Message.forName("missing-config-settings").send(player, Prefix.CHALLENGES, String.join(separator, missingConfigSettings));
			} else if (!UpdateLoader.isNewestConfigVersion()) {
				player.sendMessage("");
				Message.forName("no-missing-config-settings").send(player, Prefix.CHALLENGES, UpdateLoader.getDefaultConfigVersion().format());
			}
			if (!UpdateLoader.isNewestPluginVersion()) {
				player.sendMessage("");
				Message.forName("deprecated-plugin-version").send(player, Prefix.CHALLENGES, "spigotmc.org/resources/" + UpdateLoader.RESOURCE_ID);
			}
		}


		if (messages) {
			player.sendMessage("");
			event.setJoinMessage(Prefix.CHALLENGES + Message.forName("join-message").asString(NameHelper.getName(event.getPlayer())));
		}

		if (Challenges.getInstance().getDatabaseManager().isConnected()) {
			Challenges.getInstance().runAsync(() -> DatabaseHelper.savePlayerData(player));
		}

		if (groupsAndPermissions) {
			File permissionsFile = new File("./permissions.yml");
			YamlConfiguration permissionsConfig = YamlConfiguration.loadConfiguration(permissionsFile);

			ConfigurationSection usersSection = permissionsConfig.getConfigurationSection("users");
			if (usersSection != null) {
				ConfigurationSection userSection = usersSection.getConfigurationSection(player.getName());
				if (userSection != null) {
					List<String> groups = userSection.getStringList("groups");
					for (String group : groups) {
						ConfigurationSection groupSection = permissionsConfig.getConfigurationSection("groups." + group);
						if (groupSection != null) {
							List<String> permissions = groupSection.getStringList("permissions");
							for (String permission : permissions) {
								player.addAttachment(Challenges.getInstance(), permission, true);
							}
						}
					}
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(@Nonnull PlayerQuitEvent event) {

		try {
			Player player = event.getPlayer();
			Challenges.getInstance().getScoreboardManager().handleQuit(player);
			DatabaseHelper.clearCache(event.getPlayer().getUniqueId());

			if (Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) {
				event.setQuitMessage(null);
			} else if (messages) {
				event.setQuitMessage(null);
				Message.forName("quit-message").broadcast(Prefix.CHALLENGES, NameHelper.getName(event.getPlayer()));
			}
		} catch (Exception exception) {
			Challenges.getInstance().getLogger().error("Error while handling disconnect", exception);
		}

		if (Bukkit.getOnlinePlayers().size() <= 1) {

			if (!Challenges.getInstance().getWorldManager().isShutdownBecauseOfReset()) {
				if (resetOnLastQuit && !ChallengeAPI.isFresh()) {
					Challenges.getInstance().getWorldManager().prepareWorldReset(Bukkit.getConsoleSender());
					return;
				} else if (pauseOnLastQuit && ChallengeAPI.isStarted()) {
					ChallengeAPI.pauseTimer();
				}
			}

			if (restoreDefaultsOnLastQuit) {
				Challenges.getInstance().getChallengeManager().restoreDefaults();
			}
		}

	}

}
