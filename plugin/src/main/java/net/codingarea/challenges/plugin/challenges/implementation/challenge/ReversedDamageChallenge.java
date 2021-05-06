package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ReversedDamageChallenge extends Setting {

	public ReversedDamageChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GOLDEN_SWORD, Message.forName("item-reversed-damage-challenge"));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (!shouldExecuteEffect()) return;
		LivingEntity damager;
		if (event.getDamager() instanceof Projectile) {
			ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
			if (shooter instanceof Player) {
				damager = (LivingEntity) shooter;
			} else {
				return;
			}
		} else if (event.getDamager() instanceof Player) {
			damager = ((LivingEntity) event.getDamager());
		} else {
			return;
		}

		double damage = event.getFinalDamage();
		damager.damage(damage);
	}

}