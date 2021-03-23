package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.anntations.Since;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Since("2.0")
public class BlockPlaceDamage extends SettingModifier {

	public BlockPlaceDamage() {
		super(MenuType.CHALLENGES, 1, 40);
	}

	@EventHandler
	public void onBreak(BlockPlaceEvent event) {
		if (!shouldExecuteEffect()) return;
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
		return new ItemBuilder(Material.GOLD_BLOCK, Message.forName("item-block-place-damage-challenge"));
	}

}