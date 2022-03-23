package net.codingarea.challenges.plugin.challenges.type.helper;

import net.anweisen.utilities.bukkit.utils.item.ItemBuilder.PotionBuilder;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseMultipleItemSubSettingBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.1
 */
public class SubSettingsHelper {

	public static final String
			ENTITY_TYPE = "entity_type",
			BLOCK = "block",
			ANY = "any",
			ITEM = "item",
			LIQUID = "liquid",
			TARGET_ENTITY = "target_entity";

	public static ChooseMultipleItemSubSettingBuilder createEntityTypeSettingsBuilder() {
		return SubSettingsBuilder.createChooseMultipleItem(ENTITY_TYPE).fill(builder -> {
			builder.addSetting(ANY, new ItemBuilder(
					Material.NETHER_STAR, Message.forName("item-custom-setting-entity_type-any")).build());
			builder.addSetting("PLAYER", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-setting-entity_type-player")).build());
			for (EntityType type : EntityType.values()) {
				if (!type.isSpawnable() || !type.isAlive()) continue;
				try {
					Material spawnEgg = Material.valueOf(type.name() + "_SPAWN_EGG");
					builder.addSetting(type.name(), new ItemBuilder(spawnEgg,
							DefaultItem.getItemPrefix() + StringUtils.getEnumName(type)).build());
				} catch (Exception ex) {
					builder.addSetting(type.name(), new ItemBuilder(Material.STRUCTURE_VOID,
							DefaultItem.getItemPrefix() + StringUtils.getEnumName(type)));
				}
			}
		});
	}

	public static ChooseMultipleItemSubSettingBuilder createBlockSettingsBuilder() {
		return SubSettingsBuilder.createChooseMultipleItem(BLOCK).fill(builder -> {
			builder.addSetting(ANY, new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-setting-block-any")).build());
			for (Material material : Material.values()) {
				if (material.isBlock() && material.isItem() && !BukkitReflectionUtils.isAir(material)) {
					builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils.getEnumName(material)).build());
				}
			}
		});
	}

	public static SubSettingsBuilder createEntityTargetSettingsBuilder(boolean everyMob) {
		return createEntityTargetSettingsBuilder(everyMob, false);
	}

	public static SubSettingsBuilder createEntityTargetSettingsBuilder(boolean everyMob, boolean onlyPlayer) {
		ChooseItemSubSettingsBuilder builder = SubSettingsBuilder.createChooseItem(
				TARGET_ENTITY);
		if (!onlyPlayer) {
			builder.addSetting("current", new ItemBuilder(Material.DRAGON_HEAD,
							Message.forName("item-custom-setting-target-current")).build());
		}

		builder.addSetting("current_player", new ItemBuilder(Material.PLAYER_HEAD,
				Message.forName("item-custom-setting-target-current_player")).build());
		builder.addSetting("random_player", new ItemBuilder(Material.ZOMBIE_HEAD,
				Message.forName("item-custom-setting-target-random_player")).build());
		builder.addSetting("every_player", new ItemBuilder(Material.PLAYER_HEAD,
				Message.forName("item-custom-setting-target-every_player")).build());

		if (everyMob && !onlyPlayer) {
			builder.addSetting("every_mob", new ItemBuilder(Material.WITHER_SKELETON_SKULL,
					Message.forName("item-custom-setting-target-every_mob")).build());
			builder.addSetting("every_mob_except_current", new ItemBuilder(Material.SKELETON_SKULL,
					Message.forName("item-custom-setting-target-every_mob_except_current")).build());
			builder.addSetting("every_mob_except_players", new ItemBuilder(Material.SKELETON_SKULL,
					Message.forName("item-custom-setting-target-every_mob_except_players")).build());
		}
		return builder;
	}

	public static SubSettingsBuilder createPotionSettingsBuilder(boolean potionType,
			boolean potionTime) {

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
					value -> Message.forName("amplifier").asString(),
					integer -> "");
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
