package net.codingarea.challenges.plugin.language.loader;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class ContentLoader {

	@Nonnull
	protected final File getMessagesFolder() {
		return Challenges.getInstance().getDataFile("messages");
	}

	@Nonnull
	protected final File getMessageFile(@Nonnull String name, @Nonnull String extension) {
		return new File(getMessagesFolder(), name + "." + extension);
	}

	protected abstract void load();

}
