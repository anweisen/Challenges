package net.codingarea.challengesplugin.listener;

import com.google.common.collect.Sets;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.players.stats.StatsAttribute;
import net.codingarea.challengesplugin.manager.players.stats.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.UUID;

/**
 * @author anweisen
 * Challenges developed on 07-12-2020
 * https://github.com/anweisen
 */

public class StatsListener implements Listener {

	private final StatsManager manager;

	public StatsListener(StatsManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

		if (!Challenges.timerIsStarted()) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		LivingEntity entity = (LivingEntity) event.getEntity();

		if (event.getDamager() instanceof Player) {

			Player player = (Player) event.getDamager();
			Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
				if (!event.isCancelled()) {
					handlePlayerDoesDamage(player, event.getFinalDamage());
					if (entity.getHealth() <= 0) handlePlayerDoesKill(player);
				}
			}, 1);

		} else if (event.getDamager() instanceof Projectile) {

			Projectile projectile = (Projectile) event.getDamager();
			if (projectile.getShooter() instanceof Player) {

				Player player = (Player) projectile.getShooter();
				Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
					if (!event.isCancelled()) {
						handlePlayerDoesDamage(player, event.getFinalDamage());
						if (entity.getHealth() <= 0) handlePlayerDoesKill(player);
					}
				}, 1);

			}

		}

	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {

		if (!Challenges.timerIsStarted()) return;
		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (!event.isCancelled()) {
				handlePlayerGetsDamaged(player, event.getFinalDamage());
			}
		}, 1);

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		if (!Challenges.timerIsStarted()) return;
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (!event.isCancelled()) {
				handlePlayerBreakBlock(event.getPlayer());
			}
		}, 1);

	}

	@SuppressWarnings("depraction")
	@EventHandler
	public void onPlayerCollectItem(PlayerPickupItemEvent event) {

		if (!Challenges.timerIsStarted()) return;
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (!event.isCancelled()) {
				handlePlayerCollectItem(event.getPlayer(), event.getItem().getItemStack().getAmount());
			}
		}, 1);

	}

	private final Set<UUID> prevPlayersOnGround = Sets.newHashSet();
	private final Set<UUID> prevPlayerHitted = Sets.newHashSet();

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!Challenges.timerIsStarted()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		prevPlayerHitted.add(event.getEntity().getUniqueId());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {

		if (!Challenges.timerIsStarted()) return;
		if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		Player player = event.getPlayer();

		if (!prevPlayerHitted.contains(player.getUniqueId()) && player.getVelocity().getY() > 0) {

			double jumpVelocity = 0.42F;
			if (player.hasPotionEffect(PotionEffectType.JUMP)) {
				jumpVelocity += (float) (player.getPotionEffect(PotionEffectType.JUMP).getAmplifier() + 1) * 0.1F;
			}

			if (!player.getLocation().getBlock().getType().name().contains("water")
					&& player.getLocation().getBlock().getType() != Material.LADDER
					&& prevPlayersOnGround.contains(player.getUniqueId())) {

				if (!player.isOnGround() && Double.compare(player.getVelocity().getY(), jumpVelocity) == 0) {
					handlePlayerJump(player);
				}
			}
		}

		if (player.isOnGround()) {
			prevPlayersOnGround.add(player.getUniqueId());
			prevPlayerHitted.remove(player.getUniqueId());
		} else {
			prevPlayersOnGround.remove(player.getUniqueId());
		}
	}

	private void handlePlayerJump(Player player) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.JUMPS, 1);
	}

	private void handlePlayerCollectItem(Player player, int items) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.ITEMS_COLLECTED, items);
	}

	private void handlePlayerBreakBlock(Player player) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.BLOCKS_BROKEN, 1);
	}

	private void handlePlayerDoesDamage(Player player, double damage) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.DAMAGE_DEALT, damage);
	}

	private void handlePlayerGetsDamaged(Player player, double damage) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.DAMAGE_TAKEN, damage);
	}

	private void handlePlayerDoesKill(Player player) {
		Challenges.getInstance().getStatsManager().add(player, StatsAttribute.ENTITIES_KILLED, 1);
	}

}
