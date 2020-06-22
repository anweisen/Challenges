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
 * @author anweisen
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 */

public class PlayerRespawnSetting extends Setting {

	public PlayerRespawnSetting() {
		this.menu = MenuType.DIFFICULTY;
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		Challenges.getInstance().getPlayerManager().setPlayerRespawn(true);
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {
		Challenges.getInstance().getPlayerManager().setPlayerRespawn(false);
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.RED_BED, ItemTranslation.RESPAWN).build();
	}
}
