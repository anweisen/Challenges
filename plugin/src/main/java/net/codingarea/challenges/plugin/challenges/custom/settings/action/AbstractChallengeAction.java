package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.LinkedHashMap;
import java.util.function.Supplier;
import net.anweisen.utilities.bukkit.utils.item.ItemBuilder.PotionBuilder;
import net.anweisen.utilities.common.collection.IRandom;
import net.anweisen.utilities.common.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.AbstractChallengeSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.builder.ChooseItemSubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengeAction extends AbstractChallengeSetting implements
    IChallengeAction {

  protected static IRandom random = IRandom.create();

  public AbstractChallengeAction(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengeAction(String name) {
    super(name);
  }

  public AbstractChallengeAction(String name,
      Supplier<SubSettingsBuilder> builderSupplier) {
    super(name, builderSupplier);
  }

  @Override
  public final String getMessage() {
    return "item-custom-action-" + getMessageSuffix();
  }

  public static LinkedHashMap<String, ItemStack> getMenuItems() {
    LinkedHashMap<String, ItemStack> map = new LinkedHashMap<>();

    for (AbstractChallengeAction value : Challenges.getInstance().getCustomSettingsLoader().getActions().values()) {
      map.put(value.getName(), new ItemBuilder(value.getMaterial(), Message.forName(value.getMessage())).hideAttributes().build());
    }

    return map;
  }


  public static SubSettingsBuilder createEntityTargetSettingsBuilder(boolean everyMob) {
    return createEntityTargetSettingsBuilder(everyMob, false);
  }

  public static SubSettingsBuilder createEntityTargetSettingsBuilder(boolean everyMob, boolean onlyPlayer) {
    ChooseItemSubSettingsBuilder builder = SubSettingsBuilder.createChooseItem(
        AbstractChallengeCondition.TARGET_ENTITY);
    if (!onlyPlayer) {
      builder.addSetting("current", new ItemBuilder(Material.DRAGON_HEAD,
              Message.forName("item-custom-action-target-current")).build());
    }

    builder.addSetting("current_player", new ItemBuilder(Material.PLAYER_HEAD,
        Message.forName("item-custom-action-target-current_player")).build());
    builder.addSetting("random_player", new ItemBuilder(Material.ZOMBIE_HEAD,
        Message.forName("item-custom-action-target-random_player")).build());
    builder.addSetting("every_player", new ItemBuilder(Material.PLAYER_HEAD,
        Message.forName("item-custom-action-target-every_player")).build());

    if (everyMob && !onlyPlayer) {
      builder.addSetting("every_mob", new ItemBuilder(Material.WITHER_SKELETON_SKULL,
          Message.forName("item-custom-action-target-every_mob")).build());
      builder.addSetting("every_mob_except_current", new ItemBuilder(Material.SKELETON_SKULL,
          Message.forName("item-custom-action-target-every_mob_except_current")).build());
      builder.addSetting("every_mob_except_players", new ItemBuilder(Material.SKELETON_SKULL,
          Message.forName("item-custom-action-target-every_mob_except_players")).build());
    }
    return builder;
  }

  public static SubSettingsBuilder createPotionSettingsBuilder(boolean potionType,
      boolean potionTime) {

    SubSettingsBuilder potionSettings = SubSettingsBuilder.createValueItem().fill(builder -> {

      if (potionTime) {
        builder.addModifierSetting("length", new ItemBuilder(Material.CLOCK,
                Message.forName("item-random-effect-length-challenge")),
            30, 1, 60,
            value -> "",
            value -> Message.forName(value == 1 ? "second" : "seconds").asString());
      }
      builder.addModifierSetting("amplifier", new ItemBuilder(Material.STONE_SWORD,
              Message.forName("item-random-effect-amplifier-challenge")),
          3, 1, 8,
          value -> Message.forName("amplifier").asString());
    });

    if (potionType) {
      potionSettings = potionSettings.createChooseItemChild("potion_type").fill(builder -> {
        for (PotionEffectType effectType : PotionEffectType.values()) {
          builder.addSetting(effectType.getName(), new PotionBuilder(Material.POTION,
              DefaultItem.getItemPrefix() + StringUtils.getEnumName(effectType.getName()))
              .addEffect(effectType.createEffect(1, 0))
              .color(effectType.getColor()).build());

        }
      });
    }


    return potionSettings;
  }

}
