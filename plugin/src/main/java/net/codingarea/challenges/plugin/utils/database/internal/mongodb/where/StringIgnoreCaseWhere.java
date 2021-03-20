package net.codingarea.challenges.plugin.utils.database.internal.mongodb.where;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class StringIgnoreCaseWhere implements MongoDBWhere {

	protected final String field;
	protected final String value;

	public StringIgnoreCaseWhere(@Nonnull String field, @Nonnull String value) {
		this.field = field;
		this.value = value;
	}

	@Nonnull
	@Override
	public Bson toBson() {
		return Filters.eq(field, value);
	}

	@Nullable
	@Override
	public Collation getCollation() {
		return Collation.builder().collationStrength(CollationStrength.SECONDARY).locale("en").build();
	}

}
