package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class RandomizeUtils {

	public static int getAround(@Nonnull Random random, @Nonnegative int value, @Nonnegative int range) {
		int min = value - range;
		int max = value + range;
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
