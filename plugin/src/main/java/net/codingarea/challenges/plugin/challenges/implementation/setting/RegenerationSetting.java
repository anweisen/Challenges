package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.challenges.type.Modifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RegenerationSetting extends Modifier {

	public RegenerationSetting() {
		super(MenuType.SETTINGS, 1, 3, 2);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new PotionBuilder(Material.POTION, Message.forName("item-regeneration-setting")).setColor(Color.RED);
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == 1) {
			return DefaultItem.disabled();
		} else if (getValue() == 2) {
			return DefaultItem.enabled();
		}
		return DefaultItem.create(Material.ORANGE_DYE, "§6Not natural");
	}

	@Override
	public void playValueChangeTitle() {
		if (getValue() == 1) {
			ChallengeHelper.playToggleChallengeTitle(this, false);
			return;
		}
		ChallengeHelper.playChangeChallengeValueTitle(this, getValue() == 2 ? "§aEnabled" : "§6Not natural");
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player)) return;

		if (getValue() == 1) {
			event.setAmount(0);
			event.setCancelled(true);
			return;
		}

		if (getValue() == 3) {
			if (event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN) {
				event.setAmount(0);
				event.setCancelled(true);
			}
		}

	}

}