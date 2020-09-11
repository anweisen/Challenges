package net.codingarea.challengesplugin.challenges.challenges.randomizer;

import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.RandomizerUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-14-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class CraftingRandomizerChallenge extends Setting implements Listener {

    private static HashMap<Material, List<Material>> materials;

    public CraftingRandomizerChallenge() {
        super(MenuType.CHALLENGES);
        load();
    }

    @Override @NotNull
    public String getChallengeName() {
        return "craftingrandomizer";
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemBuilder(Material.CHEST_MINECART, ItemTranslation.CRAFTING_RANDOMIZER).build();
    }

    private void load() {
        materials = new HashMap<>();

        List<Material> drops = RandomizerUtil.getRandomizerDrops();
        List<Material> blocks = RandomizerUtil.getRandomizerDrops();
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

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!this.enabled || !Challenges.timerIsStarted()) return;
        try {
            event.setCurrentItem(new ItemStack(materials.get(event.getCurrentItem().getType()).get(0)));
        } catch (NullPointerException ignored) { }
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


    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    public static HashMap<Material, List<Material>> getMaterials() {
        return materials;
    }
}
