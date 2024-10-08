package net.codingarea.challenges.plugin.utils.item;

import java.util.UUID;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.SkullBuilder;
import net.codingarea.challenges.plugin.utils.misc.MinecraftNameWrapper;
import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class DefaultItem {

	// Owner: MHF_ArrowLeft
	private static final UUID ARROW_LEFT_UUID = UUID.fromString("a68f0b64-8d14-4000-a95f-4b9ba14f8df9");
	// Owner: MHF_ArrowRight
	private static final UUID ARROW_RIGHT_UUID = UUID.fromString("50c8510b-5ea0-4d60-be9a-7d542d6cd156");

	@Nonnull
	public static String getItemPrefix() {
		return Message.forName("item-prefix").asString() + "§e";
	}

	@Nonnull
	public static ItemBuilder navigateBack() {
		return new SkullBuilder(ARROW_LEFT_UUID, "MHF_ArrowLeft").setName(Message.forName("navigate-back")).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder navigateNext() {
		return new SkullBuilder(ARROW_RIGHT_UUID, "MHF_ArrowRight").setName(Message.forName("navigate-next")).hideAttributes();
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
		return new ItemBuilder(Material.LIME_DYE).setName(getTitle(Message.forName("enabled"))).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder disabled() {
		return new ItemBuilder(MinecraftNameWrapper.RED_DYE).setName(getTitle(Message.forName("disabled"))).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder customize() {
		return new ItemBuilder(MinecraftNameWrapper.SIGN).setName(getTitle(Message.forName("customize"))).hideAttributes();
	}

	@Nonnull
	public static ItemBuilder value(int value) {
		return value(value, "§e");
	}

	@Nonnull
	public static ItemBuilder value(int value, @Nonnull String prefix) {
		return create(Material.STONE_BUTTON, prefix + value).amount(Math.max(value, 1));
	}

	@Nonnull
	public static ItemBuilder create(@Nonnull Material material, @Nonnull String name) {
		return new ItemBuilder(material, getTitle(name));
	}

	@Nonnull
	public static ItemBuilder create(@Nonnull Material material, @Nonnull Message message) {
		ItemBuilder itemBuilder = new ItemBuilder(material, message);
		return itemBuilder.setName(getTitle(message));
	}

	@Nonnull
	private static String getTitle(@Nonnull Message message) {
		return getTitle(message.asString());
	}

	@Nonnull
	private static String getTitle(@Nonnull String text) {
		return Message.forName("item-setting-info").asString(text);
	}

}
