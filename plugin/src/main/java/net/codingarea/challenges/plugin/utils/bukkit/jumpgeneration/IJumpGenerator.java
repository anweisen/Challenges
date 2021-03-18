package net.codingarea.challenges.plugin.utils.bukkit.jumpgeneration;

import org.bukkit.block.Block;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface IJumpGenerator {

	@Nonnull
	@CheckReturnValue
	Block next(@Nonnull Random random, @Nonnull Block startingPoint, boolean includeFourBlockJumps, boolean includeUpGoing);

}
