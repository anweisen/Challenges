package net.codingarea.challenges.plugin.lang.loader;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.lang.Prefix;
import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.PropertiesDocument;

import java.io.File;
import java.util.logging.Level;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PrefixLoader extends ContentLoader {

	@Override
	protected void load() {
		try {

			File file = getFile("prefix", "properties");
			if (!file.exists()) file.createNewFile();

			Document document = new PropertiesDocument(file);

			for (Prefix prefix : Prefix.values()) {
				if (!document.contains(prefix.name())) {
					document.set(prefix.name(), prefix.toString());
					continue;
				}

				prefix.setValue(document.getString(prefix.name()));
			}

			document.save(file);
		} catch (Exception ex) {
			Challenges.getInstance().getLogger().log(Level.SEVERE, "Could not load prefix", ex);
		}
	}

}
