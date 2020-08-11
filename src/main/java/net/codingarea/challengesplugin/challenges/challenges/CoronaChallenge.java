package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.ItemBuilder.LeatherArmorBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 08-05-2020
 * https://github.com/anweisen
 */

public class CoronaChallenge extends Setting implements ITimerStatusExecutor {

	public CoronaChallenge() {
		super(MenuType.CHALLENGES);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Challenges.getInstance(), this::onTick, 10, 10);
	}

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.SLIME_BALL, ItemTranslation.CORONA_CHALLENGE).build();
	}

	private void onTick() {
		if (!enabled) return;
		Utils.forEachPlayerOnline(this::onTick);
	}

	private void onTick(Player player) {

		if (player == null) return;

		List<Entity> nearbyEntities = player.getNearbyEntities(2, 2, 2);
		nearbyEntities.remove(player);
		nearbyEntities.removeIf(entity -> !(entity instanceof LivingEntity));
		nearbyEntities.removeIf(entity -> (entity instanceof Player && ((Player)entity).getGameMode() == GameMode.SPECTATOR));
		if (nearbyEntities.isEmpty()) {
			player.removePotionEffect(PotionEffectType.CONFUSION);
			return;
		}

		if (!Challenges.timerIsStarted()) return;
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

		byte value = 1;
		for (Entity currentEntity : nearbyEntities) {
			if (player.getLocation().distance(currentEntity.getLocation()) <= 1) value = 2;
		}

		if (!(player.getPotionEffect(PotionEffectType.POISON) != null && player.getPotionEffect(PotionEffectType.POISON).getDuration() > 10)
		 && !(player.getPotionEffect(PotionEffectType.WITHER) != null && player.getPotionEffect(PotionEffectType.WITHER).getDuration() > 10)){
			player.addPotionEffect(new PotionEffect((value == 1 ? PotionEffectType.POISON : PotionEffectType.WITHER), 5*20, 7));
		}
		if (!player.hasPotionEffect(PotionEffectType.CONFUSION)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 9));
		}

	}

	public static ItemStack coronaMask() {
		return new LeatherArmorBuilder(Material.LEATHER_HELMET, Translation.CORONA_MASK.get(), Color.WHITE).build();
	}  

}
