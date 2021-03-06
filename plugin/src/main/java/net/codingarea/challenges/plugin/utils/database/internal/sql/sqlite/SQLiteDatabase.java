package net.codingarea.challenges.plugin.utils.database.internal.sql.sqlite;

import net.codingarea.challenges.plugin.utils.database.DatabaseConfig;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;
import net.codingarea.challenges.plugin.utils.misc.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class SQLiteDatabase extends AbstractSQLDatabase {

	protected final File file;

	public SQLiteDatabase(@Nonnull DatabaseConfig config) {
		super(config);
		file = new File(config.getFile());
	}

	@Override
	public void connect() throws DatabaseException {
		try {
			FileUtils.createFilesIfNecessary(file);
		} catch (IOException ex) {
			throw new DatabaseException(ex);
		}

		super.connect();
	}

	@Override
	protected String createURL() {
		return "jdbc:sqlite:" + file;
	}

}
