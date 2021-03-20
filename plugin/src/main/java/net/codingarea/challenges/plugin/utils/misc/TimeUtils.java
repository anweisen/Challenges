package net.codingarea.challenges.plugin.utils.misc;

import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public final class TimeUtils {

	private TimeUtils() { }

	public static BukkitRunnable createBukkitRunnable(@Nonnull Consumer<BukkitRunnable> runnable) {
		return new BukkitRunnable() {
			@Override
			public void run() {
				runnable.accept(this);
			}
		};
	};

}