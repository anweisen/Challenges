package net.codingarea.challenges.plugin.utils.common;

import javax.annotation.Nonnull;
import java.io.PrintWriter;

/**
 * Because this class is not implemented in versions of bukkit before 1.14, we just make our own.
 *
 * @author org.apache.commons.io
 * @since 1.3
 */
public class StringBuilderPrintWriter extends PrintWriter {

	protected final StringBuilderWriter writer;

	public StringBuilderPrintWriter() {
		super(new StringBuilderWriter());
		writer = (StringBuilderWriter) out;
	}

	@Nonnull
	public StringBuilder getBuilder() {
		return writer.getBuilder();
	}

	@Override
	public String toString() {
		return getBuilder().toString();
	}

}
