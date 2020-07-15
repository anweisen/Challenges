package net.codingarea.challengesplugin.utils;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-22-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class Replacement<T> {

	private final T toReplace;
	private final T replacement;

	public Replacement(T toReplace, T replacement) {
		this.toReplace = toReplace;
		this.replacement = replacement;
	}

	public T getReplacement() {
		return replacement;
	}

	public T getToReplace() {
		return toReplace;
	}

	@SafeVarargs
	public static String replace(String string, Replacement<String>... replacements) {

		for (Replacement<String> currentReplacement : replacements) {
			string = string.replace(currentReplacement.toReplace, currentReplacement.replacement);
		}

		return string;

	}

}
