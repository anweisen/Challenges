package net.codingarea.challenges.plugin.challenges.custom.settings.sub.impl;

import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class BooleanSetting extends ValueSetting {


  public BooleanSetting(String key, ItemBuilder itemBuilder) {
    super(key, itemBuilder);
  }

  @Override
  public String onClick(String value, int slotIndex) {
    return value.equals("enabled") ? "disabled" : "enabled";
  }

  @Override
  public ItemBuilder getSettingsItem(String value) {
    return value.equals("enabled") ? DefaultItem.enabled() : DefaultItem.disabled();
  }

}
