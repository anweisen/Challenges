package net.codingarea.challengesplugin.challenges.goal;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.CountingGoal;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Prefix;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.animation.AnimationSound;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-02-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class MineDiamondsGoal extends CountingGoal implements Listener {

	public MineDiamondsGoal() {
		super(MenuType.GOALS);
	}

	@Override
	public @NotNull ItemStack getItem() {
		return new ItemBuilder(Material.DIAMOND_ORE, ItemTranslation.MINE_DIAMONDS).build();
	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent event) {

		if (!isCurrentGoal || !Challenges.timerIsStarted()) return;
		if (event.getBlock().getType() != Material.DIAMOND_ORE) return;
		if (event.getPlayer().getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) return;

		register(event.getPlayer());

	}

	private void register(Player player) {

		handleNewPoint(player);

		player.sendMessage(Prefix.CHALLENGES	+ Translation.COLLECT_DIAMONDS_NEW_DIAMOND.get().replace("%count%", points.get(player) + ""));

		AnimationSound.PLING_SOUND.play(player);

	}

}
