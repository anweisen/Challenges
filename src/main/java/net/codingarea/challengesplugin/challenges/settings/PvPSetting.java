package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class PvPSetting extends Setting implements Listener {

	public PvPSetting() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.STONE_SWORD, ItemTranslation.PVP).hideAttributes().build();
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {

		if (enabled) return;
		if (event.getEntityType() != EntityType.PLAYER) return;

		if (event.getDamager().getType() == EntityType.PLAYER) {
			event.setCancelled(true);
		} else if (event.getDamager() instanceof Projectile) {
			if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
				event.setCancelled(true);
			}
		}


	}

}
