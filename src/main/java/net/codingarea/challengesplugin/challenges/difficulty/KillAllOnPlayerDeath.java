package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class KillAllOnPlayerDeath extends Setting {

	public KillAllOnPlayerDeath() {
		this.menu = MenuType.DIFFICULTY;
		this.enabled = true;
		update();
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.FIRE_CHARGE, ItemTranslation.ONE_TEAM_LIVE).build();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		update();
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		update();
	}

	private void update() {
		Challenges.getInstance().getPlayerManager().setEndOnPlayerDeath(enabled);
	}

}
