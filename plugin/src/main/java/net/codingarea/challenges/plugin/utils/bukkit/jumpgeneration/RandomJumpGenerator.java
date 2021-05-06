package net.codingarea.challenges.plugin.utils.bukkit.jumpgeneration;

import org.bukkit.block.Block;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Random;

import static net.codingarea.challenges.plugin.utils.misc.RandomizeUtils.choose;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class RandomJumpGenerator implements IJumpGenerator {

	@Nonnull
	@Override
	@CheckReturnValue
	public Block next(@Nonnull Random random, @Nonnull Block startingPoint, boolean includeFourBlockJumps, boolean includeUpGoing) {

		int layer = random.nextInt(includeUpGoing ? 3 : 2) - 1;
		int range = layer == 0 ? 4 : 3;

		int mainDirection = random.nextInt(range * 2) - range;
		int mainDirectionValue = Math.abs(mainDirection);
		if (mainDirectionValue < 2) {
			mainDirection = choose(random, -range, range);
			mainDirectionValue = Math.abs(mainDirection);
		}

		int secondDirection = determineSecondDirection(random, mainDirectionValue, range);
		return translate(startingPoint, random, layer, mainDirection, secondDirection);

	}

	protected int determineSecondDirection(@Nonnull Random random, int mainDirection, int range) {
		if (mainDirection == range || mainDirection == range - 1) {
			return choose(random, -1, 0, 1);
		} else if (mainDirection == range - 2) {
			return choose(random, -2, -1, 0, 1, 2);
		} else if (mainDirection == range - 3) {
			return choose(random, -3, -2, -1, 0, 1, 2, 3);
		} else if (mainDirection == range - 4 || mainDirection == range - 5) {
			return choose(random, -4, -3, -2, -1, 0, 1, 2, 3, 4);
		}
		throw new IllegalArgumentException("Could not determine second direction for main direction " + mainDirection + ", range " + range);
	}

	@Nonnull
	@CheckReturnValue
	protected Block translate(@Nonnull Block startingPoint, @Nonnull Random random, int layer, int mainDirection, int secondDirection) {

		boolean intoX = random.nextBoolean();

		int x = intoX ? mainDirection : secondDirection;
		int z = intoX ? secondDirection : mainDirection;

		return startingPoint.getRelative(x, layer, z);

	}

}
