/**
 * .d88b          8 w                  db
 * 8P    .d8b. .d88 w 8d8b. .d88      dPYb   8d8b .d88b .d88
 * 8b    8' .8 8  8 8 8P Y8 8  8     dPwwYb  8P   8.dP' 8  8
 * `Y88P `Y8P' `Y88 8 8   8 `Y88    dP    Yb 8    `Y88P `Y88
 * ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀wwdP
 */
package net.codingarea.challengesplugin.challengetypes;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;

import javax.annotation.Nonnull;

/**
 * @author Dominik https://github.com/kxmischesdomi
 * @author anweisen https://github.com/anweisen
 * @since 1.4
 */
public abstract class AutoTimeAdvancedChallenge extends AdvancedChallenge {

	public AutoTimeAdvancedChallenge(MenuType menu) {
		super(menu);
	}

	public AutoTimeAdvancedChallenge(MenuType menu, int maxValue) {
		super(menu, maxValue);
	}

	public AutoTimeAdvancedChallenge(MenuType menu, int maxValue, int minValue) {
		super(menu, maxValue, minValue);
	}

	public AutoTimeAdvancedChallenge(MenuType menu, int maxValue, int minValue, int countUp) {
		super(menu, maxValue, minValue, countUp);
	}

	protected abstract int getSeconds(final int value);

	@Override
	public void onValueChange(final @Nonnull ChallengeEditEvent event) {
		if (nextActionInSeconds > getSeconds(value)) {
			setNextSeconds();
		}
	}

	protected void setNextSeconds() {
		nextActionInSeconds = getSeconds(value);
	}


}