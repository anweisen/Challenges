package net.codingarea.challenges.plugin.challenges.type;

import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractChallenge implements IChallenge, Listener {

	protected final MenuType menu;

	public AbstractChallenge(@Nonnull MenuType menu) {
		this.menu = menu;
	}

	@Nonnull
	@Override
	public final MenuType getType() {
		return menu;
	}

	protected final void updateItems() {
		ChallengeHelper.updateItems(this);
	}

}