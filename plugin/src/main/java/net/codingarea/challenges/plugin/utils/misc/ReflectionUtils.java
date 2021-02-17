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

	private ReflectionUtils() { }

	@Nonnull
	public static Collection<Method> getDeclaredMethodsAnnotatedWith(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation> annotation) {
		List<Method> methods = new LinkedList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (!method.isAnnotationPresent(annotation)) continue;
			methods.add(method);
		}
		return methods;
	}

}
