package net.codingarea.challenges.plugin.utils.config;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@FunctionalInterface
public interface Json {

	@Nonnull
	String toJson();

}
