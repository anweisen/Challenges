package net.codingarea.challenges.plugin.utils.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Document extends Propertyable {

	@Nonnull
	Document getDocument(@Nonnull String path);

	@Nonnull
	@Override
	Document set(@Nonnull String path, @Nullable Object value);

	void write(@Nonnull Writer writer) throws IOException;

	default void save(@Nonnull File file) throws IOException {
		Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
		write(writer);
		writer.flush();
		writer.close();
	}

}
