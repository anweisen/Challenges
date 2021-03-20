package net.codingarea.challenges.plugin.utils.database.internal.mongodb.where;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.Filters;
import net.codingarea.challenges.plugin.utils.misc.MongoUtils;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ObjectWhere implements MongoDBWhere {

	protected final String field;
	protected final Object value;

	public ObjectWhere(@Nonnull String field, Object value) {
		this.field = field;
		this.value = MongoUtils.packObject(value);
	}

	@Nonnull
	@Override
	public Bson toBson() {
		return Filters.eq(field, value);
	}

	@Nullable
	@Override
	public Collation getCollation() {
		return null;
	}

}
