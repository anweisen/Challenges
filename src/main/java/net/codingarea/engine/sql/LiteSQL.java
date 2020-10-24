package net.codingarea.engine.sql;

import net.codingarea.engine.sql.source.FileDataSource;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LiteSQL extends SQL {

	@Nonnull
	@CheckReturnValue
	public static LiteSQL createDefault() throws SQLException, IOException {
		return new LiteSQL(new FileDataSource("data.db"));
	}

	public LiteSQL(@Nonnull String path) throws SQLException, IOException {
		this(new File(path));
	}

	public LiteSQL(@Nonnull File file, @Nonnull Logger logger) throws SQLException, IOException {
		this(new FileDataSource(file), logger);
	}

	public LiteSQL(@Nonnull File file) throws SQLException, IOException {
		this(new FileDataSource(file));
	}

	public LiteSQL(@Nonnull FileDataSource dataSource, @Nonnull Logger logger) throws SQLException, IOException {
		super(dataSource, logger);
		dataSource.checkFile();
		connect();
	}

	public LiteSQL(@Nonnull FileDataSource dataSource) throws SQLException, IOException {
		super(dataSource);
		dataSource.checkFile();
		connect();
	}

}
