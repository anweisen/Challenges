package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.LeatherArmorBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DamagePerBlockChallenge extends SettingModifier {

	public DamagePerBlockChallenge() {
		super(MenuType.CHALLENGES, 1, 40);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onMove(@Nonnull PlayerMoveEvent event) {
		if (!shouldExecuteEffect()) return;
		if (BlockUtils.isSameBlockIgnoreHeight(event.getTo(), event.getFrom())) return;

		event.getPlayer().setNoDamageTicks(0);
		event.getPlayer().damage(getValue());
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChallengeHeartsValueChangeTitle(this);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new LeatherArmorBuilder(Material.LEATHER_BOOTS, Message.forName("item-damage-block-challenge")).setColor(Color.RED);
	}

}