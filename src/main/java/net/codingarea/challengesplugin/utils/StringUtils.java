package net.codingarea.challengesplugin.utils;

/**
 * @author anweisen
 * Challenges developed on 07-01-2020
 * https://github.com/anweisen
 */

public class StringUtils {

	public static double compare(String origin, String comparision, boolean weightCase, boolean weightSpaces) {

		double percentage = 100;

		if (!weightSpaces) {
			origin = origin.replace(" ", "");
			comparision = comparision.replace(" ", "");
		}

		int originCharAmount = origin.length();
		int comparisionCharAmount = comparision.length();

		int charAmountDifference;
		boolean originIsLonger = originCharAmount > comparisionCharAmount;

		double charWeight = 100D / (double) (originIsLonger ? originCharAmount : comparisionCharAmount);

		if (originIsLonger) {
			charAmountDifference = originCharAmount - comparisionCharAmount;
		} else {
			charAmountDifference = comparisionCharAmount - originCharAmount;
		}

		percentage -= charWeight * charAmountDifference;

		char[] originChars = origin.toCharArray();
		char[] comparisionChars = comparision.toCharArray();

		for (int i = 0; i < originCharAmount && i < comparisionCharAmount; i++) {

			if (!charEquals(originChars[i], comparisionChars[i], !weightCase)) {
				percentage -= charWeight;
			}

		}

		return percentage;

	}

	private static boolean charEquals(char origin, char comparision, boolean ignoreCase) {

		String originChar = String.valueOf(origin);
		String comparisionChar = String.valueOf(comparision);

		return ignoreCase ? originChar.equalsIgnoreCase(comparisionChar) : originChar.equals(comparisionChar);

	}

}
