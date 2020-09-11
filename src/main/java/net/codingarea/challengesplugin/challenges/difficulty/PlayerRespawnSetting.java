package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-06-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PlayerRespawnSetting extends Setting {

	public PlayerRespawnSetting() {
		super(MenuType.DIFFICULTY);
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
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.RED_BED, ItemTranslation.RESPAWN).build();
	}

}
