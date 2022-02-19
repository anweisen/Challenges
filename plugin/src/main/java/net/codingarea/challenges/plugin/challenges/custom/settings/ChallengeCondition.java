package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.function.Supplier;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.IChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.BlockMoveCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.BreakBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.ConsumeItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.EntityDamageByPlayerCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.EntityDamageCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.EntityDeathCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.IntervallCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.PlaceBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.PlayerJumpCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation.PlayerSneakCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseMultipleItemSubSettingBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public enum ChallengeCondition implements IChallengeParam {

	INTERVALL(Material.CLOCK, "intervall", new IntervallCondition(), () -> {
		return SubSettingsBuilder.createChooseItem("time").fill(builder -> {
			builder.addSetting("1", new ItemBuilder(Material.MUSIC_DISC_13, Message.forName("item-custom-condition-intervall-second"), "1").build());
			String seconds = "item-custom-condition-intervall-seconds";
			builder.addSetting("2", new ItemBuilder(Material.MUSIC_DISC_CAT, Message.forName(seconds), "2"));
			builder.addSetting("5", new ItemBuilder(Material.MUSIC_DISC_BLOCKS, Message.forName(seconds), "5"));
			builder.addSetting("10", new ItemBuilder(Material.MUSIC_DISC_CHIRP, Message.forName(seconds), "10"));
			builder.addSetting("20", new ItemBuilder(Material.MUSIC_DISC_FAR, Message.forName(seconds), "20"));
			builder.addSetting("30", new ItemBuilder(Material.MUSIC_DISC_MALL, Message.forName(seconds), "30"));
			builder.addSetting("60", new ItemBuilder(Material.MUSIC_DISC_MELLOHI, Message.forName(seconds), "60"));
			String minutes = "item-custom-condition-intervall-minutes";
			builder.addSetting("120", new ItemBuilder(Material.MUSIC_DISC_STAL, Message.forName(minutes), "2"));
			builder.addSetting("180", new ItemBuilder(Material.MUSIC_DISC_STRAD, Message.forName(minutes), "3"));
			builder.addSetting("240", new ItemBuilder(Material.MUSIC_DISC_WARD, Message.forName(minutes), "4"));
			builder.addSetting("300", new ItemBuilder(Material.MUSIC_DISC_11, Message.forName(minutes), "5"));
		});
	}),
	PLAYER_JUMP(Material.RABBIT_FOOT, "jump", new PlayerJumpCondition()),
	PLAYER_SNEAK(Material.SANDSTONE_SLAB, "sneak", new PlayerSneakCondition()),
	PLAYER_MOVE_BLOCK(Material.LEATHER_BOOTS, "move_block", new BlockMoveCondition()),
	BLOCK_BREAK(Material.GOLDEN_PICKAXE, "block_break", new BreakBlockCondition(), ChallengeCondition::createBlockSettingsBuilder),
	BLOCK_PLACE(Material.BRICKS, "block_place", new PlaceBlockCondition(), ChallengeCondition::createBlockSettingsBuilder),
	ENTITY_DEATH(Material.BONE, "death", new EntityDeathCondition(), ChallengeCondition::createEntityTypeSettingsBuilder),
	ENTITY_DAMAGE(Material.FLINT_AND_STEEL, "damage", new EntityDamageCondition(), ChallengeCondition::createEntityTypeSettingsBuilder),
	ENTITY_DAMAGE_BY_PLAYER(Material.WOODEN_SWORD, "damage_by_player", new EntityDamageByPlayerCondition(), ChallengeCondition::createEntityTypeSettingsBuilder),
	CONSUME_ITEM(Material.COOKED_BEEF, "consume_item", new ConsumeItemCondition(), () ->
			SubSettingsBuilder.createChooseMultipleItem("item").fill(builder -> {
				for (Material material : Material.values()) {
					if (material.isEdible()) {
						builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils.getEnumName(material)).build());
					}
				}
	})),
	;

	private final Material material;
	private final String message;
	private final IChallengeCondition condition;
	private final SubSettingsBuilder subSettingsBuilder;
	private final Class<? extends IChallengeAction> actionClassTarget; // NOT IMPLEMENTED

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget, SubSettingsBuilder subSettingsBuilder) {
		this.material = material;
		this.message = "item-custom-condition-" + messageSuffix;
		this.condition = condition;
		this.subSettingsBuilder = subSettingsBuilder.build();
		this.actionClassTarget = actionClassTarget;
		Bukkit.getPluginManager().registerEvents(condition, Challenges.getInstance());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget, Supplier<SubSettingsBuilder> builderSupplier) {
		this(material, messageSuffix, condition, actionClassTarget, builderSupplier.get());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget) {
		this(material, messageSuffix, condition, actionClassTarget, SubSettingsBuilder.createEmpty());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, SubSettingsBuilder subSettingsBuilder) {
		this(material, messageSuffix, condition, IChallengeAction.class, subSettingsBuilder);
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Supplier<SubSettingsBuilder> builderSupplier) {
		this(material, messageSuffix, condition, builderSupplier.get());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition) {
		this(material, messageSuffix, condition, SubSettingsBuilder.createEmpty());
	}

	public Class<? extends IChallengeAction> getActionClassTarget() {
		return actionClassTarget;
	}

	public Material getMaterial() {
		return material;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public IChallengeParam[] getValues() {
		return values();
	}

	public IChallengeCondition getConditionInterface() {
		return condition;
	}

	public SubSettingsBuilder getSubSettingsBuilder() {
		return subSettingsBuilder;
	}

	public static ChooseItemSubSettingsBuilder createEntityTypeSettingsBuilder() {
		return SubSettingsBuilder.createChooseItem("entity_type").fill(builder -> {
			builder.addSetting("any", new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-condition-entity_type-any")).build());
			builder.addSetting("PLAYER", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-condition-entity_type-player")).build());
			for (EntityType type : EntityType.values()) {
				try {
					Material spawnEgg = Material.valueOf(type.name() + "_SPAWN_EGG");
					builder.addSetting(type.name(), new ItemBuilder(spawnEgg, DefaultItem.getItemPrefix() + StringUtils.getEnumName(type)).build());
				} catch (Exception ex) { }
			}
		});
	}

	public static ChooseMultipleItemSubSettingBuilder createBlockSettingsBuilder() {
		return SubSettingsBuilder.createChooseMultipleItem("block").fill(builder -> {
			builder.addSetting("any", new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-condition-block-any")).build());
			for (Material material : Material.values()) {
				if (material.isBlock() && material.isItem() && !BukkitReflectionUtils.isAir(material)) {
					builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils.getEnumName(material)).build());
				}
			}
		});
	}

}
