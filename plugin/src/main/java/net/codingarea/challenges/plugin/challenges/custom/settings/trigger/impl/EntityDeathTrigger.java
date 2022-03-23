package net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl;

import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class EntityDeathTrigger extends AbstractChallengeTrigger {

	public EntityDeathTrigger(String name) {
		super(name, SubSettingsHelper.createEntityTypeSettingsBuilder());
	}

	@Override
	public Material getMaterial() {
		return Material.BONE;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onDeath(@Nonnull EntityDeathEvent event) {
		createData()
				.entity(event.getEntity())
				.entityType(event.getEntityType())
				.execute();
	}

}
