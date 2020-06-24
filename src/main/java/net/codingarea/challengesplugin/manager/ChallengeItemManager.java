package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.ItemBuilder.SkullBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ChallengeItemManager {

    public ItemStack getValueItem(int value) {
        return new ItemBuilder(Material.STONE_BUTTON, "§6" + value).hideAttributes().build();
    }

    public ItemStack getActivatedItem() {
        String name = "Activated";
        if (LanguageManager.getLanguage() == Language.GERMAN) name = "Aktiviert";
        return new ItemBuilder(Material.LIME_DYE, "§a" + name).hideAttributes().build();
    }

    public ItemStack getNotActivatedItem() {
        String name = "Disabled";
        if (LanguageManager.getLanguage() == Language.GERMAN) name = "Deaktiviert";

        return new ItemBuilder(Utils.getRedDye(), "§c" + name).hideAttributes().build();
    }

    public ItemStack getActivationItem(boolean activated) {
        return activated ? getActivatedItem() : getNotActivatedItem();
    }

    public ItemStack getNextPageItem() {
        return new SkullBuilder("MHF_ArrowRight", Translation.NEXT.get()).build();
    }

    public ItemStack getBackPageItem() {
        return new SkullBuilder("MHF_ArrowLeft", Translation.BACK.get()).build();
    }

    public ItemStack getBackMainMenuItem() {
        return new ItemBuilder(Material.DARK_OAK_DOOR, Translation.BACK.get()).build();
    }

}
