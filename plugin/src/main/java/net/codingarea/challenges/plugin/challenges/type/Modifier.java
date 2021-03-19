package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public abstract class Modifier extends AbstractChallenge {

	private final int max, min;
	private int value;

	public Modifier(@Nonnull MenuType menu) {
		this(menu, 1, 64);
	}

	public Modifier(@Nonnull MenuType menu, int max) {
		this(menu, 1, max);
	}

	public Modifier(@Nonnull MenuType menu, int min, int max) {
		this(menu, min, max, min);
	}

	public Modifier(@Nonnull MenuType menu, int min, int max, int defaultValue) {
		super(menu);
		if (max <= min) throw new IllegalArgumentException("max <= min");
		if (min < 0) throw new IllegalArgumentException("min < 0");
		if (defaultValue > max) throw new IllegalArgumentException("defaultValue > max");
		if (defaultValue < min) throw new IllegalArgumentException("defaultValue < min");
		this.max = max;
		this.min = min;
		this.value = defaultValue;
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		return DefaultItem.value(value);
	}

	public void setValue(int value) {
		if (value > max) throw new IllegalArgumentException("value > max");
		if (value < min) throw new IllegalArgumentException("value < min");
		this.value = value;
		onValueChange();
		updateItems();
	}

	@Nonnegative
	public final int getValue() {
		return value;
	}

	@Nonnegative
	public final int getMaxValue() {
		return max;
	}

	@Nonnegative
	public final int getMinValue() {
		return min;
	}

	@Override
	public boolean isEnabled() {
		return getValue() > 0;
	}

	@Override
	public void handleClick(@Nonnull ChallengeMenuClickInfo event) {
		ChallengeHelper.handleModifierClick(event, this);
	}

	public void playValueChangeTitle() {
		ChallengeHelper.playChangeChallengeValueTitle(this);
	}

	protected void onValueChange() {
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		setValue(document.getInt("value", value));
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		document.set("value", value);
	}

}
