package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * https://github/github
 * Challenges developed on 10.08.2020
 */

public class DeathMessages extends Modifier implements Listener {

	public DeathMessages() {
		super(MenuType.SETTINGS, 3, 1, 3);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.BOW, ItemTranslation.DEATH_MESSAGES).build();
	}

	@Override
	public @NotNull ItemStack getActivationItem() {
		if (value == 1) {
			return ItemManager.getNotActivatedItem();
		} else if (value == 2) {
			return new ItemBuilder(Material.OAK_SIGN, "§eVanilla").build();
		} else {
			return ItemManager.getActivatedItem();
		}
	}

	@Override
	public void onMenuClick(ChallengeEditEvent event) { }

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {

		if (value == 1) {
			event.setDeathMessage(null);
		} else if (value == 3) {

			EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();

			String cause = damageEvent != null ? DamageDisplay.getCause(damageEvent) : Translation.UNDEFINED.get();

			event.setDeathMessage(null);
			Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
				Bukkit.broadcastMessage(Prefix.CHALLENGES + Translation.PLAYER_DEATH.get().replace("%player%", event.getEntity().getName()).replace("%cause%", cause));
			}, 2);

		}

	}

}
