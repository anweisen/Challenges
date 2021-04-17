package net.codingarea.challenges.plugin.language.loader;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.PropertiesDocument;
import net.anweisen.utilities.commons.misc.FileUtils;
import net.codingarea.challenges.plugin.language.Prefix;
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

			File file = getMessageFile("prefix", "properties");
			FileUtils.createFilesIfNecessary(file);

			Document document = new PropertiesDocument(file);

			for (Prefix prefix : Prefix.values()) {
				if (!document.contains(prefix.getName())) {
					document.set(prefix.getName(), prefix.toString());
					continue;
				}

				prefix.setValue(document.getString(prefix.getName()));
			}

			document.save(file);
			Logger.info("Successfully loaded {} prefixes from config file", Prefix.values().size());

		} catch (Exception ex) {
			Logger.error("Could not load prefixes", ex);
		}
	}

}
