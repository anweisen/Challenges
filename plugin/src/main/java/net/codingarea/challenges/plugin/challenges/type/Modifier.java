package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.event.MenuClickEvent;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import org.bukkit.inventory.ItemStack;

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
	public ItemStack getSettingsItem() {
		return DefaultItem.value(value).build();
	}

	@Nonnull
	public final Modifier setValue(int value) {
		if (value > max) throw new IllegalArgumentException("value > max");
		if (value < min) throw new IllegalArgumentException("value < min");
		this.value = value;
		updateItems();
		onValueChange();
		return this;
	}

	@Nonnegative
	public final int getValue() {
		return value;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override


	protected void onValueChange() {
	}

	}

	}

}
