package net.codingarea.challenges.plugin.spigot.listener;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.management.stats.PlayerStats;
import net.codingarea.challenges.plugin.management.stats.Statistic;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see net.codingarea.challenges.plugin.management.stats.StatsManager
 */
public class StatsListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(@Nonnull EntityDamageEvent event) {
		if (countNoStats()) return;
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;
		incrementStatistic(player, Statistic.DAMAGE_TAKEN, event.getFinalDamage());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(@Nonnull EntityDamageByEntityEvent event) {
		if (countNoStats()) return;

		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;
			incrementStatistic(player, Statistic.DAMAGE_DEALT, event.getFinalDamage());
		} else if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if (!((projectile.getShooter()) instanceof Player)) return;
			Player player = (Player) projectile.getShooter();
			if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;
			incrementStatistic(player, Statistic.DAMAGE_DEALT, event.getFinalDamage());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (countNoStats()) return;
		incrementStatistic(event.getPlayer(), Statistic.BLOCKS_PLACED, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (countNoStats()) return;
		incrementStatistic(event.getPlayer(), Statistic.BLOCKS_MINED, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(@Nonnull PlayerDeathEvent event) {
		if (event.getEntity().getGameMode() == GameMode.SPECTATOR || event.getEntity().getGameMode() == GameMode.CREATIVE) return;
		if (countNoStats()) return;
		incrementStatistic(event.getEntity(), Statistic.DEATHS, 1);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKill(@Nonnull EntityDeathEvent event) {
		if (countNoStats()) return;
		LivingEntity entity = event.getEntity();
		Player player = entity.getKiller();
		if (player == null) return;
		if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) return;

		incrementStatistic(player, Statistic.ENTITY_KILLS, 1);
		if (entity instanceof EnderDragon) {
			incrementStatistic(player, Statistic.DRAGON_KILLED, 1);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR || event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		if (ChallengeAPI.isPaused()) return;
		if (countNoStats()) return;
		if (event.getTo() == null) return;
		if (BlockUtils.isSameBlockIgnoreHeight(event.getFrom(), event.getTo())) return;
		incrementStatistic(event.getPlayer(), Statistic.BLOCKS_TRAVELED, 1);
	}

	private void incrementStatistic(@Nonnull Player player, @Nonnull Statistic statistic, double amount) {
		PlayerStats stats = Challenges.getInstance().getStatsManager().getStats(player.getUniqueId(), player.getName());
		stats.incrementStatistic(statistic, amount);
	}

	private boolean countNoStats() {
		return Challenges.getInstance().getServerManager().hasCheated() && Challenges.getInstance().getStatsManager().isNoStatsAfterCheating();
	}

}
