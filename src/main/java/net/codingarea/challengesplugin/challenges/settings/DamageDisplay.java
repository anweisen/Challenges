package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-22-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class DamageDisplay extends Setting implements Listener {

	private static DamageDisplay instance;

	public DamageDisplay() {
		super(MenuType.SETTINGS);
		instance = this;
		enabled = true;
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.COMMAND_BLOCK, ItemTranslation.DAMAGE_DISPLAY).build();
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getCause() == DamageCause.CUSTOM) return;

		Player player = (Player) event.getEntity();
		if (player.isBlocking()) return;

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (event.isCancelled()) return;
			handleDamage(getCause(event), event.getFinalDamage(), player);
		}, 1);
	}

	public static String getCause(EntityDamageEvent event) {

		if (event.getCause() == DamageCause.CUSTOM) return Translation.UNDEFINED.get();

		String cause = Utils.getEnumName(event.getCause());

		if (event instanceof EntityDamageByEntityEvent) {

			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;

			if (damageEvent.getDamager() instanceof Player) {
				Player damager = (Player) damageEvent.getDamager();
				cause += " §8(§7" + damager.getName() + "§8)";
			} else if (damageEvent.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) damageEvent.getDamager();
				cause = Utils.getEnumName(projectile.getType().name());
				String damager = "";
				ProjectileSource shooter = projectile.getShooter();
				if (shooter instanceof Entity) {
					Entity entity = (Entity) shooter;
					if (entity instanceof Player) {
						Player playerDamager = (Player) entity;
						damager = playerDamager.getName();
					} else {
						damager = Utils.getEnumName(entity.getType().name());
					}
				}
				cause += " §8(§7" + damager + "§8)";
			} else {
				cause += " §8(§7" + Utils.getEnumName(damageEvent.getDamager().getType().name()) + "§8)";
			}

		}

		return cause;

	}

	public static void handleDamage(String cause, double damage, Player player) {

		if (!instance.enabled) return;
		if (damage <= 0) return;

		DecimalFormat format = new DecimalFormat("0.0");
		String damageString = damage > 2000 ? "∞" : format.format(damage / ((double)2));

		String message = Translation.PLAYER_DAMAGE.get()
				.replace("%player%", player.getName())
				.replace("%cause%", cause)
				.replace("%damage%", damageString);

		Bukkit.broadcastMessage(Prefix.DAMAGE + message);

	}

}
