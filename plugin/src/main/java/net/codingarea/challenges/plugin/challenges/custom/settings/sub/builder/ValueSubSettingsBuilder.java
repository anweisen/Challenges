package net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.impl.BooleanSetting;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.IParentCustomGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.SubSettingValueMenuGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ValueSubSettingsBuilder extends SubSettingsBuilder {

  private final LinkedHashMap<ValueSetting, String> defaultSettings = new LinkedHashMap<>();

  public ValueSubSettingsBuilder() {
  }

  public ValueSubSettingsBuilder(SubSettingsBuilder parent) {
    super(parent);
  }

  @Override
  public boolean open(Player player, IParentCustomGenerator parentGenerator, String title) {

    if (hasSettings()) {
      SubSettingValueMenuGenerator generator = new SubSettingValueMenuGenerator(parentGenerator, new LinkedHashMap<>(getDefaultSettings()), title);
      generator.open(player, 0);
      return true;
    }

    return false;
  }

  @Override
  public List<String> getDisplay(String[] activated) {
    List<String> display = Lists.newLinkedList();



    return display;
  }

  public ValueSubSettingsBuilder addBooleanSetting(ItemBuilder displayItem, boolean defaultValue) {
    defaultSettings.put(new BooleanSetting(displayItem), defaultValue ? "enabled" : "disabled");
    return this;
  }

  public ValueSubSettingsBuilder fill(Consumer<ValueSubSettingsBuilder> actions) {
    actions.accept(this);
    return this;
  }

  public boolean hasSettings() {
    return !defaultSettings.isEmpty();
  }

  public LinkedHashMap<ValueSetting, String> getDefaultSettings() {
    return defaultSettings;
  }

}
