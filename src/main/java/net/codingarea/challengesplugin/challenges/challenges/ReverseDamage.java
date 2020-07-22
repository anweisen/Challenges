package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.settings.DamageDisplay;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class ReverseDamage extends Setting implements Listener {

	public ReverseDamage() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.GOLDEN_SWORD, ItemTranslation.REVERSE_DAMAGE).build();
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;

		try {

			Player damager;
			if (event.getDamager().getType() == EntityType.PLAYER) {
				damager = (Player) event.getDamager();
				damager.damage(event.getDamage());
				if (damager.getNoDamageTicks() > 0) return;
			} else if (event.getDamager() instanceof Projectile) {
				Projectile damagerProjectile = (Projectile) event.getDamager();
				if (!(damagerProjectile.getShooter() instanceof Player)) return;
				damager = (Player) damagerProjectile.getShooter();
				damager.damage(event.getDamage());
				if (damager.getNoDamageTicks() > 0) return;
			} else {
				return;
			}

			DamageDisplay.handleDamage(Utils.getEnumName("reverse_damage"), event.getDamage(), damager);

		} catch (NullPointerException ignored) { }
	}

}
