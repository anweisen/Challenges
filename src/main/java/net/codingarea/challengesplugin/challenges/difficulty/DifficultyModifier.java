package net.codingarea.challengesplugin.challenges.difficulty;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class DifficultyModifier extends Modifier {

    public DifficultyModifier() {
        super(MenuType.DIFFICULTY, 4);
        value = getValueByDifficulty(Bukkit.getWorlds().get(0).getDifficulty());
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.MELON_SLICE, ItemTranslation.DIFFICULTY).hideAttributes().getItem();
    }

    @Override
    public void onMenuClick(ChallengeEditEvent event) {
        if (this.value == 1) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:difficulty peaceful");
        } else if (this.value == 2) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:difficulty easy");
        } else if (this.value == 3) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:difficulty normal");
        } else if (this.value == 4) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:difficulty hard");
        }

    }

    @Override
    public ItemStack getActivationItem() {
        if (this.value == 1) {
            return new ItemBuilder(Material.LIME_DYE, "§aPeaceful").getItem();
        } else if (this.value == 2) {
            return new ItemBuilder(Utils.getGreenDye(), "§2Easy").getItem();
        } else if (this.value == 3) {
            return new ItemBuilder(Material.ORANGE_DYE, "§6Normal").getItem();
        } else {
            return new ItemBuilder(Utils.getRedDye(), "§cHard").getItem();
        }
    }

    private int getValueByDifficulty(Difficulty difficulty) {
        if (difficulty == Difficulty.PEACEFUL) {
            return 1;
        } else if (difficulty == Difficulty.EASY) {
            return 2;
        } else if (difficulty == Difficulty.NORMAL) {
            return 3;
        } else {
            return 4;
        }
    }

}