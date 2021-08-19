package net.codingarea.challenges.plugin.challenges.custom.api;

import net.codingarea.challenges.plugin.challenges.custom.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.api.action.IChallengeAction;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public enum ChallengeAction {

	SPAWN_RANDOM_MOB(Material.BLAZE_SPAWN_EGG, "random_mob", IChallengeAction.SPAWN_RANDOM_MOB, createTargetSettingsBuilder())
	;

	public static final Random random = new Random();

	private final Material material;
	private final IChallengeAction action;
	private final String message;
	private final SubSettingsBuilder subSettingsBuilder;

	ChallengeAction(Material material, String messageSuffix, IChallengeAction action, SubSettingsBuilder subSettingsBuilder) {
		this.material = material;
		this.message = "custom.condition." + messageSuffix;
		this.action = action;
		this.subSettingsBuilder = subSettingsBuilder;
	}

	ChallengeAction(Material material, String messageSuffix, IChallengeAction action) {
		this(material, messageSuffix, action, SubSettingsBuilder.create());
	}

	public Material getMaterial() {
		return material;
	}

	public String getMessage() {
		return message;
	}

	public IChallengeAction getAction() {
		return action;
	}

	public SubSettingsBuilder getSubSettingsBuilder() {
		return subSettingsBuilder;
	}

	public static SubSettingsBuilder createTargetSettingsBuilder() {
		return SubSettingsBuilder.create()
				.addSetting("current", new ItemBuilder(Material.SKELETON_SKULL, "§7Current Entity").build())
//				.addSetting("every_mob", new ItemBuilder(Material.WITHER_SKELETON_SKULL, "§7All Mobs").build())
				.addSetting("every_player", new ItemBuilder(Material.PLAYER_HEAD, "§7All Players").build())
				.addSetting("current_player", new ItemBuilder(Material.ZOMBIE_HEAD, "§7Current Player").build());
	}

}
