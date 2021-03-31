package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
@Since("2.0")
public class StoneSightChallenge extends Setting {

	public StoneSightChallenge() {
		super(MenuType.CHALLENGES);
	}

	@ScheduledTask(ticks = 1, async = false)
	public void schedule() {

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) continue;

			RayTraceResult result = player.getWorld().rayTraceEntities(
					player.getEyeLocation(),
					player.getLocation().getDirection(),
					30,
					0.01,
					entity -> !(entity instanceof Player) && !(entity instanceof EnderDragon) && entity instanceof LivingEntity
			);
			if (result == null) continue;

			Location location = result.getHitPosition().toLocation(player.getWorld());
			LivingEntity entity = ((LivingEntity) result.getHitEntity());
			if (entity == null) continue;

			double distance = entity.getEyeLocation().distance(location) * 5;

			BoundingBox box = entity.getBoundingBox();
			double volume = box.getWidthX() + box.getWidthZ() + box.getHeight();
			if (distance > volume) continue;

			Bukkit.getScheduler().runTask(plugin, () -> {
				entity.getLocation().getBlock().setType(getRandomStone(), false);
				entity.remove();
				SoundSample.BREAK.play(player);
			});

		}

	}

	@Nonnull
	private Material getRandomStone() {
		Material[] materials = {
			Material.STONE,
			Material.STONE,
			Material.STONE,
			Material.COBBLESTONE,
			Material.COBBLESTONE,
			Material.MOSSY_COBBLESTONE
		};
		return materials[new Random().nextInt(materials.length)];
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.COBBLESTONE, Message.forName("item-stone-sight-challenge"));
	}

}