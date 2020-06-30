package net.codingarea.challengesplugin.manager;

import net.codingarea.challengesplugin.manager.lang.LanguageManager;
import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.ItemBuilder.SkullBuilder;
import net.codingarea.challengesplugin.utils.ItemBuilder.TippedArrowBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class ItemManager {

    public static ItemStack getValueItem(int value) {
        return new ItemBuilder(Material.STONE_BUTTON, "§6" + value).hideAttributes().build();
    }

    public static ItemStack getActivatedItem() {
        String name = "Activated";
        if (LanguageManager.getLanguage() == Language.GERMAN) name = "Aktiviert";
        return new ItemBuilder(Material.LIME_DYE, "§a" + name).hideAttributes().build();
    }

    public static ItemStack getNotActivatedItem() {
        String name = "Disabled";
        if (LanguageManager.getLanguage() == Language.GERMAN) name = "Deaktiviert";

        return new ItemBuilder(Utils.getRedDye(), "§c" + name).hideAttributes().build();
    }

    public static ItemStack getActivationItem(boolean activated) {
        return activated ? getActivatedItem() : getNotActivatedItem();
    }

    public static ItemStack getNextPageItem() {
        return new SkullBuilder("MHF_ArrowRight", Translation.NEXT.get()).build();
    }

    public static ItemStack getBackPageItem() {
        return new SkullBuilder("MHF_ArrowLeft", Translation.BACK.get()).build();
    }

    public static ItemStack getBackMainMenuItem() {
        return new ItemBuilder(Material.DARK_OAK_DOOR, Translation.BACK.get()).build();
    }

    public static ItemStack getTimerStartedItem() {
        return new ItemBuilder(Material.LIME_DYE, Translation.TIMER_STARTED_ITEM.get()).build();
    }

    public static ItemStack getTimerStoppedItem() {
        return new ItemBuilder(Material.GRAY_DYE, Translation.TIMER_STOPPED_ITEM.get()).build();
    }

    public static ItemStack getTimerModeDownItem() {
        return new TippedArrowBuilder(Material.TIPPED_ARROW, PotionType.STRENGTH, Translation.TIMER_MODE_DOWN_ITEM.get()).hideAttributes().build();
    }

    public static ItemStack getTimerModeUpItem() {
        return new TippedArrowBuilder(Material.TIPPED_ARROW, PotionType.JUMP, Translation.TIMER_MODE_UP_ITEM.get()).hideAttributes().build();
    }

}
