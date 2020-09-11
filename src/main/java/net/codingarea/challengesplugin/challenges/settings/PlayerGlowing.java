package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.challengetypes.extra.ISecondExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 06-28-2020
 * https://github.com/anweisen
 */

public class PlayerGlowing extends Setting implements ISecondExecutor {

	public PlayerGlowing() {
		super(MenuType.SETTINGS);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.GLASS_BOTTLE, ItemTranslation.PLAYER_GLOW).build();
	}

	@Override
	public void onTimerSecond() {

		if (!enabled) return;
		for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
			if (currentPlayer.getGameMode() == GameMode.SPECTATOR) continue;
			currentPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 25, 200, true, false, false));
		}
	}
}
