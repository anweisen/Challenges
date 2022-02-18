package net.codingarea.challenges.plugin.challenges.custom.settings;

import net.codingarea.challenges.plugin.challenges.custom.settings.action.IChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public enum ChallengeAction implements IChallengeParam {

	KILL(Material.COMMAND_BLOCK, "kill", IChallengeAction.KILL, createEntityTargetSettingsBuilder(true).createValueChild().fill(builder -> {
		builder.addBooleanSetting(new ItemBuilder(Material.TURTLE_EGG, "TEST"), true);
	})),
	DAMAGE(Material.FERMENTED_SPIDER_EYE, "damage", IChallengeAction.DAMAGE, createEntityTargetSettingsBuilder(true).createChooseItemChild("damage").fill(builder -> {
		String prefix = Message.forName("item-prefix").asString();
		for (int i = 1; i < 21; i++) {
			builder.addSetting(i + "", new ItemBuilder(Material.RED_DYE, prefix + "§7" + (i / 2f) + " §c❤").setAmount(i).build());
		}
	})),
	SPAWN_RANDOM_MOB(Material.BLAZE_SPAWN_EGG, "random_mob", IChallengeAction.SPAWN_RANDOM_MOB, createEntityTargetSettingsBuilder(false)),
	;

	public static final Random random = new Random();

	private final Material material;
	private final IChallengeAction action;
	private final String message;
	private final SubSettingsBuilder subSettingsBuilder;

	ChallengeAction(Material material, String messageSuffix, IChallengeAction action, SubSettingsBuilder subSettingsBuilder) {
		this.material = material;
		this.message = "item-custom-action-" + messageSuffix;
		this.action = action;
		this.subSettingsBuilder = subSettingsBuilder.build();
	}

	ChallengeAction(Material material, String messageSuffix, IChallengeAction action) {
		this(material, messageSuffix, action, SubSettingsBuilder.createChooseItem("none"));
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

	@Override
	public IChallengeParam[] getValues() {
		return values();
	}

	public static SubSettingsBuilder createEntityTargetSettingsBuilder(boolean everyMob) {
		ChooseItemSubSettingsBuilder builder = SubSettingsBuilder.createChooseItem("target_entity")
				.addSetting("current", new ItemBuilder(Material.DRAGON_HEAD,
						Message.forName("item-custom-action-target-current")).build());
		if (everyMob) {
			builder.addSetting("every_mob", new ItemBuilder(Material.WITHER_SKELETON_SKULL, Message.forName("item-custom-action-target-every_mob")).build());
			builder.addSetting("every_mob_except_current", new ItemBuilder(Material.SKELETON_SKULL, Message.forName("item-custom-action-target-every_mob_except_current")).build());
			builder.addSetting("every_mob_except_players", new ItemBuilder(Material.SKELETON_SKULL, Message.forName("item-custom-action-target-every_mob_except_players")).build());
		}
		return builder
				.addSetting("random_player", new ItemBuilder(Material.ZOMBIE_HEAD, Message.forName("item-custom-action-target-random_player")).build())
				.addSetting("every_player", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-action-target-every_player")).build())
				.addSetting("current_player", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-action-target-current_player")).build());
	}

}
