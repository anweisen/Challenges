package net.codingarea.challenges.plugin.challenges.custom.settings.condition.implementation;

import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.IChallengeCondition;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class EntityDamageByPlayerCondition implements IChallengeCondition {

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDeath(@Nonnull EntityDamageByEntityEvent event) {

		Entity damager = event.getDamager();

		if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
			damager = ((Player) ((Projectile) damager).getShooter());
		}

		if (damager instanceof Player) {
			execute(damager, MapUtils
					.createStringListMap("entity_type","any", event.getEntityType().name()));
		}

	}

}
