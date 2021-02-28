package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.event.MenuClickEvent;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import org.bukkit.inventory.ItemStack;

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
		this.enabled = enabledByDefault;
	}

	@Override
	public final void handleClick(@Nonnull MenuClickEvent event) {
		setEnabled(!enabled);
		SoundSample.playEnablingSound(event.getPlayer(), enabled);
	}

	public final void setEnabled(boolean enabled) {
		if (this.enabled == enabled) return;

		this.enabled = enabled;

		if (enabled) onEnable();
		else onDisable();

		updateItems();
	}

	@Nonnull
	@Override
	public ItemStack getSettingsItem() {
		return DefaultItem.status(enabled).build();
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
	public void writeSettings(@Nonnull Document document) {
		document.set("enabled", enabled);
	}

	@Override
	public void loadSettings(@Nonnull Document document) {
		enabled = document.getBoolean("enabled");
	}

}
