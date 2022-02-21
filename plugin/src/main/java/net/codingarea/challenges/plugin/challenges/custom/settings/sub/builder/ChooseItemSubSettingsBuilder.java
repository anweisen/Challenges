package net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.IParentCustomGenerator;
import net.codingarea.challenges.plugin.management.menu.generator.implementation.custom.SubSettingChooseMenuGenerator;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ChooseItemSubSettingsBuilder extends SubSettingsBuilder {

  protected final LinkedHashMap<String, ItemStack> settings = new LinkedHashMap<>();
  protected final String key;

  public ChooseItemSubSettingsBuilder(String key) {
    super(key);
    this.key = key;
  }

  public ChooseItemSubSettingsBuilder(String key, SubSettingsBuilder parent) {
    super(key, parent);
    this.key = key;
  }

  @Override
  public boolean open(Player player, IParentCustomGenerator parentGenerator, String title) {

    if (hasSettings()) {
      SubSettingChooseMenuGenerator generator = new SubSettingChooseMenuGenerator(key, parentGenerator, getSettings(), title);
      generator.open(player, 0);
      return true;
    }

    return false;
  }

  @Override
  public List<String> getDisplay(Map<String, String[]> activated) {
    List<String> display = Lists.newLinkedList();

    for (Entry<String, String[]> entry : activated.entrySet()) {
      if (entry.getKey().equals(key)) {
        for (String value : entry.getValue()) {
          ItemStack itemStack = getSettings().get(value);
          if (itemStack != null) {
            display.add("§7" + StringUtils.getEnumName(entry.getKey()) + " " + itemStack.getItemMeta().getDisplayName());

          }
        }
      }
    }

    return display;
  }

  public ChooseItemSubSettingsBuilder addSetting(String key, ItemStack value) {
    settings.put(key, value);
    return this;
  }

  public ChooseItemSubSettingsBuilder addSetting(String key, ItemBuilder value) {
    settings.put(key, value.hideAttributes().build());
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
