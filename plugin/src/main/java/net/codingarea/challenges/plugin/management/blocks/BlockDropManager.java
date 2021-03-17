package net.codingarea.challenges.plugin.management.blocks;

import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class BlockDropManager {

	public static final class DropPriority {

		private DropPriority() {
		}

		public static final byte    CUT_CLEAN = 10,
									RANDOMIZER = 5;

	}

	private class RegisteredDrops {

		private final SortedMap<Byte, List<Material>> dropsByPriority = new TreeMap<>(Collections.reverseOrder());

		public void setCustomDrop(byte priority, @Nonnull List<Material> drops) {
			dropsByPriority.put(priority, drops);
		}

		public void resetCustomDrop(byte priority) {
			dropsByPriority.remove(priority);
		}

	}

	private final Map<Material, RegisteredDrops> drops = new HashMap<>();

	@Nullable
	public List<Material> getCustomDrops(@Nonnull Material block) {
		RegisteredDrops drops = this.drops.get(block);
		if (drops == null) return null;
		return drops.dropsByPriority.values().stream().findFirst().orElse(null);
	}

	public void setCustomDrops(@Nonnull Material block, @Nonnull Material item, byte priority) {
		setCustomDrops(block, Collections.singletonList(item), priority);
	}

	public void setCustomDrops(@Nonnull Material block, @Nonnull List<Material> items, byte priority) {
		Logger.debug("Setting block drop for " + block + " to " + items + " at priority " + priority);

		RegisteredDrops drops = this.drops.get(block);
		if (drops == null) {
			this.drops.put(block, drops = new RegisteredDrops());
		}

		drops.setCustomDrop(priority, items);
	}

	public void resetCustomDrop(@Nonnull Material block, byte priority) {
		Logger.debug("Resetting block drop for " + block + " at priority " + priority);

		RegisteredDrops drops = this.drops.get(block);
		if (drops == null) return;

		drops.resetCustomDrop(priority);
		if (drops.dropsByPriority.isEmpty()) this.drops.remove(block);
	}

	public void resetCustomDrops(byte priority) {
		Logger.debug("Resetting block drops at priority " + priority);

		List<Material> remove = new ArrayList<>();
		for (Entry<Material, RegisteredDrops> entry : drops.entrySet()) {

			RegisteredDrops drops = entry.getValue();
			drops.dropsByPriority.remove(priority);
			if (drops.dropsByPriority.isEmpty()) remove.add(entry.getKey());
		}

		remove.forEach(drops::remove);
	}

}
