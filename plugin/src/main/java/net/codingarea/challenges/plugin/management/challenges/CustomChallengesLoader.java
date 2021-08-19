package net.codingarea.challenges.plugin.management.challenges;

import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.api.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.ChallengeMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallengesLoader extends ModuleChallengeLoader {

	private final List<CustomChallenge> customChallenges = new LinkedList<>();

	public CustomChallengesLoader() {
		super(Challenges.getInstance());
	}

	public CustomChallenge registerCustomChallenge(@Nonnull Material displayItem, @Nonnull ChallengeCondition condition, String[] subConditions, ChallengeAction action, String[] subActions) {
		CustomChallenge challenge = new CustomChallenge(MenuType.CUSTOM, displayItem, condition, subConditions, action, subActions);
		registerCustomChallenge(challenge);
		return challenge;
	}

	public void onSettingsLoad(@Nonnull Document document) {

	}

	private void registerCustomChallenge(@Nonnull CustomChallenge challenge) {
		customChallenges.add(challenge);
		register(challenge);

		MenuGenerator generator = challenge.getType().getMenuGenerator();
		if (generator instanceof ChallengeMenuGenerator) {
			((ChallengeMenuGenerator) generator).addChallengeToCache(challenge);
			generator.generateInventories();
		}
	}

	public List<CustomChallenge> getCustomChallengesByCondition(@Nonnull IChallengeCondition condition) {
		return customChallenges.stream().filter(customChallenge -> customChallenge.getCondition().getCondition() == condition).collect(Collectors.toList());
	}

	public void executeCondition(@Nonnull IChallengeCondition condition, @Nonnull Entity entity, String... data) {
		getCustomChallengesByCondition(condition).forEach(customChallenge -> customChallenge.onConditionFulfilled(entity, data));
	}

}
