package net.codingarea.challenges.plugin.utils.config.document.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface GsonTypeAdapter<T> {

	void write(@Nonnull Gson gson, @Nonnull JsonWriter writer, @Nonnull T object) throws IOException;

	T read(@Nonnull Gson gson, @Nonnull JsonReader reader) throws IOException;

	static <T> TypeAdapterFactory newTypeHierarchyFactory(@Nonnull Class<T> clazz, @Nonnull GsonTypeAdapter<T> adapter) {
		return new TypeAdapterFactory() {
			@SuppressWarnings("unchecked")
			@Override
			public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> token) {
				Class<? super R> requestedType = token.getRawType();
				if (!clazz.isAssignableFrom(requestedType)) return null;

				return (TypeAdapter<R>) new TypeAdapter<T>() {
					@Override
					public void write(JsonWriter writer, T object) throws IOException {
						adapter.write(gson, writer, object);
					}

					@Override
					public T read(JsonReader reader) throws IOException {
						return adapter.read(gson, reader);
					}
				};
			}
		};
	}

}
