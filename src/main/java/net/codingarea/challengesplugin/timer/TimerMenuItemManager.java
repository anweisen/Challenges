package net.codingarea.challengesplugin.timer;

import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.ItemBuilder.TippedArrowBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-15-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class TimerMenuItemManager {

	public ItemStack getTimerStartedItem() {
		return new ItemBuilder(Material.LIME_DYE, Translation.TIMER_STARTED_ITEM.get()).build();
	}

	public ItemStack getTimerStoppedItem() {
		return new ItemBuilder(Material.GRAY_DYE, Translation.TIMER_STOPPED_ITEM.get()).build();
	}

	public ItemStack getTimerModeDownItem() {
		return new TippedArrowBuilder(Material.TIPPED_ARROW, PotionType.STRENGTH, Translation.TIMER_MODE_DOWN_ITEM.get()).hideAttributes().build();
	}

	public ItemStack getTimerModeUpItem() {
		return new TippedArrowBuilder(Material.TIPPED_ARROW, PotionType.JUMP, Translation.TIMER_MODE_UP_ITEM.get()).hideAttributes().build();
	}

}
