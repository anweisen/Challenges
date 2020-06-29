package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 06-23-2020
 * https://github.com/anweisen
 */

public class NoHungerSetting extends Setting implements Listener {

	public NoHungerSetting() {
		menu = MenuType.DIFFICULTY;
	}

	@Override
	public String getChallengeName() {
		return "nohunger";
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		Utils.forEachPlayerOnline((player -> player.setFoodLevel(20)));
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.BREAD, ItemTranslation.NO_HUNGER).hideAttributes().build();
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (!enabled) return;
		event.setCancelled(true);
		((Player)event.getEntity()).setFoodLevel(20);
	}

}
