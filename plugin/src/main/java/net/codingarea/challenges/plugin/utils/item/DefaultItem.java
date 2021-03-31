package net.codingarea.challenges.plugin.utils.item;

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
		return new ItemBuilder(Material.LIME_DYE).setName(name("§aEnabled")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder disabled() {
		return new ItemBuilder(MaterialWrapper.RED_DYE).setName(name("§cDisabled")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder customize() {
		return new ItemBuilder(MaterialWrapper.SIGN).setName(name("§6Customize")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder value(int number) {
		return value(number, "§7");
	}

	@Nonnull
	public static ItemBuilder value(int number, @Nonnull String prefix) {
		return new ItemBuilder(Material.STONE_BUTTON).setName(name(prefix + number)).setAmount(Math.max(number, 1));
	}

	@Nonnull
	public static String name(@Nonnull String name) {
		return "§8➟ " + name;
	}

	public static ItemBuilder create(@Nonnull Material material, @Nonnull String name) {
		return new ItemBuilder(material, name(name));
	}

}
