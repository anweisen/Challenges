package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PropertiesUtils {

	private PropertiesUtils() {
	}

	public static void setProperties(@Nonnull Properties properties, @Nonnull Map<String, Object> map) {
		for (Entry<Object, Object> entry : properties.entrySet()) {
			map.put((String) entry.getKey(), entry.getValue());
		}
	}

}
