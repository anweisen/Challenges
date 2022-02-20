package net.codingarea.challenges.plugin.challenges.custom;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomChallenge extends Setting {

	private final UUID uuid;
	private Material material;
	private String name;
	private AbstractChallengeCondition condition;
	private Map<String, String[]> subConditions;
	private AbstractChallengeAction action;
	private Map<String, String[]> subActions;

	public CustomChallenge(MenuType menuType, UUID uuid, Material displayItem, String name, AbstractChallengeCondition condition,
			Map<String, String[]> subConditions, AbstractChallengeAction action, Map<String, String[]> subActions) {
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
			material = Material.BARRIER;
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
		document.set("condition", condition == null ? null : condition.getName());
		document.set("subConditions", subConditions);
		document.set("action", action == null ? null : action.getName());
		document.set("subActions", subActions);
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return super.getSettingsDescription();
	}

	public final void onConditionFulfilled(Entity entity, Map<String, List<String>> data) {
		if (isEnabled()) {

			boolean conditionMet = isConditionMet(data);
			if (conditionMet) {
				executeAction(entity);
			}

		}
	}

	/**
	 * @return if the condition is met.
	 * That is when every key in the subConditions is contained by the data map and one or more value
	 * of the lists are equal to one another.
	 */
	public boolean isConditionMet(Map<String, List<String>> data) {
		if (!subConditions.isEmpty()) {
			for (Entry<String, String[]> entry : subConditions.entrySet()) {
				if (!data.containsKey(entry.getKey())) {
					return false;
				}
				List<String> list = data.get(entry.getKey());

				boolean match = false;
				for (String value : entry.getValue()) {
					if (list.contains(value)) {
						match = true;
						break;
					}
				}

				if (!match) {
					return false;
				}
			}
		}
		return true;
	}

	public void executeAction(Entity entity) {
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
				action.execute(entity, subActions);
			});
			return;
		}
		action.execute(entity, subActions);
	}

	public void applySettings(@Nonnull Material material, @Nonnull String name, @Nonnull AbstractChallengeCondition condition,
			Map<String, String[]> subConditions, AbstractChallengeAction action, Map<String, String[]> subActions) {
		this.material = material;
		this.name = name;
		this.condition = condition;
		this.subConditions = subConditions;
		this.action = action;
		this.subActions = subActions;
	}

	public AbstractChallengeAction getAction() {
		return action;
	}

	public AbstractChallengeCondition getCondition() {
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
		return uuid.toString();
	}

	public Map<String, String[]> getSubConditions() {
		return subConditions;
	}

	public Map<String, String[]> getSubActions() {
		return subActions;
	}

	@Override
	public String toString() {
		return "CustomChallenge{" +
				"uuid=" + uuid +
				", material=" + material +
				", name='" + name + '\'' +
				", condition=" + condition +
				", subConditions=" + subConditions +
				", action=" + action +
				", subActions=" + subActions +
				'}';
	}

}
