package net.codingarea.challenges.plugin.utils.bukkit.validator;

import net.codingarea.challenges.plugin.utils.logging.ConsolePrint;
import net.codingarea.challenges.plugin.utils.misc.Utils;

import javax.annotation.CheckReturnValue;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ServerValidator {

	private ServerValidator() {}

	@CheckReturnValue
	public static boolean validate() {
		if (!Utils.isSpigot()) {
			ConsolePrint.notSpigot();
			return true;
		}
		return false;
	}

}
