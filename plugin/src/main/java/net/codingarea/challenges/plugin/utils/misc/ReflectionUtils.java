package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	@Nonnull
	public static Collection<Method> getDeclaredMethodsAnnotatedWith(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation> annotation) {
		List<Method> methods = new LinkedList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(annotation)) continue;
			methods.add(method);
		}
		return methods;
	}

	@Nonnull
	public static <E extends Enum<E>> E getEnumByNames(@Nonnull Class<E> clazz, @Nonnull String... names) {
		for (String name : names) {
			try {
				return Enum.valueOf(clazz, name);
			} catch (IllegalArgumentException | NoSuchFieldError ex) { }
		}
		return null; // exit, should never happen with correct inputs
	}

}
