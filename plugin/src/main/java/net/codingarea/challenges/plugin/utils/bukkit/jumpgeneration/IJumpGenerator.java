package net.codingarea.challenges.plugin.utils.bukkit.jumpgeneration;

import net.anweisen.utilities.commons.common.IRandom;
import org.bukkit.block.Block;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface IJumpGenerator {

	@Nonnull
	@CheckReturnValue
	Block next(@Nonnull IRandom random, @Nonnull Block startingPoint, boolean includeFourBlockJumps, boolean includeUpGoing);

}
