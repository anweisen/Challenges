package net.codingarea.engine.sql.source;

import net.codingarea.engine.utils.NamedValue;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class LinkAttachment extends NamedValue {

	public static final LinkAttachment[] DEFAULT = {
		new LinkAttachment("autoReconnect", true),
		new LinkAttachment("serverTimezone", "UTC")	,
		new LinkAttachment("useJDBCCompliantTimezoneShift", true),
		new LinkAttachment("useLegacyDatetimeCode", true)
	};

	public LinkAttachment(@Nonnull String label, boolean value) {
		this(label, String.valueOf(value));
	}

	public LinkAttachment(@Nonnull String label, @Nonnull String value) {
		super(label, value);
	}

	@Nonnull
	@CheckReturnValue
	public static String list(@Nonnull Iterable<LinkAttachment> attachments) {
		StringBuilder string = new StringBuilder();
		for (LinkAttachment attachment : attachments) {
			if (string.length() == 0) {
				string.append("?");
			} else {
				string.append("&");
			}
			string.append(attachment.asAttachment());
		}
		return string.toString();
	}

	@Nonnull
	public String asAttachment() {
		return key + '=' + value;
	}

	@Nonnull
	@Override
	public String toString() {
		return "LinkAttachment{" +
				"key='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
