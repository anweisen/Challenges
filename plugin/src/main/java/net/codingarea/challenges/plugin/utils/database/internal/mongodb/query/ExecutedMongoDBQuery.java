package net.codingarea.challenges.plugin.utils.database.internal.mongodb.query;

import net.codingarea.challenges.plugin.utils.database.internal.abstractation.AbstractExecutedQuery;
import org.bson.Document;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ExecutedMongoDBQuery extends AbstractExecutedQuery {

	public ExecutedMongoDBQuery(@Nonnull List<Document> documents) {
		super(new ArrayList<>(documents.size()));
		for (Document document : documents) {
			results.add(new MongoDBResult(document));
		}
	}

}
