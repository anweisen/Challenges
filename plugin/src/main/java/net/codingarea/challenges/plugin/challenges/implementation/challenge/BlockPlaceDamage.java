package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.annotations.Since;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.annotation.Nonnull;

/**
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
		event.getPlayer().damage(getValue());
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GOLD_BLOCK, Message.forName("item-block-place-damage-challenge"));
	}

}