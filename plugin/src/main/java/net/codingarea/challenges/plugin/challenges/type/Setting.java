package net.codingarea.challenges.plugin.challenges.type;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public abstract class Setting extends AbstractChallenge {

	private final boolean enabledByDefault;
	private boolean enabled;

	public Setting(@Nonnull MenuType menu) {
		this(menu, false);
	}

	public Setting(@Nonnull MenuType menu, boolean enabledByDefault) {
		super(menu);
		this.enabledByDefault = enabledByDefault;
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

	@Override
	public void restoreDefaults() {
		setEnabled(enabledByDefault);
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
	public void handleShutdown() {
		super.handleShutdown();

		if (isEnabled())
			onDisable();
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
