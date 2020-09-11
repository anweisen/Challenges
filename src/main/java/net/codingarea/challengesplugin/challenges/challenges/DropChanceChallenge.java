package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.challengetypes.AdvancedChallenge;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author anweisen & Dominik
 * Challenges developed on 09-05-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class DropChanceChallenge extends AdvancedChallenge implements Listener {


    public DropChanceChallenge() {
        super(MenuType.CHALLENGES, 99, 1);
    }

    @Override
    public void onValueChange(ChallengeEditEvent event) {
        
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public void onTimeActivation() { }

    @EventHandler
    public void onItemDrop(EntityDeathEvent event) {

        int i = new Random().nextInt(100) + 1;

        if (i >= value) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }

    }

    @EventHandler
    public void onItemDrop(BlockBreakEvent event) {

        int i = new Random().nextInt(100) + 1;

        if (i >= value) {
            event.setExpToDrop(0);
            event.setDropItems(false);
        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        int i = new Random().nextInt(100) + 1;

        if (i >= value) {
            event.getItemDrop().remove();
        }

    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.FLINT).build();
    }
}
