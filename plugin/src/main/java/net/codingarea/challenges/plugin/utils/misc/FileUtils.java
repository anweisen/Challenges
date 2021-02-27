package net.codingarea.challenges.plugin.utils.misc;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class FileUtils {

	private FileUtils() {
	}

	@Nonnull
	public static BufferedWriter newBufferedWriter(@Nonnull File file) throws IOException {
		return Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);
	}

	@Nonnull
	public static BufferedReader newBufferedReader(@Nonnull File file) throws IOException {
		return Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
	}

	@Nonnull
	public static String getFileExtension(@Nonnull File file) {
		return getFileExtension(file.getName());
	}

	@Nonnull
	public static String getFileExtension(@Nonnull String filename) {
		return Optional.of(filename)
				.filter(name -> name.contains("."))
				.map(name -> name.substring(filename.lastIndexOf(".") + 1).toLowerCase())
				.orElse("");
	}

}
