package net.codingarea.challenges.plugin.challenges.implementation.goal;

import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.commons.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.type.CollectionGoal;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class CollectMostDeathsGoal extends CollectionGoal {

	public CollectMostDeathsGoal() {
		super((Object[]) DamageCause.values());
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.LAVA_BUCKET, Message.forName("item-most-deaths-goal"));
	}

	@EventHandler
	public void onDeath(@Nonnull PlayerDeathEvent event) {
		if (!shouldExecuteEffect()) return;

		EntityDamageEvent lastCause = event.getEntity().getLastDamageCause();
		if (lastCause == null) return;

		DamageCause cause = lastCause.getCause();
		if (cause == DamageCause.CUSTOM) return;

		collect(event.getEntity(), cause, () -> {
			Message.forName("death-collected").send(event.getEntity(), Prefix.CHALLENGES, StringUtils.getEnumName(cause));
			SoundSample.PLING.play(event.getEntity());
		});
	}

}
