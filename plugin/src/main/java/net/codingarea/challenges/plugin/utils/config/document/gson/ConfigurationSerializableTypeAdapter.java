package net.codingarea.challenges.plugin.utils.config.document.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.GsonUtils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 *
 * @see ConfigurationSerializable
 */
public final class ConfigurationSerializableTypeAdapter implements GsonTypeAdapter<ConfigurationSerializable> {

	public static final String KEY = "classOfType";

	@Override
	public void write(@Nonnull Gson gson, @Nonnull JsonWriter writer, @Nonnull ConfigurationSerializable object) throws IOException {

		Class<? extends ConfigurationSerializable> clazz = object.getClass();

		JsonObject json = new JsonObject();
		GsonUtils.setValues(gson, json, object.serialize());
		json.addProperty(KEY, clazz.getName());
		TypeAdapters.JSON_ELEMENT.write(writer, json);

	}

	@Override
	public ConfigurationSerializable read(@Nonnull Gson gson, @Nonnull JsonReader reader) throws IOException {

		JsonElement element = TypeAdapters.JSON_ELEMENT.read(reader);
		if (element == null || !element.isJsonObject()) return null;

		JsonObject json = element.getAsJsonObject();
		String classOfType = json.get(KEY).getAsString();

		Map<String, Object> map = GsonUtils.convertToMap(json);

		try {
			Class<?> clazz = Class.forName(classOfType);

			Method method = clazz.getMethod("deserialize", Map.class);
			method.setAccessible(true);
			Object deserialization = method.invoke(null, map);

			return (ConfigurationSerializable) deserialization;

		} catch (Throwable ex) {
			Logger.severe("Could not deserialize '" + classOfType + "': ");
			return null;
		}

	}

}
