package net.codingarea.challenges.plugin.lang.loader;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.PropertiesDocument;
import net.anweisen.utilities.commons.misc.FileUtils;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import java.io.File;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PrefixLoader extends ContentLoader {

	@Override
	protected void load() {
		try {

			File file = getFile("prefix", "properties");
			FileUtils.createFilesIfNecessary(file);

			Document document = new PropertiesDocument(file);

			for (Prefix prefix : Prefix.values()) {
				if (!document.contains(prefix.name())) {
					document.set(prefix.name(), prefix.toString());
					continue;
				}

				prefix.setValue(document.getString(prefix.name()));
			}

			document.save(file);
			Logger.info("Successfully loaded prefixes from config file");

		} catch (Exception ex) {
			Logger.severe("Could not load prefix", ex);
		}
	}

}
