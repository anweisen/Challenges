package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.insertorupdate;

import net.codingarea.challenges.plugin.utils.database.DatabaseInsertionOrUpdate;
import net.codingarea.challenges.plugin.utils.database.exceptions.DatabaseException;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.AbstractSQLDatabase;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.update.SQLUpdate;
import net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.where.SQLWhere;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class SQLInsertionOrUpdate extends SQLUpdate implements DatabaseInsertionOrUpdate {

	public SQLInsertionOrUpdate(@Nonnull AbstractSQLDatabase database, @Nonnull String table) {
		super(database, table);
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate where(@Nonnull String column, @Nullable Object value) {
		super.where(column, value);
		return this;
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate where(@Nonnull String column, @Nullable Number value) {
		super.where(column, value);
		return this;
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate where(@Nonnull String column, @Nullable String value, boolean ignoreCase) {
		super.where(column, value);
		return this;
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate where(@Nonnull String column, @Nullable String value) {
		super.where(column, value);
		return this;
	}

	@Nonnull
	@Override
	public DatabaseInsertionOrUpdate set(@Nonnull String column, @Nullable Object value) {
		super.set(column, value);
		return this;
	}

	@Override
	public void execute() throws DatabaseException {
		if (database.query(table, where).execute().isSet()) {
			super.execute();
		} else {
			Map<String, Object> insert = new HashMap<>(values);
			for (Entry<String, SQLWhere> entry : where.entrySet()) {
				Object[] args = entry.getValue().getArgs();
				if (args.length == 0) continue;
				insert.put(entry.getKey(), args[0]);
			}

			database.insert(table, insert).execute();
		}
	}

}
