package net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl;

import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.utils.misc.MapUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class EntityDamageCondition extends AbstractChallengeCondition {

	public EntityDamageCondition(String name) {
		super(name, createEntityTypeSettingsBuilder());
	}

	@Override
	public Material getMaterial() {
		return Material.FLINT_AND_STEEL;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDeath(@Nonnull EntityDamageEvent event) {
		execute(event.getEntity(), MapUtils.createStringListMap("entity_type","any", event.getEntityType().name()));
	}

}
