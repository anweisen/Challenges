package net.codingarea.challenges.plugin.management.scheduler.policy;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public enum PlayerCountPolicy implements IPolicy {

	ALWAYS  ((online, max) -> true),
	EMPTY   ((online, max) -> online == 0),
	SOMEONE((online, max) -> online > 0),
	FULL    ((online, max) -> online.equals(max));

	private final BiPredicate<Integer, Integer> check;

	PlayerCountPolicy(@Nonnull BiPredicate<Integer, Integer> check) {
		this.check = check;
	}

	@Override
	public boolean check(@Nonnull Object holder) {
		return check.test(Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers());
	}

}
