package net.codingarea.challengesplugin.listener;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class InventoryClickListener implements Listener {

    private Challenges plugin;

    public InventoryClickListener(Challenges plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void handle(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null) return;

        String title = event.getView().getTitle();

        if (title.contains(Challenges.getInstance().getStringManager().MENU_TITLE)) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();
        int page = plugin.getMenuManager().getClickHandler().getPageByInventoryTitle(title) - 1;

        MenuType menu = null;

        if (title.equals(plugin.getStringManager().MENU_TITLE)) {
            byte[] slots = plugin.getMenuManager().getMainMenuSlots();
            final byte slot = (byte) event.getSlot();

            if (slot == slots[MenuType.GOALS.getPageID()]) {
                open(event.getWhoClicked(), MenuType.GOALS);
            } else if (slot == slots[MenuType.CHALLENGES.getPageID()]) {
                open(event.getWhoClicked(), MenuType.CHALLENGES);
            } else if (slot == slots[MenuType.SETTINGS.getPageID()]) {
                open(event.getWhoClicked(), MenuType.SETTINGS);
            } else if (slot == slots[MenuType.BLOCK_ITEMS.getPageID()]) {
                open(event.getWhoClicked(), MenuType.BLOCK_ITEMS);
            } else if (slot == slots[MenuType.DIFFICULTY.getPageID()]) {
                open(event.getWhoClicked(), MenuType.DIFFICULTY);
            } else if (slot == slots[MenuType.DAMAGE.getPageID()]) {
                open(event.getWhoClicked(), MenuType.DAMAGE);
            } else if (slot == slots[MenuType.values().length]) {
                plugin.getChallengeTimer().getMenu().openMainMenu(player);
            }

            AnimationSound.STANDARD_SOUND.play(player);

        } else if (title.startsWith(plugin.getStringManager().SETTINGS_TITLE)) {
            menu = MenuType.SETTINGS;
        } else if (title.startsWith(plugin.getStringManager().CHALLENGES_TITLE)) {
            menu = MenuType.CHALLENGES;
        } else if (title.startsWith(plugin.getStringManager().GOALS_TITLE)) {
            menu = MenuType.GOALS;
        } else if (title.startsWith(plugin.getStringManager().DIFFICULTY_TITLE)) {
            menu = MenuType.DIFFICULTY;
        } else if (title.startsWith(plugin.getStringManager().BLOCKS_TITLE)) {
            menu = MenuType.BLOCK_ITEMS;
        } else if (title.startsWith(plugin.getStringManager().DAMAGE_TITLE)) {
            menu = MenuType.DAMAGE;
        } else if (title.equals(plugin.getStringManager().TIMER_TITLE)) {
            plugin.getChallengeTimer().getMenu().handleClick(event);
            return;
        }

        if (menu != null && !plugin.getMenuManager().getClickHandler().handle(event, page, menu)) {
            AnimationSound.STANDARD_SOUND.play(player);
        }

    }

    private void open(HumanEntity entity, MenuType type) {
        try {
            entity.openInventory(plugin.getMenuManager().getMenus().get(type.getPageID()).get(0));
        } catch (IndexOutOfBoundsException ex) {
            try {
                ((Player)entity).playSound(entity.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1F, 1F);
            } catch (ClassCastException ignored) { }
        }
    }

}
