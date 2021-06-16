package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class PvPSetting extends Setting {

	public PvPSetting() {
		super(MenuType.SETTINGS, true);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.STONE_SWORD, Message.forName("item-pvp-setting"));
	}

	@EventHandler
	public void onDamage(@Nonnull EntityDamageByEntityEvent event) {
		if (isEnabled()) return;
		if (!(event.getEntity() instanceof Player)) return;

		Entity damager = event.getDamager();
		if (damager instanceof Player) {
			event.setCancelled(true);
		} else if (damager instanceof Projectile) {
			Projectile projectile = (Projectile) damager;
			ProjectileSource shooter = projectile.getShooter();
			if (shooter instanceof Player) {
				event.setCancelled(true);
			}
		}
	}

}
