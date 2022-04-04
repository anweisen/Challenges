package net.codingarea.challenges.plugin.challenges.custom.settings.action;

import java.util.LinkedHashMap;
import java.util.function.Supplier;
import net.anweisen.utilities.common.collection.IRandom;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.AbstractChallengeSetting;
import net.codingarea.challenges.plugin.challenges.custom.settings.sub.SubSettingsBuilder;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public abstract class AbstractChallengeAction extends AbstractChallengeSetting implements
    IChallengeAction {

  protected static final IRandom random = IRandom.create();

  public AbstractChallengeAction(String name,
      SubSettingsBuilder subSettingsBuilder) {
    super(name, subSettingsBuilder);
  }

  public AbstractChallengeAction(String name) {
    super(name);
  }

  public AbstractChallengeAction(String name, Supplier<SubSettingsBuilder> builderSupplier) {
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

}
