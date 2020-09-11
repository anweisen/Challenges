package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.challenges.difficulty.SplitHealthSetting;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 07-23-2020
 * https://github.com/anweisen
 */

public class AchievmentDamage extends Setting implements Listener {

	public AchievmentDamage() {
		super(MenuType.CHALLENGES);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.BOOK, ItemTranslation.ACHIEVMENT_DAMAGE).build();
	}

	@EventHandler
	public void onAchievmentGet(PlayerAdvancementDoneEvent event) {
		if (!enabled) return;
		event.getPlayer().damage(1);
		SplitHealthSetting.sync(event.getPlayer());
	}

}
