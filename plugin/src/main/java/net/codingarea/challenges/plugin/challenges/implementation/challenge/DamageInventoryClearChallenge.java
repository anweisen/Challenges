package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DamageInventoryClearChallenge extends SettingModifier {

	public DamageInventoryClearChallenge() {
		super(MenuType.CHALLENGES, 1, 2);
	}

	@EventHandler
	public void onDamage(@Nonnull EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (!shouldExecuteEffect()) return;
		if (event.getFinalDamage() <= 0 && event.getDamage(DamageModifier.ABSORPTION) >= 0) return;

		if (getValue() == 1) {
			Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().clear());
		} else {
			Player player = (Player) event.getEntity();
			player.getInventory().clear();
		}
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == 1) {
			return DefaultItem.create(Material.ENDER_CHEST, "§5Everyone");
		} else {
			return DefaultItem.create(Material.PLAYER_HEAD, "§6Player");
		}
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChangeChallengeValueTitle(this, getValue() == 0 ? "§5Everyone" : "§6Player");
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CHEST, Message.forName("item-damage-inv-clear-challenge"));
	}

}