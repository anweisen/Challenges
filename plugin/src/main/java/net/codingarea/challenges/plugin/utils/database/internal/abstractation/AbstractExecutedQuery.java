package net.codingarea.challenges.plugin.utils.database.internal.abstractation;

import net.codingarea.challenges.plugin.utils.database.ExecutedQuery;
import net.codingarea.challenges.plugin.utils.database.Result;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractExecutedQuery implements ExecutedQuery {

	protected final List<Result> results;

	public AbstractExecutedQuery(@Nonnull List<Result> results) {
		this.results = results;
	}

	@Nonnull
	@Override
	public Optional<Result> first() {
		if (results.isEmpty()) return Optional.empty();
		return Optional.ofNullable(results.get(0));
	}

	@Nonnull
	@Override
	public Optional<Result> get(int index) {
		if (index >= results.size()) return Optional.empty();
		return Optional.ofNullable(results.get(index));
	}

	@Nonnull
	@Override
	public Stream<Result> all() {
		return results.stream();
	}

	@Override
	public boolean isEmpty() {
		return results.isEmpty();
	}

	@Override
	public boolean isSet() {
		return !results.isEmpty();
	}

	@Override
	public void print() {
		if (results.isEmpty()) {
			System.out.println("<Empty Result>");
			return;
		}

		int index = 0;
		for (Result result : results) {
			System.out.print(index + ". | ");
			for (Entry<String, Object> entry : result.values().entrySet()) {
				System.out.print(entry.getKey() + " = '" + entry.getValue() + "' ");
			}
			System.out.println();
			index++;
		}
	}

}
