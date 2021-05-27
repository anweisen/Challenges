package net.codingarea.challenges.plugin.utils.misc;

import net.anweisen.utilities.commons.annotations.ReplaceWith;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * @deprecated Simply use an {@link net.anweisen.utilities.commons.common.IRandom} directly
 *
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Deprecated
@ReplaceWith("IRandom")
public final class RandomizeUtils {

	private RandomizeUtils() {}

	@Deprecated
	@ReplaceWith("IRandom#around(int, int)")
	public static int randomAround(@Nonnull Random random, @Nonnegative int value, @Nonnegative int range) {
		return randomInRange(random, value - range, value + range);
	}

	@Deprecated
	@ReplaceWith("IRandom#range(int, int)")
	public static int randomInRange(@Nonnull Random random, int min, int max) {
		return random.nextInt(max - min) + min;
	}

	@Nonnull
	@Deprecated
	@ReplaceWith("IRandom#choose(T...)")
	public static <T> T choose(@Nonnull Random random, @Nonnull T... array) {
		return array[random.nextInt(array.length)];
	}

	@Nonnull
	@Deprecated
	@ReplaceWith("IRandom#choose(List<T>)")
	public static <T> T choose(@Nonnull Random random, @Nonnull List<? extends T> list) {
		return list.get(random.nextInt(list.size()));
	}

}
