package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class FileUtils {

	private FileUtils() {
	}

	@Nonnull
	public static Writer createWriter(@Nonnull File file) throws IOException {
		return Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
	}

	@Nonnull
	public static Reader createReader(@Nonnull File file) throws IOException {
		return Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
	}

}
