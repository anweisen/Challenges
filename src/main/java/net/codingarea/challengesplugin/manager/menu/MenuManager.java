package net.codingarea.challengesplugin.manager.menu;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.utils.items.ItemBuilder;
import net.codingarea.challengesplugin.utils.Utils;
import net.codingarea.challengesplugin.utils.animation.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class MenuManager {

    private final Challenges plugin;

    /*
     * 22 Settings
     * 11 Challenges
     * 32 Goal
     * 25 Blocks
     * 19 Damage
     * 15 Difficulty
     * 30 Timer
     */
    private final byte[] mainMenuSlots = { 22, 11, 32, 25, 19, 15, 30 };
    private final byte[] slots = { 10, 11, 12, 13, 14, 15, 16, 17 };
    private final List<List<Inventory>> menus;
    private AnimatedInventory mainMenu;

    private final MenuClickHandler clickHandler;

    public MenuManager(Challenges plugin) {
        this.plugin = plugin;
        menus = new ArrayList<>();
        clickHandler = new MenuClickHandler(this);
    }

    public void load() {
        loadMainMenu();
        loadMenus();
    }

    public void loadMenus() {

        List<List<AbstractChallenge>> challengesOnMenus = new ArrayList<>();

        // This is creating a inventory list and a challenge list for every menu
        for (MenuType ignored : MenuType.values()) {
            challengesOnMenus.add(new ArrayList<>());
            this.menus.add(new ArrayList<>());
        }

        // This is going through all challenges and is adding them to the right menu's list
        for (AbstractChallenge currentChallenge : plugin.getChallengeManager().getChallenges()) {

            if (currentChallenge == null) continue;
            if (currentChallenge.getMenu() == null) continue;

            challengesOnMenus.get(currentChallenge.getMenu().getPageID()).add(currentChallenge);

        }

        // This is the inventory size for all challenge inventories
        int size = 36;

        for (MenuType currentMenuType : MenuType.values()) {

            int currentPage = 0;
            int challengesOnThisPageLoaded = 0;

            List<Inventory> currentMenu = this.menus.get(currentMenuType.getPageID());

            for (AbstractChallenge currentChallenge : challengesOnMenus.get(currentMenuType.getPageID())) {

                Inventory currentPageInventory;

                if (currentPage >= currentMenu.size()) {

                    /*
                     * This will be executed, when no inventory for this page was generated
                     * It will generate a new inventory and add it to the current menu's inventory list
                     */

                    String template;

                    if (currentMenuType == MenuType.SETTINGS) {
                        template = plugin.getStringManager().SETTINGS_TITLE;
                    } else if (currentMenuType == MenuType.GOALS) {
                        template = plugin.getStringManager().GOALS_TITLE;
                    } else if (currentMenuType == MenuType.CHALLENGES) {
                        template = plugin.getStringManager().CHALLENGES_TITLE;
                    } else if (currentMenuType == MenuType.BLOCK_ITEMS) {
                        template = plugin.getStringManager().BLOCKS_TITLE;
                    } else if (currentMenuType == MenuType.DAMAGE) {
                        template = plugin.getStringManager().DAMAGE_TITLE;
                    } else {
                        template = plugin.getStringManager().DIFFICULTY_TITLE;
                    }

                    String title = template + plugin.getStringManager().MENU_TITLE_END.replace("%page", (currentPage + 1) + "");

                    currentPageInventory = Bukkit.createInventory(null, size,  title);
                    currentMenu.add(currentPageInventory);
                    Utils.fillInventory(ItemBuilder.FILL_ITEM, currentPageInventory);

                } else {
                    currentPageInventory = currentMenu.get(currentPage);
                }

                currentChallenge.updateItem(currentPageInventory, slots[challengesOnThisPageLoaded]);

                challengesOnThisPageLoaded++;
                if ((slots.length % challengesOnThisPageLoaded) == 1) {
                    currentPage++;
                    challengesOnThisPageLoaded = 0;
                }

            }

            /*
             * This is cycling thought all Inventories and adding the change page items
             */
            for (int i = 0; i < currentMenu.size(); i++) {

                Inventory currentInventory = currentMenu.get(i);

                currentInventory.setItem(27, i != 0 ? ItemManager.getBackPageItem() : ItemManager.getBackMainMenuItem());

                if (i < (currentMenu.size() - 1)) {
                    currentInventory.setItem(35, ItemManager.getNextPageItem());
                }

            }

        }



    }

    private void loadMainMenu() {

        mainMenu = new AnimatedInventory(plugin.getStringManager().MENU_TITLE, 1, 5*9, AnimationSound.STANDARD_SOUND);
        mainMenu.addFrame(new AnimationFrame(5*9).fill(ItemBuilder.FILL_ITEM));
        mainMenu.addFrame(mainMenu.getFrame(0).clone().setItem(39, ItemBuilder.FILL_ITEM_2).setItem(41, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(1).clone().setItem(38, ItemBuilder.FILL_ITEM_2).setItem(42, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(2).clone().setItem(37, ItemBuilder.FILL_ITEM_2).setItem(43, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(3).clone().setItem(28, ItemBuilder.FILL_ITEM_2).setItem(34, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(4).clone().setItem(27, ItemBuilder.FILL_ITEM_2).setItem(35, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(5).clone().setItem(18, ItemBuilder.FILL_ITEM_2).setItem(26, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(6).clone().setItem(9, ItemBuilder.FILL_ITEM_2).setItem(17, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(7).clone().setItem(10, ItemBuilder.FILL_ITEM_2).setItem(16, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(8).clone().setItem(1, ItemBuilder.FILL_ITEM_2).setItem(7, ItemBuilder.FILL_ITEM_2));
        mainMenu.addFrame(mainMenu.getFrame(9).clone().setItem(2, ItemBuilder.FILL_ITEM_2).setItem(6, ItemBuilder.FILL_ITEM_2));

        mainMenu.addFrame(mainMenu.getFrame(10).clone().setItem(mainMenuSlots[MenuType.values().length], new ItemBuilder(Material.CLOCK, "§6Timer").build())
                                                       .setItem(mainMenuSlots[MenuType.GOALS.getPageID()], new ItemBuilder(Material.COMPASS, "§5Goal").build())
                                                       .setPlaySound(false));
        mainMenu.addFrame(mainMenu.getFrame(11).clone().setItem(mainMenuSlots[MenuType.DAMAGE.getPageID()], new ItemBuilder(Material.IRON_SWORD, "§7Damage").hideAttributes().build())
                                                       .setItem(mainMenuSlots[MenuType.BLOCK_ITEMS.getPageID()], new ItemBuilder(Material.STICK, "§4Blocks and items").build())
                                                       .setPlaySound(false));
        mainMenu.addFrame(mainMenu.getFrame(12).clone().setItem(mainMenuSlots[MenuType.CHALLENGES.getPageID()], new ItemBuilder(Material.BOOK, "§cChallenges").build())
                                                       .setItem(mainMenuSlots[MenuType.DIFFICULTY.getPageID()], new ItemBuilder(Material.ENCHANTED_BOOK, "§dDifficulty").hideAttributes().build())
                                                       .setPlaySound(false));
        mainMenu.addFrame(mainMenu.getFrame(13).clone().setItem(mainMenuSlots[MenuType.SETTINGS.getPageID()], new ItemBuilder(Material.COMPARATOR, "§eSettings").build())
                                                       .setPlaySound(false));

        mainMenu.setEndSound(AnimationSound.OPEN_SOUND);

    }

    public AnimatedInventory getMainMenu() {
        return mainMenu;
    }

    public byte[] getMainMenuSlots() {
        return mainMenuSlots;
    }

    public byte[] getSlots() {
        return slots;
    }

    public Challenges getPlugin() {
        return plugin;
    }

    public List<List<Inventory>> getMenus() {
        return menus;
    }

    public MenuClickHandler getClickHandler() {
        return clickHandler;
    }
}
