package net.codingarea.challenges.plugin.utils.common;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Because this class is not implemented in versions of bukkit before 1.14, we just make our own.
 *
 * @author org.apache.commons.io
 * @since 1.2
 */

public class IOUtils {

	public static String toString(String url) throws IOException {
		return toString(new URL(url));
	}

	public static String toString(URL url) throws IOException {
		InputStream inputStream = url.openStream();
		String string;
		try {
			string = toString(inputStream);
		} finally {
			inputStream.close();
		}
		return string;
	}

	public static String toString(InputStream input) throws IOException {
		StringBuilderWriter writer = new StringBuilderWriter();
		copy(input, writer);
		return writer.toString();
	}

	public static void copy(InputStream input, Writer output) throws IOException {
		InputStreamReader in = new InputStreamReader(input, StandardCharsets.UTF_8);
		copy(in, output);
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		return count > 2147483647L ? -1 : (int) count;
	}

	public static long copyLarge(Reader input, Writer output) throws IOException {
		return copyLarge(input, output, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
		long count;
		int n;
		for(count = 0L; -1 != (n = input.read(buffer)); count += n) {
			output.write(buffer, 0, n);
		}
		return count;
	}

	public static HttpURLConnection createConnection(final String url) throws IOException {
		return (HttpURLConnection) new URL(url).openConnection();
	}

}
