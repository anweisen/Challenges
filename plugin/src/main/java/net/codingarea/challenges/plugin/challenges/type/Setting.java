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
 * @since 1.0
 */
public abstract class Setting extends AbstractChallenge {

	private boolean enabled;

	public Setting(@Nonnull MenuType menu) {
		super(menu);
	}

	public Setting(@Nonnull MenuType menu, boolean enabledByDefault) {
		super(menu);
		setEnabled(enabledByDefault);
	}

	@Override
	public void handleClick(@Nonnull ChallengeMenuClickInfo event) {
		setEnabled(!enabled);
		SoundSample.playEnablingSound(event.getPlayer(), enabled);
		playStatusUpdateTitle();
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;
		this.enabled = enabled;

		if (enabled) onEnable();
		else onDisable();

		updateItems();
	}

	public void playStatusUpdateTitle() {
		ChallengeHelper.playToggleChallengeTitle(this);
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		return DefaultItem.status(enabled);
	}

	protected void onEnable() {
	}

	protected void onDisable() {
	}

	@Override
	public final boolean isEnabled() {
		return enabled;
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		setEnabled(document.getBoolean("enabled", enabled));
	}

	@Override
	public void writeSettings(@Nonnull Document document) {
		document.set("enabled", enabled);
	}

}
