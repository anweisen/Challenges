package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since .
 */
public class NoHitDelaySetting extends Setting {

	public NoHitDelaySetting() {
		super(MenuType.SETTINGS);
	}

	@EventHandler
	public void onDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) return;
		if (!shouldExecuteEffect()) return;
		Bukkit.getScheduler().runTaskLater(Challenges.getInstance(), () -> {
			((LivingEntity)event.getEntity()).setNoDamageTicks(-1);
		}, 1);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.FEATHER, Message.forName("item-no-hit-delay-setting"));
	}

}