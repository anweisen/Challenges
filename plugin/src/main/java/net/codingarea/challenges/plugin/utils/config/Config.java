package net.codingarea.challenges.plugin.utils.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Config extends Propertyable {

	@Nonnull
	Config set(@Nonnull String path, @Nullable Object value);

	@Nonnull
	Config clear();

	@Nonnull
	Config remove(@Nonnull String path);

	boolean isReadonly();

}
