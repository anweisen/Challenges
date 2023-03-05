package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;
import net.anweisen.utilities.bukkit.utils.logging.Logger;
import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.EntityTargetAction;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.RandomPotionEffectChallenge;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class RandomPotionEffectAction extends EntityTargetAction {

	public RandomPotionEffectAction(String name) {
		super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(true).addChild(
				SubSettingsHelper.createPotionSettingsBuilder(false, true)));
	}


	@Override
	public void executeFor(Entity entity, Map<String, String[]> subActions) {
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			PotionEffectType randomEffect = RandomPotionEffectChallenge
					.getNewRandomEffect(livingEntity);

			if (randomEffect == null) return;
			int length = Integer.parseInt(subActions.get("length")[0]);
			String[] length_multipliers = subActions.get("length_multiplier");
			int length_multiplier = 1;
			if(length_multipliers != null){
				length_multiplier= Integer.parseInt(length_multipliers[0]);
			}

			List<Player> ingamePlayers =
				ChallengeAPI.getIngamePlayers();

			Challenges.getInstance().getLogger().debug("length and length_multiplier",length, length_multiplier);
			Logger.debug("length and length_multiplier",length, length_multiplier);
			PotionEffect effect = randomEffect.createEffect((length * 20 + 1) * length,
					Integer.parseInt(subActions.get("amplifier")[0]));

			livingEntity.addPotionEffect(effect);
		}
	}

	@Override
	public Material getMaterial() {
		return Material.BREWING_STAND;
	}

}
