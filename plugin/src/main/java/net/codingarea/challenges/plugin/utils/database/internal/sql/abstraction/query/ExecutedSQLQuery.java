package net.codingarea.challenges.plugin.utils.database.internal.sql.abstraction.query;

import net.codingarea.challenges.plugin.utils.database.Result;
import net.codingarea.challenges.plugin.utils.database.internal.abstractation.AbstractExecutedQuery;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ExecutedSQLQuery extends AbstractExecutedQuery {

	public ExecutedSQLQuery(@Nonnull ResultSet result) throws SQLException {
		super(new ArrayList<>());

		ResultSetMetaData data = result.getMetaData();
		while (result.next()) {
			Map<String, Object> map = new HashMap<>();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				Object value = result.getObject(i);
				map.put(data.getColumnLabel(i), value);
			}
			Result row = new SQLResult(map);
			results.add(row);
		}
	}

}
