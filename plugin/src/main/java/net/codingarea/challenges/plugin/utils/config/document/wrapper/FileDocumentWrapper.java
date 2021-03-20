package net.codingarea.challenges.plugin.utils.config.document.wrapper;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.config.document.PropertiesDocument;
import net.codingarea.challenges.plugin.utils.config.document.YamlDocument;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class FileDocumentWrapper extends DocumentWrapper {

	@Nonnull
	@CheckReturnValue
	public static FileDocumentWrapper createGson(@Nonnull File file) throws IOException {
		return new FileDocumentWrapper(file, new GsonDocument(file));
	}

	@Nonnull
	@CheckReturnValue
	public static FileDocumentWrapper createYaml(@Nonnull File file) {
		return new FileDocumentWrapper(file, new YamlDocument(file));
	}

	@Nonnull
	@CheckReturnValue
	public static FileDocumentWrapper createProperties(@Nonnull File file) throws IOException {
		return new FileDocumentWrapper(file, new PropertiesDocument(file));
	}

	@Nonnull
	@CheckReturnValue
	public static FileDocumentWrapper create(@Nonnull File file) throws IOException {
		String extension = FileUtils.getFileExtension(file);
		switch (extension) {
			case "yml":
			case "yaml":
				return createYaml(file);
			case "json":
				return createGson(file);
			case "properties":
				return createProperties(file);
			default:
				throw new IllegalArgumentException("Cannot create config for file type '" + extension + "'");
		}
	}

	protected final File file;

	public FileDocumentWrapper(@Nonnull File file, @Nonnull Document document) {
		super(document);
		this.file = file;
	}

	public void save(boolean async) {
		if (async)  saveAsync();
		else        save();
	}

	public void save() {
		try {
			document.save(file);
		} catch (Exception ex) {
			Logger.severe("Could not save config " + file.getName() + ": " + ex.getMessage());
		}
	}

	public void saveAsync() {
		Thread thread = new Thread(this::save);
		thread.setName("DocumentWriter");
		thread.start();
	}

}
