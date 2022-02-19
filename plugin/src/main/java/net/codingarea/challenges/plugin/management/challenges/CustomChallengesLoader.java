package net.codingarea.challenges.plugin.management.challenges;

import java.util.LinkedList;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.ChallengeMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallengesLoader extends ModuleChallengeLoader {

	private final Map<UUID, CustomChallenge> customChallenges = new HashMap<>();

	public CustomChallengesLoader() {
		super(Challenges.getInstance());
	}

	public CustomChallenge registerCustomChallenge(@Nonnull UUID uuid, Material material, String name, ChallengeCondition condition,
			Map<String, String[]> subConditions, ChallengeAction action, Map<String, String[]> subActions, boolean generate) {
		CustomChallenge challenge = customChallenges.getOrDefault(uuid, new CustomChallenge(MenuType.CUSTOM, uuid, material, name, condition, subConditions, action, subActions));
		if (!customChallenges.containsKey(uuid)) {
			customChallenges.put(uuid, challenge);
			register(challenge);
		} else {
			challenge.applySettings(material, name, condition, subConditions, action, subActions);
		}
		generateCustomChallenge(challenge, false, generate);
		return challenge;
	}

	public boolean unregisterCustomChallenge(@Nonnull UUID uuid) {
		CustomChallenge challenge = customChallenges.remove(uuid);
		if (challenge == null) return false;
		Challenges.getInstance().getChallengeManager().unregister(challenge);
		unregister(challenge);
		generateCustomChallenge(challenge, true, true);
		return true;
	}

	public void loadCustomChallengesFrom(@Nonnull Document document) {
		customChallenges.clear();
		Challenges.getInstance().getChallengeManager().unregisterIf(iChallenge -> iChallenge.getType() == MenuType.CUSTOM);
		((ChallengeMenuGenerator) MenuType.CUSTOM.getMenuGenerator()).resetChallengeCache();

		for (String key : document.keys()) {
			try {
				Document doc = document.getDocument(key);

				UUID uuid = UUID.fromString(key);
				String name = doc.getString("name");
				Material material = doc.getEnum("material", Material.class);
				ChallengeCondition condition = doc.getEnum("condition", ChallengeCondition.class);
				Map<String, String[]> subConditions = MapUtils.createSubSettingsMapFromDocument(doc.getDocument("subConditions"));
				ChallengeAction action = doc.getEnum("action", ChallengeAction.class);
				Map<String, String[]> subActions = MapUtils.createSubSettingsMapFromDocument(doc.getDocument("subActions"));

				CustomChallenge challenge = registerCustomChallenge(uuid, material, name, condition, subConditions, action, subActions, false);
				challenge.setEnabled(doc.getBoolean("enabled"));

			} catch (Exception exception) {
				Challenges.getInstance().getLogger().error("Something went wrong while initializing custom challenge {} :: {}", key, exception.getMessage());
				exception.printStackTrace();
			}

		}



		MenuType.CUSTOM.getMenuGenerator().generateInventories();
	}

	public void resetChallenges() {
		customChallenges.clear();
		Challenges.getInstance().getChallengeManager().unregisterIf(iChallenge -> iChallenge.getType() == MenuType.CUSTOM);
		((ChallengeMenuGenerator) MenuType.CUSTOM.getMenuGenerator()).resetChallengeCache();
	}

	private void generateCustomChallenge(CustomChallenge challenge, boolean deleted, boolean generate) {
		MenuGenerator generator = challenge.getType().getMenuGenerator();
		if (generator instanceof ChallengeMenuGenerator) {
			ChallengeMenuGenerator menuGenerator = (ChallengeMenuGenerator) generator;
			if (deleted) {
				menuGenerator.removeChallengeFromCache(challenge);
				if (generate) generator.generateInventories();
			} else {
				if (!menuGenerator.isInChallengeCache(challenge)) {
					menuGenerator.addChallengeToCache(challenge);
					if (generate) generator.generateInventories();
				} else {
					menuGenerator.updateItem(challenge);
				}

			}
		}
	}

	public List<CustomChallenge> getCustomChallengesByCondition(@Nonnull IChallengeCondition condition) {
		List<CustomChallenge> challenges = new LinkedList<>();

		for (CustomChallenge challenge : customChallenges.values()) {
			if (challenge.getCondition() != null && challenge.getCondition().getConditionInterface() == condition) {
				challenges.add(challenge);
			}
		}

		return challenges;
	}

	public void executeCondition(@Nonnull IChallengeCondition condition, Entity entity, Map<String, List<String>> data) {
		getCustomChallengesByCondition(condition).forEach(customChallenge -> customChallenge.onConditionFulfilled(entity, data));
	}

	public Map<UUID, CustomChallenge> getCustomChallenges() {
		return customChallenges;
	}

}
