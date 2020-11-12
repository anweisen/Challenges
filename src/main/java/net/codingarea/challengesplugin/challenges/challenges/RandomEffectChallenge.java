/**
 * .d88b          8 w                  db                    
 * 8P    .d8b. .d88 w 8d8b. .d88      dPYb   8d8b .d88b .d88 
 * 8b    8' .8 8  8 8 8P Y8 8  8     dPwwYb  8P   8.dP' 8  8 
 * `Y88P `Y8P' `Y88 8 8   8 `Y88    dP    Yb 8    `Y88P `Y88 
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀wwdP
 */
package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.challengetypes.AutoTimeAdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder.TippedArrowBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dominik https://github.com/kxmischesdomi
 * @author anweisen https://github.com/anweisen
 * @since 1.4
 */
public class RandomEffectChallenge extends AutoTimeAdvancedChallenge {

	public RandomEffectChallenge() {
		super(MenuType.CHALLENGES, 30, 1, 1);
		value = 10;
		setNextSeconds();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		value = 10;
		onValueChange(event);
	}

	@Override
	public void onDisable(ChallengeEditEvent event) {

	}

	@Override
	public void onTimeActivation() {
		setNextSeconds();

		PotionEffectType type = getRandomType();

		for (Player player : Bukkit.getOnlinePlayers()) {
			player.addPotionEffect(new PotionEffect(type, 30*20, 0, false));
		}

	}

	private PotionEffectType getRandomType() {
		return PotionEffectType.values()[ThreadLocalRandom.current().nextInt(PotionEffectType.values().length)];
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new TippedArrowBuilder(Material.SPLASH_POTION , PotionType.INSTANT_DAMAGE, ItemTranslation.RANDOM_EFFECTS).build();
	}

	@Override
	protected int getSeconds(int value) {
		return value*10;
	}

}