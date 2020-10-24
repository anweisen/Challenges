package net.codingarea.challengesplugin.utils.commons;

import java.io.PrintWriter;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.3
 */
public class StringBuilderPrintWriter extends PrintWriter {

	protected final StringBuilderWriter writer;

	public StringBuilderPrintWriter() {
		super(new StringBuilderWriter());
		writer = (StringBuilderWriter) out;
	}

	public StringBuilder getBuilder() {
		return writer.builder;
	}

	@Override
	public String toString() {
		return getBuilder().toString();
	}

}
