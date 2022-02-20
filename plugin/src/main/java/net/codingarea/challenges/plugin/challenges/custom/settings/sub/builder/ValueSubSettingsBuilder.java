package net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.impl.BooleanSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.impl.ModifierSetting;
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
    super(null);
  }

  public ValueSubSettingsBuilder(SubSettingsBuilder parent) {
    super(null, parent);
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
  public List<String> getDisplay(Map<String, String[]> activated) {
    List<String> display = Lists.newLinkedList();



    return display;
  }

  public ValueSubSettingsBuilder addBooleanSetting(String key, ItemBuilder displayItem,
      boolean defaultValue) {
    defaultSettings.put(new BooleanSetting(key, displayItem),
        defaultValue ? "enabled" : "disabled");
    return this;
  }

  public ValueSubSettingsBuilder addModifierSetting(String key, ItemBuilder displayItem,
      int defaultValue, int min, int max) {
    defaultSettings.put(new ModifierSetting(key, min, max, displayItem),
        String.valueOf(defaultValue));
    return this;
  }

  public ValueSubSettingsBuilder addModifierSetting(String key, ItemBuilder displayItem,
      int defaultValue, int min, int max, Function<Integer, String> prefixGetter) {
    defaultSettings.put(new ModifierSetting(key, min, max, displayItem, prefixGetter),
        String.valueOf(defaultValue));
    return this;
  }

  public ValueSubSettingsBuilder addModifierSetting(String key, ItemBuilder displayItem,
      int defaultValue, int min, int max, Function<Integer, String> prefixGetter, Function<Integer, String> suffixGetter) {
    defaultSettings.put(new ModifierSetting(key, min, max, displayItem, prefixGetter, suffixGetter),
        String.valueOf(defaultValue));
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
