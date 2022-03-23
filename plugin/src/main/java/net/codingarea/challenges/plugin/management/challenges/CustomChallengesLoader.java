package net.codingarea.challenges.plugin.management.challenges;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.CustomChallenge;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeExecutionData;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.IChallengeTrigger;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.ChallengeMenuGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.MenuGenerator;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallengesLoader extends ModuleChallengeLoader {

	private final Map<UUID, CustomChallenge> customChallenges = new LinkedHashMap<>();

	public CustomChallengesLoader() {
		super(Challenges.getInstance());
	}

	public CustomChallenge registerCustomChallenge(@Nonnull UUID uuid, Material material, String name, AbstractChallengeTrigger trigger,
			Map<String, String[]> subTriggers, AbstractChallengeAction action, Map<String, String[]> subActions, boolean generate) {
		CustomChallenge challenge = customChallenges.getOrDefault(uuid, new CustomChallenge(MenuType.CUSTOM, uuid, material, name, trigger, subTriggers, action, subActions));
		if (!customChallenges.containsKey(uuid)) {
			customChallenges.put(uuid, challenge);
			register(challenge);
		} else {
			challenge.applySettings(material, name, trigger, subTriggers, action, subActions);
		}
		generateCustomChallenge(challenge, false, generate);
		return challenge;
	}

	public void unregisterCustomChallenge(@Nonnull UUID uuid) {
		CustomChallenge challenge = customChallenges.remove(uuid);
		if (challenge == null) return;
		Challenges.getInstance().getChallengeManager().unregister(challenge);
		unregister(challenge);
		generateCustomChallenge(challenge, true, true);
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
				AbstractChallengeTrigger trigger = Challenges.getInstance().getCustomSettingsLoader().getTriggerByName(doc.getString("trigger"));
				Map<String, String[]> subTriggers = MapUtils.createSubSettingsMapFromDocument(doc.getDocument("subTrigger"));
				AbstractChallengeAction action = Challenges.getInstance().getCustomSettingsLoader().getActionByName(doc.getString("action"));
				Map<String, String[]> subActions = MapUtils.createSubSettingsMapFromDocument(doc.getDocument("subActions"));

				CustomChallenge challenge = registerCustomChallenge(uuid, material, name, trigger, subTriggers, action, subActions, false);
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

	public List<CustomChallenge> getCustomChallengesByTrigger(@Nonnull IChallengeTrigger trigger) {
		List<CustomChallenge> challenges = new LinkedList<>();

		for (CustomChallenge challenge : customChallenges.values()) {
			if (challenge.getTrigger() != null && challenge.getTrigger() == trigger) {
				challenges.add(challenge);
			}
		}

		return challenges;
	}

	public void executeTrigger(@Nonnull ChallengeExecutionData challengeExecutionData) {
		getCustomChallengesByTrigger(challengeExecutionData.getTrigger())
				.forEach(customChallenge -> customChallenge
						.onTriggerFulfilled(challengeExecutionData));
	}

	public Map<UUID, CustomChallenge> getCustomChallenges() {
		return customChallenges;
	}

}
