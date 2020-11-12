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
import net.codingarea.challengesplugin.utils.Utils;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Dominik https://github.com/kxmischesdomi
 * @author anweisen https://github.com/anweisen
 * @since 1.4
 */
public abstract class RandomTimeAdvancedChallenge extends AdvancedChallenge {

	public RandomTimeAdvancedChallenge(final @Nonnull MenuType menu) {
		super(menu);
		setNextSeconds();
	}

	public RandomTimeAdvancedChallenge(final @Nonnull MenuType menu, final int maxValue) {
		super(menu, maxValue);
		setNextSeconds();
	}

	public RandomTimeAdvancedChallenge(final @Nonnull MenuType menu, final int maxValue, final int minValue) {
		super(menu, maxValue, minValue);
		setNextSeconds();
	}

	public RandomTimeAdvancedChallenge(final @Nonnull MenuType menu, final int maxValue, final int minValue, final int countUp) {
		super(menu, maxValue, minValue, countUp);
		setNextSeconds();
	}

	

	@Override
	public void onValueChange(final @Nonnull ChallengeEditEvent event) {
		if (nextActionInSeconds > getSeconds(maxValue) || nextActionInSeconds < getSeconds(minValue)) {
			setNextSeconds();
		}
	}

	protected abstract int getSeconds(final int value);

	protected void setNextSeconds() {
		nextActionInSeconds = getRandomSeconds();
	}

	protected int getRandomSeconds() {
		int max = Utils.getRandomSecondsUp(getSeconds(value));
		int min = Utils.getRandomSecondsDown(getSeconds(value));
		return ThreadLocalRandom.current().nextInt(max - min) + min;
	}

}