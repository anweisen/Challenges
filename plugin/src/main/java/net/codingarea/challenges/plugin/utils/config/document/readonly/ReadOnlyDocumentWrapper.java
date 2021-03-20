package net.codingarea.challenges.plugin.utils.config.document.readonly;

import net.codingarea.challenges.plugin.utils.config.Document;
import net.codingarea.challenges.plugin.utils.config.document.wrapper.DocumentWrapper;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class ReadOnlyDocumentWrapper extends DocumentWrapper {

	public ReadOnlyDocumentWrapper(@Nonnull Document document) {
		super(document);
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

	@Nonnull
	@Override
	public Document getDocument(@Nonnull String path) {
		return new ReadOnlyDocumentWrapper(document.getDocument(path));
	}

	@Nonnull
	@Override
	@Deprecated
	public Document set(@Nonnull String path, @Nullable Object value) {
		throw new UnsupportedOperationException("ReadonlyDocumentWrapper.set(String, Object)");
	}

	@Nonnull
	@Override
	@Deprecated
	public Document clear() {
		throw new UnsupportedOperationException("ReadonlyDocumentWrapper.clear()");
	}

	@Nonnull
	@Override
	@Deprecated
	public Document remove(@Nonnull String path) {
		throw new UnsupportedOperationException("ReadonlyDocumentWrapper.remove(String)");
	}

}
