package net.codingarea.challengesplugin.challenges.challenges.randomizer;

import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.RandomizerUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-05-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class BlockRandomizerChallenge extends Setting implements Listener {

    @Getter private static HashMap<Material, List<Material>> materials;

    public BlockRandomizerChallenge() {
        this.menu = MenuType.CHALLENGES;
        load();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MINECART, ItemTranslation.BLOCK_DROP_RANDOMIZER).build();
    }

    private void load() {
        materials = new HashMap<>();

        List<Material> drops = RandomizerUtil.getRandomizerDrops();
        List<Material> blocks = RandomizerUtil.getRandomizerBlocks();
        Collections.shuffle(drops);
        Collections.shuffle(blocks);

        while (!blocks.isEmpty()) {

            Material currentBlock = blocks.remove(0);
            List<Material> list = new ArrayList<>();

            int addDrops = getDropsForBlock(blocks.size(), drops.size());
            for (int i = 0; i < addDrops; i++) {
                list.add(drops.remove(0));
            }

            materials.put(currentBlock, list);

        }

    }

    private int getDropsForBlock(int blocksRemaining, int itemsRemaining) {

        if ((blocksRemaining * 3) <= itemsRemaining - 3) {
            return 3;
        }
        if ((blocksRemaining * 2) <= itemsRemaining - 2) {
            return 2;
        }

        return 1;

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        event.setDropItems(false);

        for (Material material : materials.get(event.getBlock().getType())) {
            try {
                event.getBlock().getWorld().dropItem(
                        event.getBlock().getLocation().clone().add(0.5, 0.5, 0.5),
                        new ItemStack(material));
            } catch (Exception e) {
                System.out.println(material.name());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

}