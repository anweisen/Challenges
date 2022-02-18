package net.codingarea.challenges.plugin.management.menu.generator.implementation.custom;

import java.util.Map;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.management.menu.generator.ValueMenuGenerator;
import org.bukkit.entity.Player;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class SubSettingValueMenuGenerator extends ValueMenuGenerator {

  private final IParentCustomGenerator parent;
  private final String title;

  public SubSettingValueMenuGenerator(IParentCustomGenerator parent, Map<ValueSetting, String> settings, String title) {
    super(settings);
    this.title = title;
    this.parent = parent;
  }

  @Override
  public String[] getSubTitles(int page) {
    return new String[]{ "Create", title };
  }

  @Override
  public void onSaveItemClick(Player player) {
//    parent.accept(player, null,getSettings().values().toArray(new String[0]));
  }

  @Override
  public void onBackToMenuItemClick(Player player) {
    parent.decline(player);
  }

}
