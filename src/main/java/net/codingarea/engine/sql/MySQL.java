package net.codingarea.engine.sql;

import net.codingarea.engine.sql.source.LinkAttachment;
import net.codingarea.engine.sql.source.URLDataSource;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.SQLException;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MySQL extends SQL {

	@Nonnull
	@CheckReturnValue
	public static MySQL createDefault(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		return new MySQL(host, database, user, password, LinkAttachment.DEFAULT);
	}

	@Nonnull
	@CheckReturnValue
	public static MySQL createDefault(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		return new MySQL(host, port, database, user, password, LinkAttachment.DEFAULT);
	}

	public MySQL(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password));
	}

	public MySQL(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) throws SQLException {
		this(new URLDataSource(host, database, user, password, port));
	}

	public MySQL(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password, @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password).addAttachment(attachments));
	}

	public MySQL(@Nonnull String host, int port, @Nonnull String database, @Nonnull String user, @Nonnull String password, @Nonnull LinkAttachment... attachments) throws SQLException {
		this(new URLDataSource(host, database, user, password, port).addAttachment(attachments));
	}

	public MySQL(@Nonnull URLDataSource dataSource) throws SQLException {
		super(dataSource);
		connect();
	}

}
