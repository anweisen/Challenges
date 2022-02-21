package net.codingarea.challenges.plugin.challenges.custom.settings.condition;

import java.util.LinkedHashMap;
import java.util.function.Supplier;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.AbstractChallengeSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseMultipleItemSubSettingBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengeCondition extends AbstractChallengeSetting implements
    IChallengeCondition {

  public AbstractChallengeCondition(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengeCondition(String name) {
    super(name);
  }

  public AbstractChallengeCondition(String name,
      Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public final String getMessage() {
    return "item-custom-condition-" + getMessageSuffix();
  }

  public static LinkedHashMap<String, ItemStack> getMenuItems() {
    LinkedHashMap<String, ItemStack> map = new LinkedHashMap<>();

    for (AbstractChallengeCondition value : Challenges.getInstance().getCustomSettingsLoader().getConditions().values()) {
      map.put(value.getName(), new ItemBuilder(value.getMaterial(), Message.forName(value.getMessage())).hideAttributes().build());
    }

    return map;
  }

  public static ChooseItemSubSettingsBuilder createEntityTypeSettingsBuilder() {
    return SubSettingsBuilder.createChooseItem("entity_type").fill(builder -> {
      builder.addSetting("any", new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-condition-entity_type-any")).build());
      builder.addSetting("PLAYER", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-condition-entity_type-player")).build());
      for (EntityType type : EntityType.values()) {
        try {
          Material spawnEgg = Material.valueOf(type.name() + "_SPAWN_EGG");
          builder.addSetting(type.name(), new ItemBuilder(spawnEgg, DefaultItem.getItemPrefix() + StringUtils
              .getEnumName(type)).build());
        } catch (Exception ex) { }
      }
    });
  }

  public static ChooseMultipleItemSubSettingBuilder createBlockSettingsBuilder() {
    return SubSettingsBuilder.createChooseMultipleItem("block").fill(builder -> {
      builder.addSetting("any", new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-condition-block-any")).build());
      for (Material material : Material.values()) {
        if (material.isBlock() && material.isItem() && !BukkitReflectionUtils.isAir(material)) {
          builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils.getEnumName(material)).build());
        }
      }
    });
  }

}
