package net.codingarea.challenges.plugin.utils.config.document.readonly;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.BsonDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class ReadOnlyBsonDocument extends BsonDocument {

	public ReadOnlyBsonDocument(@Nonnull File file) throws IOException {
		super(file);
	}

	public ReadOnlyBsonDocument(@Nonnull Reader reader) {
		super(reader);
	}

	public ReadOnlyBsonDocument(@Nonnull org.bson.Document bsonDocument) {
		super(bsonDocument);
	}

	public ReadOnlyBsonDocument() {
		super();
	}

	@Nonnull
	@Override
	@Deprecated
	public Document set(@Nonnull String path, @Nullable Object value) {
		throw new UnsupportedOperationException("ReadOnlyGsonDocument.set(String, Object)");
	}

	@Nonnull
	@Override
	@Deprecated
	public Document remove(@Nonnull String path) {
		throw new UnsupportedOperationException("ReadOnlyGsonDocument.remove(String)");
	}

	@Nonnull
	@Override
	@Deprecated
	public Document clear() {
		throw new UnsupportedOperationException("ReadOnlyGsonDocument.clear()");
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		return new ReadOnlyDocumentWrapper(super.getDocument(path));
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

}
