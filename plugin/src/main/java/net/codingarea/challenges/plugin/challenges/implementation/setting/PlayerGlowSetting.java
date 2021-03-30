package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.policy.TimerPolicy;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class PlayerGlowSetting extends Setting {

	public PlayerGlowSetting() {
		super(MenuType.SETTINGS);
	}


	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GLASS_BOTTLE, Message.forName("item-glow-setting"));
	}

	@Override
	protected void onEnable() {
		playEffects();
	}

	@Override
	protected void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.removePotionEffect(PotionEffectType.GLOWING);
		}
	}

	@ScheduledTask(ticks = 20*60, async = false, timerPolicy = TimerPolicy.ALWAYS)
	public void playEffects() {
		if (!shouldExecuteEffect()) return;
		Bukkit.getOnlinePlayers().forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, true, false, false)));
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		playEffects();
	}

}
