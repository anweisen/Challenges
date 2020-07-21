package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
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
		instance = this;
		menu = MenuType.SETTINGS;
		enabled = true;
	}

	@Override
	public String getChallengeName() {
		return "damagedisplay";
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.COMMAND_BLOCK, ItemTranslation.DAMAGE_DISPLAY).build();
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getCause() == DamageCause.CUSTOM || event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE) return;

		Player player = (Player) event.getEntity();

		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (event.isCancelled()) return;
			handleDamage(Utils.getEnumName(event.getCause().name()), event.getFinalDamage(), player);
		}, 1);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getCause() == DamageCause.CUSTOM) return;
		if (event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.PROJECTILE) return;

		Player player = (Player) event.getEntity();

		String cause = Utils.getEnumName(event.getCause().name());

		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			cause += " §8(§7" + damager.getName() + "§8)";
		} else if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
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
			cause += " §8(§7" + Utils.getEnumName(event.getDamager().getType().name()) + "§8)";
		}

		String finalCause = cause;
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			if (event.isCancelled()) return;
			handleDamage(finalCause, event.getFinalDamage(), player);
		}, 1);
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

		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().DAMAGE_PREFIX + message);

	}

}
