package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.abstraction.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class DamageTeleportChallenge extends SettingModifier {

	private static final int PLAYER = 1, EVERYONE = 2;

	public DamageTeleportChallenge() {
		super(MenuType.CHALLENGES, 1, 2);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.SHULKER_SHELL, Message.forName("item-damage-teleport-challenge"));
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (getValue() == 1) {
			return DefaultItem.create(Material.ENDER_CHEST, Message.forName("everyone"));
		} else {
			return DefaultItem.create(Material.PLAYER_HEAD, Message.forName("player"));
		}
	}

	@Override
	public void playValueChangeTitle() {
		ChallengeHelper.playChangeChallengeValueTitle(this, getValue() == 0 ? Message.forName("everyone").asString() : Message.forName("player").asString());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDamage(@Nonnull EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (!shouldExecuteEffect()) return;
		if (ChallengeHelper.finalDamageNull(event)) return;

		handleDamage(((Player) event.getEntity()));
	}

	private void handleDamage(@Nonnull Player player) {

		Location location = player.getWorld().getHighestBlockAt(getRandomLocation(player.getWorld())).getLocation();
		location.setY(location.getY() + 1);
		location.getChunk().load(true);

		if (getValue() == PLAYER) {
			player.teleport(location);
		} else {
			broadcastFiltered(player1 -> player1.teleport(location));
		}

	}

	public Location getRandomLocation(World world) {
		final double randomX = ThreadLocalRandom.current().nextDouble(0, world.getWorldBorder().getSize() / 2);
		final double randomY = ThreadLocalRandom.current().nextDouble(0, world.getWorldBorder().getSize() / 2);
		return world.getWorldBorder().getCenter().add(randomX, 0, randomY);
	}

}