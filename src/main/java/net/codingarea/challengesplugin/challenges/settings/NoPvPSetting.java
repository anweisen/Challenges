package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class NoPvPSetting extends Setting implements Listener {

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.STONE_SWORD, ItemTranslation.PVP).build();
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {

		if (!enabled) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getDamager().getType() != EntityType.PLAYER) return;

		event.setCancelled(true);

	}

}
