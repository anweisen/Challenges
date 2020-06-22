package net.codingarea.challengesplugin.utils;

/**
 * @author anweisen
 * Challenges developed on 06-22-2020
 * https://github.com/anweisen
 */

public class Replacement {

	private String toReplace;
	private String replacement;

	public Replacement(String toReplace, String replacement) {
		this.toReplace = toReplace;
		this.replacement = replacement;
	}

	public String getReplacement() {
		return replacement;
	}

	public String getToReplace() {
		return toReplace;
	}

	public static String replace(String string, Replacement... replacements) {

		for (Replacement currentReplacement : replacements) {
			string = string.replace(currentReplacement.toReplace, currentReplacement.replacement);
		}

		return string;

	}

}
