package net.codingarea.challenges.plugin.challenges.type;

import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.info.ChallengeMenuClickInfo;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.2
 */
public class EmptyChallenge implements IChallenge {

	private final MenuType menuType;

	public EmptyChallenge(@Nonnull MenuType menuType) {
		this.menuType = menuType;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void restoreDefaults() {

	}

	@Override
	public void handleShutdown() {

	}

	@NotNull
	@Override
	public String getUniqueName() {
		return "empty";
	}

	@NotNull
	@Override
	public MenuType getType() {
		return menuType;
	}

	@NotNull
	@Override
	public ItemStack getDisplayItem() {
		return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§0").build();
	}

	@NotNull
	@Override
	public ItemStack getSettingsItem() {
		return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§0").build();
	}

	@Override
	public void handleClick(@NotNull ChallengeMenuClickInfo info) {
		SoundSample.CLICK.play(info.getPlayer());
	}

	@Override
	public void writeSettings(@NotNull Document document) {

	}

	@Override
	public void loadSettings(@NotNull Document document) {

	}

	@Override
	public void writeGameState(@NotNull Document document) {

	}

	@Override
	public void loadGameState(@NotNull Document document) {

	}

}
