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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-22-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class DamageDisplay extends Setting implements Listener {

	public DamageDisplay() {
		menu = MenuType.SETTINGS;
		enabled = true;
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
		if (event.getCause() == DamageCause.CUSTOM || event.getCause() == DamageCause.ENTITY_ATTACK) return;

		Player player = (Player) event.getEntity();

		DecimalFormat format = new DecimalFormat("0.0");

		String message = Translation.PLAYER_DAMAGE.get()
				.replace("%player%", player.getName())
				.replace("%cause%", Utils.getEnumName(event.getCause().name()))
				.replace("%damage%", format.format(event.getDamage() / ((double)2)));

		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().DAMAGE_PREFIX + message);

	}

	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {

		if (!enabled || !Challenges.timerIsStarted()) return;
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getCause() == DamageCause.CUSTOM) return;
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;

		Player player = (Player) event.getEntity();

		String cause = Utils.getEnumName(event.getCause().name());

		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			cause += " §8(§7" + damager.getName() + "§8)";
		} else {
			cause += " §8(§7" + Utils.getEnumName(event.getDamager().getType().name()) + "§8)";
		}

		DecimalFormat format = new DecimalFormat("0.0");

		String message = Translation.PLAYER_DAMAGE.get()
				.replace("%player%", player.getName())
				.replace("%cause%", cause)
				.replace("%damage%", format.format(event.getDamage() / ((double)2)));

		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().DAMAGE_PREFIX + message);

	}

}
