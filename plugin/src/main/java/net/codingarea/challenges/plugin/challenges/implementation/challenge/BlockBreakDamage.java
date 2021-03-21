package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockBreakDamage extends SettingModifier {

	public BlockBreakDamage() {
		super(MenuType.CHALLENGES, 1, 40);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (!shouldExecuteEffect()) return;
		event.getPlayer().damage(getValue());
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.GOLDEN_PICKAXE, Message.forName("item-block-break-damage-challenge"));
	}

}