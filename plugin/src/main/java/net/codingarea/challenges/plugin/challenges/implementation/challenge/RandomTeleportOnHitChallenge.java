package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import java.util.List;
import net.anweisen.utilities.common.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.EntityDamageByPlayerEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.3
 */
@Since("2.1.3")
public class RandomTeleportOnHitChallenge extends Setting {

	public RandomTeleportOnHitChallenge() {
		super(MenuType.CHALLENGES);
	}

	@NotNull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.ENDER_CHEST, Message.forName("item-mob-damage-teleport-challenge"));
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByPlayer(EntityDamageByPlayerEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getDamager())) return;

		World world = event.getDamager().getWorld();
		List<LivingEntity> livingEntities = world.getLivingEntities();
		LivingEntity entity = globalRandom.choose(livingEntities);

		entity.setInvisible(true);
		Location playerLocation = event.getDamager().getLocation();
		event.getDamager().teleport(entity.getLocation());
		entity.teleport(playerLocation);
		entity.setInvisible(false);
	}

}
