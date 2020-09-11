package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.difficulty.SplitHealthSetting;
import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 06-26-2020
 * https://github.com/anweisen
 */

public class BlockBreakDamage extends AdvancedChallenge implements Listener {

	public BlockBreakDamage() {
		super(MenuType.CHALLENGES, 60);
	}

	@Override
	public void onValueChange(ChallengeEditEvent event) { }

	@Override
	public void onEnable(ChallengeEditEvent event) { }

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onTimeActivation() { }

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.STONE, ItemTranslation.BLOCK_BREAK_DAMAGE).build();
	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {
		if (!enabled || !Challenges.timerIsStarted()) return;
		event.getPlayer().damage(value);
		event.getPlayer().setNoDamageTicks(0);
		SplitHealthSetting.sync(event.getPlayer());
	}

}
