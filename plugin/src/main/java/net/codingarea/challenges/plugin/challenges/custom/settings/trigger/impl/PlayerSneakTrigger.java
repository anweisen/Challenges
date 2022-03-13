package net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl;

import javax.annotation.Nonnull;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1
 */
public class PlayerSneakTrigger extends AbstractChallengeTrigger {

	public PlayerSneakTrigger(String name) {
		super(name);
	}

	@Override
	public Material getMaterial() {
		return Material.SANDSTONE_SLAB;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onJump(@Nonnull PlayerToggleSneakEvent event) {
		if (event.isSneaking()) {
			createData().entity(event.getPlayer()).execute();
		}
	}

}
