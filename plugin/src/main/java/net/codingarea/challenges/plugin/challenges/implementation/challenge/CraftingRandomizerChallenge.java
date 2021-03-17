package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.RandomizerSetting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class CraftingRandomizerChallenge extends RandomizerSetting {

	protected final Map<Material, Material> randomization = new HashMap<>();

	public CraftingRandomizerChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.CHEST_MINECART, Message.forName("item-crafting-randomizer-challenge"));
	}

	@Override
	protected void reloadRandomization() {

		List<Material> from = new ArrayList<>(Arrays.asList(Material.values()));
		from.removeIf(material -> !material.isItem() || !ItemUtils.isObtainableInSurvival(material));
		Collections.shuffle(from, random);

		List<Material> to = new ArrayList<>(Arrays.asList(Material.values()));
		to.removeIf(material -> !material.isItem() || !ItemUtils.isObtainableInSurvival(material));
		Collections.shuffle(to, random);

		while (!from.isEmpty()) {
			Material item = from.remove(0);
			Material result = to.remove(0);

			randomization.put(item, result);
		}

	}

	@Override
	protected void onDisable() {
		randomization.clear();
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCraftItem(@Nonnull CraftItemEvent event) {
		try {
			if (!isEnabled()) return;
			event.setCurrentItem(new ItemStack(randomization.get(event.getCurrentItem().getType())));
		} catch (NullPointerException ex) {
		}
	}

}
