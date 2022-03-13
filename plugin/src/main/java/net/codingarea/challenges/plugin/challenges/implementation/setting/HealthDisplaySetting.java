package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import javax.annotation.Nonnull;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class HealthDisplaySetting extends Setting {

	public static final String OBJECTIVE_NAME = "health_display";

	public HealthDisplaySetting() {
		super(MenuType.SETTINGS, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.RED_STAINED_GLASS, Message.forName("item-health-display-setting"));
	}

	@Override
	protected void onEnable() {
		Bukkit.getOnlinePlayers().forEach(this::show);
	}

	@Override
	protected void onDisable() {
		Bukkit.getOnlinePlayers().forEach(this::hide);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(@Nonnull PlayerJoinEvent event) {
		if (isEnabled()) {
			show(event.getPlayer());
			updatePlayerHealth(event.getPlayer());
		} else {
			hide(event.getPlayer());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(@Nonnull EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			updatePlayerHealth((Player) event.getEntity());
		}, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onRegain(@Nonnull EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			updatePlayerHealth((Player) event.getEntity());
		}, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEffect(@Nonnull EntityPotionEffectEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			updatePlayerHealth((Player) event.getEntity());
		}, 1);
	}


	@ScheduledTask(ticks = 10)
	public void updateAllPlayersHealth() {
		Bukkit.getOnlinePlayers().forEach(this::updatePlayerHealth);
	}

	protected void updatePlayerHealth(@Nonnull Player player) {
		for (Player current : Bukkit.getOnlinePlayers()) {
			Objective objective = current.getScoreboard().getObjective(OBJECTIVE_NAME);
			if (objective == null) continue;
			objective.getScore(player.getName()).setScore((int) (player.getHealth() + BukkitReflectionUtils.getAbsorptionAmount(player)));
		}
	}

	private void show(@Nonnull Player player) {

		Scoreboard scoreboard = player.getScoreboard();
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		if (manager == null) return;
		if (player.getScoreboard() == manager.getMainScoreboard())
			player.setScoreboard(scoreboard = manager.getNewScoreboard());

		Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
		if (objective == null)
			objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, "health", OBJECTIVE_NAME);

		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		try {
			objective.setRenderType(RenderType.HEARTS);
		} catch (Exception ex) {
			Challenges.getInstance().getLogger().severe("Tablist Health could not be updated. You are using an outdated version of spigot.");
			// In some versions of spigot RenderType does not exist
		}

		for (Player current : Bukkit.getOnlinePlayers()) {
			objective.getScore(current.getName()).setScore((int) current.getHealth());
		}

	}

	private void hide(@Nonnull Player player) {

		Scoreboard scoreboard = player.getScoreboard();
		Objective objective = scoreboard.getObjective(OBJECTIVE_NAME);
		if (objective == null) return;

		try {
			objective.unregister();
		} catch (Exception ex) {
			Challenges.getInstance().getLogger().severe("Error while unregistering tablist hearts objective");
		}

	}

}
