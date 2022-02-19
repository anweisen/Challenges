package net.codingarea.challenges.plugin.challenges.custom.settings.sub;

import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class ValueSetting {

  private final String key;
  private final ItemBuilder itemBuilder;

  public ValueSetting(String key, ItemBuilder itemBuilder) {
    this.key = key;
    this.itemBuilder = itemBuilder;
  }

  public ItemBuilder createDisplayItem() {
    return itemBuilder;
  }

  public abstract String onClick(String value, int slotIndex);
  public ItemBuilder getDisplayItem(String value) {
    return createDisplayItem().hideAttributes();
  }
  public abstract ItemBuilder getSettingsItem(String value);

  public String getKey() {
    return key;
  }

}
