package net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.IParentCustomGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.SubSettingChooseMenuGenerator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ChooseItemSubSettingsBuilder extends SubSettingsBuilder {

  private final LinkedHashMap<String, ItemStack> settings = new LinkedHashMap<>();

  public ChooseItemSubSettingsBuilder() {
  }

  public ChooseItemSubSettingsBuilder(SubSettingsBuilder parent) {
    super(parent);
  }

  @Override
  public boolean open(Player player, IParentCustomGenerator parentGenerator, String title) {

    if (hasSettings()) {
      SubSettingChooseMenuGenerator generator = new SubSettingChooseMenuGenerator(parentGenerator, getSettings(), title);
      generator.open(player, 0);
      return true;
    }

    return false;
  }

  @Override
  public List<String> getDisplay(String[] activated) {
    List<String> display = Lists.newLinkedList();

    // TRY DIFFERENT WAY OF DEFINING ACTIVE SETTINGS OR NAME ALL SETTINGS UNIQUE
    // IF TWO SETTINGS WHETHER THEY ARE AT THE SAME INDEX HAVE THE SAME NAME THE SYSTEM THINKS
    // ITS ACTIVATED!!!!!!!!!!!!!!
    for (Entry<String, ItemStack> entry : getSettings().entrySet()) {
      for (String key : activated) {
        if (entry.getKey().equals(key)) {
          display.add(entry.getValue().getItemMeta().getDisplayName());
        }
      }
    }

    return display;
  }

  public ChooseItemSubSettingsBuilder addSetting(String key, ItemStack value) {
    settings.put(key, value);
    return this;
  }

  public ChooseItemSubSettingsBuilder fill(Consumer<ChooseItemSubSettingsBuilder> actions) {
    actions.accept(this);
    return this;
  }

  public boolean hasSettings() {
    return !settings.isEmpty();
  }

  public LinkedHashMap<String, ItemStack> getSettings() {
    return settings;
  }

}
