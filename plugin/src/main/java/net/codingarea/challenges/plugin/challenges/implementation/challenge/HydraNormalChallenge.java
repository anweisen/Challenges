package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.*;

import javax.annotation.Nonnull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class HydraNormalChallenge extends net.codingarea.challenges.plugin.challenges.type.HydraChallenge {

	public HydraNormalChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.WITCH_SPAWN_EGG, Message.forName("item-hydra-challenge"));
	}

	@Override
	public int getNewMobsCount(@Nonnull EntityType entityType) {
		return 2;
	}
}