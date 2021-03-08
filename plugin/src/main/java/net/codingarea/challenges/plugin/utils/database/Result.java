package net.codingarea.challenges.plugin.utils.database;

import net.codingarea.challenges.plugin.utils.config.Document;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface Result extends Document {

	@Nonnull
	@Override
	@Deprecated
	default Result set(@Nonnull String path, @Nullable Object value) {
		throw new UnsupportedOperationException("Result.set(String, Object)");
	}

	@Nonnull
	@Override
	@Deprecated
	default Result remove(@Nonnull String path) {
		throw new UnsupportedOperationException("Result.remove(String)");
	}

	@Nonnull
	@Override
	@Deprecated
	default Result clear() {
		throw new UnsupportedOperationException("Result.clear()");
	}

	@Override
	default void write(@Nonnull Writer writer) throws IOException {
		throw new UnsupportedOperationException("Result.write(Writer)");
	}

	@Override
	default boolean isReadonly() {
		return true;
	}

}
