package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.generator.categorised.ChallengeCategory;
import net.codingarea.challenges.plugin.spigot.events.PlayerJumpEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class HigherJumpsChallenge extends Setting {

	public HigherJumpsChallenge() {
		super(MenuType.CHALLENGES);
		setCategory(ChallengeCategory.MOVEMENT);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.RABBIT_FOOT, Message.forName("item-higher-jumps-challenge"));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJump(@Nonnull PlayerJumpEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;

		int jumps = getPlayerData(event.getPlayer()).getInt("jumps") + 1;
		getPlayerData(event.getPlayer()).set("jumps", jumps);

		float y = jumps / 7f;
		event.getPlayer().setVelocity(new Vector().setY(y));
	}

}
