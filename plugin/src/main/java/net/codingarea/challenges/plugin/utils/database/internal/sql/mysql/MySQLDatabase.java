package net.codingarea.challenges.plugin.utils.database.internal.sql.mysql;

import net.codingarea.challenges.plugin.utils.database.DatabaseConfig;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class MySQLDatabase extends AbstractSQLDatabase {

	public MySQLDatabase(@Nonnull DatabaseConfig config) {
		super(config);
	}

	@Nonnull
	@Override
	protected String createURL() {
		return "jdbc:mysql://" + config.getHost() + (config.isPortSet() ? ":" + config.getPort() : "") + "/" + config.getDatabase();
	}

}
