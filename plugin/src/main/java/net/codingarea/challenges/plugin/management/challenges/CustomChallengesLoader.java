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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallengesLoader extends ModuleChallengeLoader {

	private final Map<UUID, CustomChallenge> customChallenges = new HashMap<>();

	public CustomChallengesLoader() {
		super(Challenges.getInstance());
	}

	public void registerCustomChallenge(@Nonnull UUID uuid, Material material, String name, ChallengeCondition condition, String[] subConditions, ChallengeAction action, String[] subActions, boolean generate) {
		CustomChallenge challenge = customChallenges.getOrDefault(uuid, new CustomChallenge(MenuType.CUSTOM, uuid, material, name, condition, subConditions, action, subActions));
		if (!customChallenges.containsKey(uuid)) {
			customChallenges.put(uuid, challenge);
			challenge.setEnabled(true);
		} else {
			challenge.applySettings(material, name, condition, subConditions, action, subActions);
		}
		if (generate) generateCustomChallenge(challenge, false);
	}

	public void deleteCustomChallenge(@Nonnull UUID uuid) {
		unregisterCustomChallenge(uuid);
	}

	public void unregisterCustomChallenge(@Nonnull UUID uuid) {
		CustomChallenge challenge = customChallenges.remove(uuid);
		generateCustomChallenge(challenge, true);
	}

	public void onSettingsLoad(@Nonnull Document document) {
		customChallenges.clear();

		for (String key : document.keys()) {
			if (key.startsWith("custom-")) {
				try {
					Document doc = document.getDocument(key);

					UUID uuid = UUID.fromString(key.replaceFirst("custom-", ""));
					String name = doc.getString("name");
					Material material = doc.getEnum("material", Material.class);
					ChallengeCondition condition = doc.getEnum("condition", ChallengeCondition.class);
					String[] subConditions = doc.getStringArray("subConditions");
					ChallengeAction action = doc.getEnum("action", ChallengeAction.class);
					String[] subActions = doc.getStringArray("subActions");

					registerCustomChallenge(uuid, material, name, condition, subConditions, action, subActions, false);

				} catch (Exception exception) {
					new Exception("Something went wrong while initializing custom challenge " + key + " :: " + exception.getMessage(), exception).printStackTrace();
				}

			}

		}

		MenuType.CUSTOM.getMenuGenerator().generateInventories();
	}

	private void generateCustomChallenge(CustomChallenge challenge, boolean deleted) {
		MenuGenerator generator = challenge.getType().getMenuGenerator();
		if (generator instanceof ChallengeMenuGenerator) {
			ChallengeMenuGenerator menuGenerator = (ChallengeMenuGenerator) generator;
			if (deleted) {
				menuGenerator.removeChallengeFromCache(challenge);
				generator.generateInventories();
			} else {
				if (!menuGenerator.isInChallengeCache(challenge)) {
					menuGenerator.addChallengeToCache(challenge);
					generator.generateInventories();
					register(challenge);
				} else {
					menuGenerator.updateItem(challenge);
				}

			}
		}
	}

	public List<CustomChallenge> getCustomChallengesByCondition(@Nonnull IChallengeCondition condition) {
		return customChallenges.values().stream().filter(customChallenge -> customChallenge.getCondition().getCondition() == condition).collect(Collectors.toList());
	}

	public void executeCondition(@Nonnull IChallengeCondition condition, @Nonnull Entity entity, String... data) {
		getCustomChallengesByCondition(condition).forEach(customChallenge -> customChallenge.onConditionFulfilled(entity, data));
	}

}
