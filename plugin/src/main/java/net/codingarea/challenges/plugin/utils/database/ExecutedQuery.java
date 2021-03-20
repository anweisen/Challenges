package net.codingarea.challenges.plugin.utils.database;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
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

	@Nonnull
	<C extends Collection<? super Result>> C into(@Nonnull C collection);

	void forEach(@Nonnull Consumer<? super Result> action);

	boolean isEmpty();

	boolean isSet();

	int size();

	void print();

}
