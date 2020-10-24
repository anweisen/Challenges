package net.codingarea.engine.sql.source;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class URLDataSource implements DataSource {

	private final List<LinkAttachment> linkAttachments = new ArrayList<>();
	private final String host, database, user, password;
	private final Integer port;

	public URLDataSource(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = null;
	}

	public URLDataSource(@Nonnull String host, @Nonnull String database, @Nonnull String user, @Nonnull String password, int port) {
		this.host = host;
		this.database = database;
		this.user = user;
		this.password = password;
		this.port = port;
	}

	@Nonnull
	@Override
	public String getURL() {
		return MYSQL_URL.replace("%host", host + (port != null ? ":" + port : "")).replace("%database", database) + LinkAttachment.list(linkAttachments);
	}

	@Nonnull
	@Override
	public Connection createConnection() throws SQLException {
		return DriverManager.getConnection(getURL(), user, password);
	}

	@Nonnull
	public String getUser() {
		return user;
	}

	@Nonnull
	public String getPassword() {
		return password;
	}

	public Integer getPort() {
		return port;
	}

	@Nonnull
	public String getHost() {
		return host;
	}

	@Nonnull
	public String getDatabase() {
		return database;
	}

	@Nonnull
	public List<LinkAttachment> getLinkAttachments() {
		return linkAttachments;
	}

	@Nonnull
	public URLDataSource addAttachment(@Nonnull LinkAttachment... attachment) {
		linkAttachments.addAll(Arrays.asList(attachment));
		return this;
	}

	@Override
	public String toString() {
		return "URLDataSource{" +
				"linkAttachments=" + linkAttachments +
				", host='" + host + '\'' +
				", database='" + database + '\'' +
				", user='" + user + '\'' +
				", password='" + password + '\'' +
				", port=" + port +
				'}';
	}
}
