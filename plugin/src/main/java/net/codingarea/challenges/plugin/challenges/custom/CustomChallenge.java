package net.codingarea.challenges.plugin.challenges.custom;

import java.util.List;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomChallenge extends Setting {

	private final UUID uuid;
	private Material material;
	private String name;
	private ChallengeCondition condition;
	private String[] subConditions;
	private ChallengeAction action;
	private String[] subActions;

	public CustomChallenge(MenuType menuType, UUID uuid, Material displayItem, String name, ChallengeCondition condition, String[] subConditions, ChallengeAction action, String[] subActions) {
		super(menuType);
		this.uuid = uuid;
		this.material = displayItem;
		this.name = name;
		this.condition = condition;
		this.subConditions = subConditions;
		this.action = action;
		this.subActions = subActions;
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		String name = this.name;
		if (name == null) {
			name = "NULL";

		}
		Material material = this.material;
		if (material == null) {
			if (action != null) {
				material = action.getMaterial();
			} else {
				material = Material.BARRIER;
			}
		}

		return new ItemBuilder(material, "§7" + name);
	}

	@Override
	public void playStatusUpdateTitle() {
		Challenges.getInstance().getTitleManager().sendChallengeStatusTitle(enabled ? Message.forName("title-challenge-enabled") : Message.forName("title-challenge-disabled"), getDisplayName());
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		super.writeSettings(document);

		document.set("material", material == null ? null : material.name());
		document.set("name", name);
		document.set("condition", condition == null ? null : condition.name());
		document.set("subConditions", subConditions);
		document.set("action", action == null ? null : action.name());
		document.set("subActions", subActions);
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return super.getSettingsDescription();
	}

	public final void onConditionFulfilled(Entity entity, String... data) {
		if (isEnabled()) {

			if (subConditions.length == 0) {
				List<String> dataList = Arrays.asList(data);
				for (String subCondition : subConditions) {
					if (!dataList.contains(subCondition)) {
						return;
					}
				}
			}

			executeAction(entity);
		}
	}

	public void executeAction(Entity entity) {
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
				action.getAction().execute(entity, subActions);
			});
			return;
		}
		action.getAction().execute(entity, subActions);
	}

	public void applySettings(@Nonnull Material material, @Nonnull String name, @Nonnull ChallengeCondition condition, String[] subConditions, ChallengeAction action, String[] subActions) {
		this.material = material;
		this.name = name;
		this.condition = condition;
		this.subConditions = subConditions;
		this.action = action;
		this.subActions = subActions;
	}

	public ChallengeAction getAction() {
		return action;
	}

	public ChallengeCondition getCondition() {
		return condition;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public Material getMaterial() {
		return material;
	}

	@Nonnull
	public String getDisplayName() {
		return name;
	}

	@Nonnull
	@Override
	public String getUniqueName() {
		return "custom-" + uuid.toString();
	}

	public String[] getSubConditions() {
		return subConditions;
	}

	public String[] getSubActions() {
		return subActions;
	}

	@Override
	public String toString() {
		return "CustomChallenge{" +
				"uuid=" + uuid +
				", material=" + material +
				", name='" + name + '\'' +
				", condition=" + condition +
				", subConditions=" + Arrays.toString(subConditions) +
				", action=" + action +
				", subActions=" + Arrays.toString(subActions) +
				'}';
	}

}
