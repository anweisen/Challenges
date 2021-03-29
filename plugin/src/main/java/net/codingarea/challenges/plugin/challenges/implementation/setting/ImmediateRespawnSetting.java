package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.commons.anntations.Since;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class ImmediateRespawnSetting extends Setting {

	public ImmediateRespawnSetting() {
		super(MenuType.SETTINGS);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.TOTEM_OF_UNDYING, Message.forName("item-immediate-respawn-setting"));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		if (!isEnabled()) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getEntity().spigot().respawn(), 1);
	}

}