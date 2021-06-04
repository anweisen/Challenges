package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.LeatherArmorBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class NewEntityOnJumpChallenge extends Setting {

	private final Random random = new Random();

	public NewEntityOnJumpChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new LeatherArmorBuilder(Material.LEATHER_BOOTS, Message.forName("item-jump-entity-challenge")).setColor(Color.GREEN);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onJump(@Nonnull PlayerJumpEvent event) {
		if (ignorePlayer(event.getPlayer())) return;
		if (!shouldExecuteEffect()) return;
		spawnRandomEntity(event.getPlayer().getLocation());
	}

	private void spawnRandomEntity(@Nonnull Location location) {
		if (location.getWorld() == null) return;
		try {
			location.getWorld().spawnEntity(location, EntityType.values()[random.nextInt(EntityType.values().length)]);
		} catch (IllegalArgumentException | IllegalStateException ex) {
			spawnRandomEntity(location);
		}
	}

}