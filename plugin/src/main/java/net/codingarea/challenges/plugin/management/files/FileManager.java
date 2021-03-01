package net.codingarea.challenges.plugin.management.files;

import net.codingarea.challenges.plugin.Challenges;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class FileManager {

	@Nonnull
	public static File getFile(@Nonnull String name) {
		return new File(Challenges.getInstance().getDataFolder(), name);
	}

}
