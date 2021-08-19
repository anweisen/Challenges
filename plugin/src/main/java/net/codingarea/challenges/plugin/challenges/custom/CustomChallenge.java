package net.codingarea.challenges.plugin.challenges.custom;

import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.api.ChallengeCondition;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class CustomChallenge extends Setting {

	private final Material material;
	private final ChallengeCondition condition;
	private final String[] subConditions;
	private final ChallengeAction action;
	private final String[] subActions;

	public CustomChallenge(MenuType menuType, Material displayItem, ChallengeCondition condition, String[] subConditions, ChallengeAction action, String[] subActions) {
		super(menuType);
		this.material = displayItem;
		this.condition = condition;
		this.subConditions = subConditions;
		this.action = action;
		this.subActions = subActions;
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(material);
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		super.loadSettings(document);
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		super.writeSettings(document);
	}

	@Nullable
	@Override
	protected String[] getSettingsDescription() {
		return super.getSettingsDescription();
	}

	public final void onConditionFulfilled(@Nonnull Entity entity, String... data) {
		if (isEnabled()) {
			if (Arrays.equals(this.subConditions, data)) {
				action.getAction().execute(entity, subActions);
			}
		}
	}

	public ChallengeAction getAction() {
		return action;
	}

	public ChallengeCondition getCondition() {
		return condition;
	}

}
