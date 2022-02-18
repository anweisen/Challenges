package net.codingarea.challenges.plugin.management.menu.generator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Nonnull;
import net.anweisen.utilities.bukkit.utils.animation.SoundSample;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.anweisen.utilities.bukkit.utils.menu.MenuPosition;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.management.menu.InventoryTitleManager;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.menu.position.GeneratorMenuPosition;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class ValueMenuGenerator extends MultiPageMenuGenerator {

  private final Map<ValueSetting, String> settings;

  public ValueMenuGenerator(Map<ValueSetting, String> settings) {
    this.settings = settings;
  }

  @Override
  public MenuPosition getMenuPosition(int page) {
    return new GeneratorMenuPosition(this, page) {

      @Override
      public void handleClick(@Nonnull MenuClickInfo info) {

        if (InventoryUtils.handleNavigationClicking(generator, getNavigationSlots(page), page, info)) {
          onBackToMenuItemClick(info.getPlayer());
          return;
        }
        if (info.getSlot() == 35) {
          onSaveItemClick(info.getPlayer());
          return;
        }

        int slot = info.getSlot();

        int index = getItemsPerPage() * page + slot - 10;

        if (slot > 18) index -= 9;

        if (index >= settings.size() || index < 0) {
          SoundSample.CLICK.play(info.getPlayer());
          return;
        }

        ValueSetting[] array = settings.keySet().toArray(new ValueSetting[0]);

        ValueSetting key = array[index];
        String oldValue = settings.get(key);

        int itemIndex = slot >= 18 ? 1 : 0;
        String newValue = key.onClick(oldValue, itemIndex);
        settings.put(key, newValue);
        generatePage(info.getInventory(), page);
        open(info.getPlayer(), page);
      }
    };
  }

  @Override
  public int getSize() {
    return 4*9;
  }

  @Override
  public int getPagesCount() {
    return (settings.size() / getItemsPerPage()) + 1;
  }

  public int getItemsPerPage() {
    return 7;
  }

  @Override
  public void generatePage(@Nonnull Inventory inventory, int page) {

    int startIndex = getItemsPerPage() * page;
    for (int i = startIndex; i < startIndex + getItemsPerPage() && i < settings.size(); i++) {
      int slot = i+10;
      ValueSetting key = settings.keySet().toArray(new ValueSetting[0])[i];
      String value = settings.get(key);
      inventory.setItem(slot, key.getDisplayItem(value).build());
      inventory.setItem(slot+9, key.getSettingsItem(value).build());
    }

  }

  @Override
  public int[] getNavigationSlots(int page) {
    return new int[]{ 27 };
  }

  @Override
  protected String getTitle(int page) {
    LinkedList<String> list = new LinkedList<>(Arrays.asList(getSubTitles(page)));
    list.add(String.valueOf(page+1));

    return InventoryTitleManager.getTitle(MenuType.CUSTOM, list.toArray(new String[0]));
  }

  public abstract String[] getSubTitles(int page);
  public abstract void onSaveItemClick(Player player);
  public abstract void onBackToMenuItemClick(Player player);

  public Map<ValueSetting, String> getSettings() {
    return settings;
  }

}
