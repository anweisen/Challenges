package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class RandomizeUtils {

	private RandomizeUtils() {}

	public static int randomAround(@Nonnull Random random, @Nonnegative int value, @Nonnegative int range) {
		return randomInRange(random, value - range, value + range);
	}

	public static int randomInRange(@Nonnull Random random, int min, int max) {
		return random.nextInt(max - min) + min;
	}

	@Nonnull
	public static <T> T choose(@Nonnull Random random, @Nonnull T... array) {
		return array[random.nextInt(array.length)];
	}

	@Nonnull
	public static <T> T choose(@Nonnull Random random, @Nonnull List<? extends T> list) {
		return list.get(random.nextInt(list.size()));
	}

}
