package net.codingarea.challenges.plugin.utils.config;

import net.codingarea.challenges.plugin.utils.misc.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Document extends Config, Json {

	@Nonnull
	Document getDocument(@Nonnull String path);

	@Nonnull
	@Override
	Document set(@Nonnull String path, @Nullable Object value);

	@Nonnull
	@Override
	Document clear();

	@Nonnull
	@Override
	Document remove(@Nonnull String path);

	void write(@Nonnull Writer writer) throws IOException;

	default void save(@Nonnull File file) throws IOException {
		Writer writer = FileUtils.newBufferedWriter(file);
		write(writer);
		writer.flush();
		writer.close();
	}

}
