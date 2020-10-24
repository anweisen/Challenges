package net.codingarea.engine.sql.source;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class FileDataSource implements DataSource {

	private final File file;

	public FileDataSource(@Nonnull String path) {
		this(new File(path));
	}

	public FileDataSource(@Nonnull File file) {
		if (!file.getName().endsWith(".db")) throw new IllegalArgumentException("File is not a .db file");
		this.file = file;
	}

	@Nonnull
	@Override
	public String getURL() {
		return DataSource.LITESQL_URL.replace("%file", file.getPath());
	}

	@Nonnull
	public File getFile() {
		return file;
	}

	public void checkFile() throws IOException {
		if (!file.exists()) file.createNewFile();
	}

	@Override
	public String toString() {
		return "FileDataSource{" +
				"file=" + file +
				'}';
	}
}
