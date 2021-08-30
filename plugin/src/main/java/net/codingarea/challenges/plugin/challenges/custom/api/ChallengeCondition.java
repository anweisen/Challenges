package net.codingarea.challenges.plugin.challenges.custom.api;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.api.action.IChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.action.PlayerChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.api.condition.implementation.EntityDeathCondition;
import net.codingarea.challenges.plugin.challenges.custom.api.condition.implementation.PlayerJumpCondition;
import net.codingarea.challenges.plugin.challenges.custom.api.condition.implementation.PlayerSneakCondition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public enum ChallengeCondition implements IChallengeEnum {

	PLAYER_JUMP(Material.RABBIT_FOOT, "jump", new PlayerJumpCondition(), PlayerChallengeAction.class),
	PLAYER_SNEAK(Material.SANDSTONE_SLAB, "sneak", new PlayerSneakCondition()),
	ENTITY_DEATH(Material.BONE, "death", new EntityDeathCondition(), () -> SubSettingsBuilder.create().fill(builder -> {
		for (EntityType type : EntityType.values()) {
			try {
				builder.addSetting(type.name(), new ItemStack(Material.valueOf(type.name() + "_SPAWN_EGG")));
			} catch (Exception ex) { }
		}
	}))
	;

	private final Material material;
	private final String message;
	private final IChallengeCondition condition;
	private final SubSettingsBuilder subSettingsBuilder;
	private final Class<? extends IChallengeAction> actionClassTarget;

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget, SubSettingsBuilder subSettingsBuilder) {
		this.material = material;
		this.message = "custom-condition-" + messageSuffix;
		this.condition = condition;
		this.subSettingsBuilder = subSettingsBuilder;
		this.actionClassTarget = actionClassTarget;
		Bukkit.getPluginManager().registerEvents(condition, Challenges.getInstance());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget, Supplier<SubSettingsBuilder> builderSupplier) {
		this(material, messageSuffix, condition, actionClassTarget, builderSupplier.get());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Class<? extends IChallengeAction> actionClassTarget) {
		this(material, messageSuffix, condition, actionClassTarget, SubSettingsBuilder.create());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, SubSettingsBuilder subSettingsBuilder) {
		this(material, messageSuffix, condition, IChallengeAction.class, subSettingsBuilder);
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition, Supplier<SubSettingsBuilder> builderSupplier) {
		this(material, messageSuffix, condition, builderSupplier.get());
	}

	ChallengeCondition(Material material, String messageSuffix, IChallengeCondition condition) {
		this(material, messageSuffix, condition, SubSettingsBuilder.create());
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
	public IChallengeEnum[] getValues() {
		return values();
	}

	public IChallengeCondition getConditionInterface() {
		return condition;
	}

	public SubSettingsBuilder getSubSettingsBuilder() {
		return subSettingsBuilder;
	}

}
