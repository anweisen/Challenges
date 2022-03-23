package net.codingarea.challenges.plugin.challenges.custom.settings.trigger;

import java.util.LinkedHashMap;
import java.util.function.Supplier;
import net.anweisen.utilities.bukkit.utils.misc.BukkitReflectionUtils;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.AbstractChallengeSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
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
public abstract class AbstractChallengeTrigger extends AbstractChallengeSetting implements
    IChallengeTrigger {

  public static final String ENTITY_TYPE = "entity_type",
        BLOCK = "block",
        ANY = "any",
        ITEM = "item",
        LIQUID = "liquid",
        TARGET_ENTITY = "target_entity";

  public AbstractChallengeTrigger(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengeTrigger(String name) {
    super(name);
  }

  public AbstractChallengeTrigger(String name, Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public final String getMessage() {
    return "item-custom-trigger-" + getMessageSuffix();
  }

  public static LinkedHashMap<String, ItemStack> getMenuItems() {
    LinkedHashMap<String, ItemStack> map = new LinkedHashMap<>();

    for (AbstractChallengeTrigger value : Challenges.getInstance().getCustomSettingsLoader().getTriggers().values()) {
      map.put(value.getName(), new ItemBuilder(value.getMaterial(), Message.forName(value.getMessage())).hideAttributes().build());
    }

    return map;
  }

  public static ChooseMultipleItemSubSettingBuilder createEntityTypeSettingsBuilder() {
    return SubSettingsBuilder.createChooseMultipleItem(AbstractChallengeTrigger.ENTITY_TYPE).fill(builder -> {
      builder.addSetting(AbstractChallengeTrigger.ANY, new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-trigger-entity_type-any")).build());
      builder.addSetting("PLAYER", new ItemBuilder(Material.PLAYER_HEAD, Message.forName("item-custom-trigger-entity_type-player")).build());
      for (EntityType type : EntityType.values()) {
        if (!type.isSpawnable() || !type.isAlive()) continue;
        try {
          Material spawnEgg = Material.valueOf(type.name() + "_SPAWN_EGG");
          builder.addSetting(type.name(), new ItemBuilder(spawnEgg,
              DefaultItem.getItemPrefix() + StringUtils.getEnumName(type)).build());
        } catch (Exception ex) {
          builder.addSetting(type.name(), new ItemBuilder(Material.STRUCTURE_VOID,
              DefaultItem.getItemPrefix() + StringUtils.getEnumName(type)));
        }
      }
    });
  }

  public static ChooseMultipleItemSubSettingBuilder createBlockSettingsBuilder() {
    return SubSettingsBuilder.createChooseMultipleItem(AbstractChallengeTrigger.BLOCK).fill(builder -> {
      builder.addSetting(AbstractChallengeTrigger.ANY, new ItemBuilder(Material.NETHER_STAR, Message.forName("item-custom-trigger-block-any")).build());
      for (Material material : Material.values()) {
        if (material.isBlock() && material.isItem() && !BukkitReflectionUtils.isAir(material)) {
          builder.addSetting(material.name(), new ItemBuilder(material, DefaultItem.getItemPrefix() + StringUtils.getEnumName(material)).build());
        }
      }
    });
  }

}
