package net.codingarea.challenges.plugin.management.challenges.annotations;

import net.anweisen.utilities.bukkit.utils.misc.MinecraftVersion;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireVersion {

	@Nonnull
	MinecraftVersion value();

}
