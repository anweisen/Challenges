package net.codingarea.challenges.plugin.utils.item;

import net.anweisen.utilities.bukkit.utils.item.MaterialWrapper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class DefaultItem {

	@Nonnull
	public static ItemBuilder navigateBack() {
		return new SkullBuilder("MHF_ArrowLeft").setName(Message.forName("navigate-back")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder navigateNext() {
		return new SkullBuilder("MHF_ArrowRight").setName(Message.forName("navigate-next")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder navigateBackMainMenu() {
		return new ItemBuilder(Material.DARK_OAK_DOOR).setName(Message.forName("navigate-back")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder status(boolean enabled) {
		return enabled ? enabled() : disabled();
	}

	@Nonnull
	public static ItemBuilder enabled() {
		return new ItemBuilder(Material.LIME_DYE).setName(getTitle("enabled")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder disabled() {
		return new ItemBuilder(MaterialWrapper.RED_DYE).setName(getTitle("disabled")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder customize() {
		return new ItemBuilder(MaterialWrapper.SIGN).setName(getTitle("customize")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder value(int value) {
		return value(value, "§e");
	}

	@Nonnull
	public static ItemBuilder value(int value, @Nonnull String prefix) {
		return create(Material.STONE_BUTTON, prefix + value).amount(value);
	}

	@Nonnull
	public static String name(@Nonnull String name) {
		return "§8➟ " + name;
	}

	public static ItemBuilder create(@Nonnull Material material, @Nonnull String name) {
		return new ItemBuilder(material, name(name));
	}

	public static ItemBuilder create(@Nonnull Material material, @Nonnull Message message) {
		ItemBuilder itemBuilder = new ItemBuilder(material, message);
		return itemBuilder.setName(name(itemBuilder.getName()));
	}

	private static String getTitle(@Nonnull String messageName) {
		return Message.forName("item-setting-info").asString(Message.forName(messageName));
	}

}
