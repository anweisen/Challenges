package net.codingarea.challenges.plugin.lang.loader;

import net.codingarea.challenges.plugin.Challenges;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class ContentLoader {

	@Nonnull
	protected final File getFolder() {
		File folder = new File(Challenges.getInstance().getDataFolder(), "messages");
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}

	@Nonnull
	protected final File getFile(@Nonnull String name, @Nonnull String extension) {
		return new File(getFolder(), name + "." + extension);
	}

	public abstract void load();

}
