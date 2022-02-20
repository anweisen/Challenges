package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.Random;
import net.anweisen.utilities.bukkit.utils.item.ItemBuilder.PotionBuilder;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.IChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public enum ChallengeAction implements IChallengeParam {

	KILL(Material.COMMAND_BLOCK, "kill", IChallengeAction.KILL, createEntityTargetSettingsBuilder(true)),
	DAMAGE(Material.FERMENTED_SPIDER_EYE, "damage", IChallengeAction.DAMAGE, createEntityTargetSettingsBuilder(true).createChooseItemChild("damage").fill(builder -> {
		String prefix = Message.forName("item-prefix").asString();
		for (int i = 1; i < 21; i++) {
			builder.addSetting(i + "", new ItemBuilder(Material.RED_DYE, prefix + "§7" + (i / 2f) + " §c❤").setAmount(i).build());
		}
	})),
	SPAWN_RANDOM_MOB(Material.BLAZE_SPAWN_EGG, "random_mob", IChallengeAction.SPAWN_RANDOM_MOB, createEntityTargetSettingsBuilder(false)),
	RANDOM_ITEM(Material.BEACON, "random_item", IChallengeAction.RANDOM_ITEM, createEntityTargetSettingsBuilder(false)),
	UNCRAFT_INVENTORY(Material.CRAFTING_TABLE, "uncraft_inventory", IChallengeAction.UNCRAFT_INVENTORY, createEntityTargetSettingsBuilder(false)),
	BOOST_IN_AIR(Material.FEATHER, "boost_in_air", IChallengeAction.BOOST_IN_AIR, createEntityTargetSettingsBuilder(false)),
	POTION_EFFECT(Material.POTION, "potion_effect", IChallengeAction.POTION_EFFECT, createEntityTargetSettingsBuilder(true).addChild(createPotionSettingsBuilder(true, true)))
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

	public static SubSettingsBuilder createPotionSettingsBuilder(boolean potionType, boolean potionTime) {

		SubSettingsBuilder potionSettings = SubSettingsBuilder.createValueItem().fill(builder -> {

			if (potionTime) {
				builder.addModifierSetting("length", new ItemBuilder(Material.CLOCK,
						Message.forName("item-random-effect-length-challenge")),
						30, 1, 60,
						value -> "",
						value -> Message.forName(value == 1 ? "second" : "seconds").asString());
			}
			builder.addModifierSetting("amplifier", new ItemBuilder(Material.STONE_SWORD,
							Message.forName("item-random-effect-amplifier-challenge")),
					3, 1, 8,
					value -> Message.forName("amplifier").asString());
		});

		if (potionType) {
			potionSettings = potionSettings.createChooseItemChild("potion_type").fill(builder -> {
				for (PotionEffectType effectType : PotionEffectType.values()) {
					builder.addSetting(effectType.getName(), new PotionBuilder(Material.POTION,
							DefaultItem.getItemPrefix() + StringUtils.getEnumName(effectType.getName()))
							.addEffect(effectType.createEffect(1, 0))
							.color(effectType.getColor()).build());

				}
			});
		}


		return potionSettings;
	}

}
