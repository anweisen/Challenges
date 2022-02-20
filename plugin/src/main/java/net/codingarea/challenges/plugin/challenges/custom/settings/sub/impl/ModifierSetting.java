package net.codingarea.challenges.plugin.challenges.custom.settings.sub.impl;

import java.util.function.Function;
import net.anweisen.utilities.bukkit.utils.menu.MenuClickInfo;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.ValueSetting;
import net.codingarea.challenges.plugin.challenges.type.IModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class ModifierSetting extends ValueSetting implements IModifier {

  private final int min, max;
  private final Function<Integer, String> prefixGetter;
  private final Function<Integer, String> suffixGetter;

  private int tempValue;

  public ModifierSetting(String key, int min, int max, ItemBuilder itemBuilder) {
    this(key, min, max, itemBuilder, integer -> "");
  }

  public ModifierSetting(String key, int min, int max, ItemBuilder itemBuilder, Function<Integer, String> prefixGetter) {
    this(key, min, max, itemBuilder, prefixGetter, value -> "");
  }


  public ModifierSetting(String key, int min, int max, ItemBuilder itemBuilder, Function<Integer, String> prefixGetter, Function<Integer, String> suffixGetter) {
    super(key, itemBuilder);
    this.min = min;
    this.max = max;
    this.prefixGetter = prefixGetter;
    this.suffixGetter = suffixGetter;
  }

  @Override
  public String onClick(MenuClickInfo info, String value, int slotIndex) {

    int intValue = getIntValue(value);
    tempValue = intValue;

    ChallengeHelper.handleModifierClick(info, this);

    System.out.println(tempValue);
    intValue = tempValue;
    tempValue = 0;
    return String.valueOf(intValue);
  }

  @Override
  public int getValue() {
    return tempValue;
  }

  @Override
  public int getMinValue() {
    return min;
  }

  @Override
  public int getMaxValue() {
    return max;
  }

  @Override
  public void setValue(int value) {
    tempValue = value;
  }

  @Override
  public void playValueChangeTitle() { }

  @Override
  public ItemBuilder getSettingsItem(String value) {
    int intValue = getIntValue(value);
    String prefix = prefixGetter.apply(intValue);
    return DefaultItem.create(Material.STONE_BUTTON, "§7" + (prefix.equals("") ? "" : prefix + " ")
        + "§e" + value + " §7" + suffixGetter.apply(intValue)).amount(intValue);
  }

  public int getIntValue(String value) {

    try {
      return Integer.parseInt(value);
    } catch (Exception exception) {
      Challenges.getInstance().getLogger().severe("Something went wrong while parsing the "
          + "value of subsetting " + getKey() + " with value " + value);
      exception.printStackTrace();
    }

    return 0;
  }

}
