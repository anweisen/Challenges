package net.codingarea.challenges.plugin.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see Deprecated
 */
@Retention(RetentionPolicy.CLASS)
public @interface ReplaceWith {

	String value();

}
