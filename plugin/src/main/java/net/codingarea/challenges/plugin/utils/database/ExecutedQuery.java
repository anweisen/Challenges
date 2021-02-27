package net.codingarea.challenges.plugin.utils.database;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public interface ExecutedQuery {

	@Nonnull
	@CheckReturnValue
	Optional<Result> first();

	@Nonnull
	@CheckReturnValue
	Optional<Result> get(int index);

	@Nonnull
	@CheckReturnValue
	Stream<Result> all();

	boolean isEmpty();

	boolean isSet();

	void print();

}
