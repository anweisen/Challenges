package net.codingarea.challengesplugin.utils.commons;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author anweisen
 * Challenges developed on 07-13-2020
 * https://github.com/anweisen
 */

public class Charsets {

	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
	public static final Charset US_ASCII = StandardCharsets.US_ASCII;
	public static final Charset UTF_16 = StandardCharsets.UTF_16;
	public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;
	public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static SortedMap<String, Charset> requiredCharsets() {
		TreeMap<String, Charset> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		map.put(ISO_8859_1.name(), ISO_8859_1);
		map.put(US_ASCII.name(), US_ASCII);
		map.put(UTF_16.name(), UTF_16);
		map.put(UTF_16BE.name(), UTF_16BE);
		map.put(UTF_16LE.name(), UTF_16LE);
		map.put(UTF_8.name(), UTF_8);
		return Collections.unmodifiableSortedMap(map);
	}

	public static Charset toCharset(Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

	public static Charset toCharset(String charset) {
		return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
	}

}
