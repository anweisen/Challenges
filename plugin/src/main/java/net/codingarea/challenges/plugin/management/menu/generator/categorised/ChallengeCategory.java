package net.codingarea.challenges.plugin.management.menu.generator.categorised;

import net.anweisen.utilities.bukkit.utils.item.MaterialWrapper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.function.Supplier;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ChallengeCategory {

	public static final ChallengeCategory MISC = new ChallengeCategory(99, Material.MINECART, () -> Message.forName("category-misc"));
	public static final ChallengeCategory RANDOMIZER = new ChallengeCategory(1, Material.COMMAND_BLOCK, () -> Message.forName("category-randomizer"));
	public static final ChallengeCategory LIMITED_TIME = new ChallengeCategory(2, Material.CLOCK, () -> Message.forName("category-limited_time"));
	public static final ChallengeCategory FORCE = new ChallengeCategory(3, Material.BLUE_BANNER, () -> Message.forName("category-force"));
	public static final ChallengeCategory WORLD = new ChallengeCategory(4, Material.TNT, () -> Message.forName("category-world"));
	public static final ChallengeCategory DAMAGE = new ChallengeCategory(5, MaterialWrapper.RED_DYE, () -> Message.forName("category-damage"));
	public static final ChallengeCategory MOVEMENT = new ChallengeCategory(6, Material.RABBIT_FOOT, () -> Message.forName("category-movement"));
	public static final ChallengeCategory EFFECT = new ChallengeCategory(7, Material.FERMENTED_SPIDER_EYE, () -> Message.forName("category-effect"));
	public static final ChallengeCategory ENTITIES = new ChallengeCategory(4, Material.PIG_SPAWN_EGG, () -> Message.forName("category-entities"));
	public static final ChallengeCategory INVENTORY = new ChallengeCategory(8, Material.CHEST, () -> Message.forName("category-inventory"));
	public static final ChallengeCategory EXTRA_WORLD = new ChallengeCategory(9, Material.GRASS_BLOCK, () -> Message.forName("category-extra_world"));

	// Lowest priority will be displayed first
	private final int priority;
	private final Material material;
	private final Supplier<Message> messageSupplier;

	public ChallengeCategory(int priority, Material material, Supplier<Message> messageSupplier) {
		this.priority = priority;
		this.material = material;
		this.messageSupplier = messageSupplier;
	}

	public int getPriority() {
		return priority;
	}

	public ItemBuilder getDisplayItem() {
		return new ItemBuilder(material, messageSupplier.get());
	}

	public Material getMaterial() {
		return material;
	}

	public Supplier<Message> getMessageSupplier() {
		return messageSupplier;
	}

}
