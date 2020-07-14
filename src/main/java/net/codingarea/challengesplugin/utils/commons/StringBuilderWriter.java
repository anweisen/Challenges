package net.codingarea.challengesplugin.utils.commons;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.io.Writer;

/**
 * @author anweisen
 * Challenges developed on 07-13-2020
 * https://github.com/anweisen
 */

public class StringBuilderWriter extends Writer implements Serializable {

	private static final long serialVersionUID = -146927496096066153L;
	private final StringBuilder builder;

	public StringBuilderWriter() {
		this.builder = new StringBuilder();
	}

	public StringBuilderWriter(int capacity) {
		this.builder = new StringBuilder(capacity);
	}

	public StringBuilderWriter(StringBuilder builder) {
		this.builder = builder != null ? builder : new StringBuilder();
	}

	public Writer append(char value) {
		this.builder.append(value);
		return this;
	}

	public Writer append(CharSequence value) {
		this.builder.append(value);
		return this;
	}

	public Writer append(CharSequence value, int start, int end) {
		this.builder.append(value, start, end);
		return this;
	}

	public void close() {
	}

	public void flush() {
	}

	public void write(@NotNull String value) {
		builder.append(value);
	}

	public void write(char[] value, int offset, int length) {
		if (value != null) {
			this.builder.append(value, offset, length);
		}

	}

	public StringBuilder getBuilder() {
		return this.builder;
	}

	public String toString() {
		return this.builder.toString();
	}

}

