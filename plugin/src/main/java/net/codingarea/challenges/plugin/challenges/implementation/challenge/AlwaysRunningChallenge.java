package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.bukkit.wrapper.BukkitReflectionUtils;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.EntityUtils;
import org.apache.commons.lang3.event.EventUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
public class AlwaysRunningChallenge extends Setting {

	public AlwaysRunningChallenge() {
		super(MenuType.CHALLENGES);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.COBBLESTONE_SLAB, Message.forName("item-always-running-challenge"));
	}

	@ScheduledTask(ticks = 1)
	public void setVelocity() {
		broadcastFiltered(player -> {
			if (player.isSprinting()) return;

			Location location = player.getLocation();
			Vector velocity = new Vector();

			double rotX = location.getYaw();
			double xz = Math.cos(Math.toRadians(0));
			velocity.setX(-xz * Math.sin(Math.toRadians(rotX)));
			velocity.setZ(xz * Math.cos(Math.toRadians(rotX)));
			velocity.multiply(0.5);

			if (player.isSneaking())
				velocity.multiply(0.5);
			else if (BukkitReflectionUtils.isInWater(player))
				velocity.multiply(0.5);
			else if (!player.isOnGround())
				velocity.multiply(0.85);

			Vector oldVelocity = player.getVelocity();
			if (oldVelocity.getY() > 0)
				oldVelocity.multiply(0.9);
			velocity.setY(oldVelocity.getY());
			player.setVelocity(velocity);
		});
	}

}
