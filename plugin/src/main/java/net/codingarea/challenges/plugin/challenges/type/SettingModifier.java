package net.codingarea.challenges.plugin.challenges.type;

import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class SettingModifier extends Modifier {

	private boolean enabled;

	public SettingModifier(@Nonnull MenuType menu) {
		super(menu);
	}

	public SettingModifier(@Nonnull MenuType menu, int max) {
		super(menu, max);
	}

	public SettingModifier(@Nonnull MenuType menu, int min, int max) {
		super(menu, min, max);
	}

	public SettingModifier(@Nonnull MenuType menu, int min, int max, int defaultValue) {
		super(menu, min, max, defaultValue);
	}

	@Override
	public void handleClick(@Nonnull ChallengeMenuClickInfo event) {
		if (event.isUpperItemClick() || !enabled) {
			setEnabled(!enabled);
			SoundSample.playEnablingSound(event.getPlayer(), enabled);
			playStatusUpdateTitle();
		} else {
			super.handleClick(event);
		}
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		this.enabled = enabled;

		if (enabled) onEnable();
		else onDisable();

		updateItems();
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		return enabled ? DefaultItem.enabled().amount(getValue()) : DefaultItem.disabled();
	}

	@Override
	public final boolean isEnabled() {
		return enabled;
	}

	public void playStatusUpdateTitle() {
		ChallengeHelper.playToggleChallengeTitle(this);
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		super.writeSettings(document);
		document.set("enabled", enabled);
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		super.loadSettings(document);
		setEnabled(document.getBoolean("enabled"));
	}

}
