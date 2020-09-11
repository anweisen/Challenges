package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 07-11-2020
 * https://github.com/anweisen
 */

public class NoHitDelay extends Setting implements Listener {

	public NoHitDelay() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.FEATHER, ItemTranslation.NO_HIT_DELAY).build();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			((LivingEntity)event.getEntity()).setNoDamageTicks(-1);
		}, 1);
	}

}
