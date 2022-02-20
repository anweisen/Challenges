package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.HashMap;
import javax.annotation.Nullable;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.BoostEntityInAirAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.DamageEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.KillEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.PotionEffectAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.RandomItemAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.UncraftInventoryAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.BlockMoveCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.BreakBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.ConsumeItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDamageByPlayerCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDamageCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDeathCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.IntervallCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlaceBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlayerJumpCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlayerSneakCondition;
import org.bukkit.Bukkit;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomSettingsLoader {

  private final HashMap<String, AbstractChallengeCondition> conditions;
  private final HashMap<String, AbstractChallengeAction> actions;

  public CustomSettingsLoader() {
    actions = new HashMap<>();
    conditions = new HashMap<>();
  }

  public void enable() {
    loadConditions();
    loadActions();
  }

  private void loadConditions() {
    registerConditions(
        new IntervallCondition("intervall"),
        new PlayerJumpCondition("jump"),
        new PlayerSneakCondition("sneak"),
        new BlockMoveCondition("move_block"),
        new BreakBlockCondition("block_break"),
        new PlaceBlockCondition("block_place"),
        new EntityDeathCondition("death"),
        new EntityDamageCondition("damage"),
        new EntityDamageByPlayerCondition("damage_by_player"),
        new ConsumeItemCondition("consume_item")
    );
  }

  private void loadActions() {
    registerActions(
        new KillEntityAction("kill"),
        new DamageEntityAction("damage"),
        new RandomItemAction("random_item"),
        new UncraftInventoryAction("uncraft_inventory"),
        new BoostEntityInAirAction("boost_in_air"),
        new PotionEffectAction("potion_effect")
    );
  }

  public void registerConditions(AbstractChallengeCondition... condition) {
    for (AbstractChallengeCondition condition1 : condition) {
      conditions.put(condition1.getName(), condition1);
      Bukkit.getPluginManager().registerEvents(condition1, Challenges.getInstance());
    }
  }

  public void registerActions(AbstractChallengeAction... action) {
    for (AbstractChallengeAction action1 : action) {
      actions.put(action1.getName(), action1);
    }
  }

  @Nullable
  public AbstractChallengeAction getActionByName(String name) {
    return actions.get(name);
  }

  @Nullable
  public AbstractChallengeCondition getConditionByName(String name) {
    return conditions.get(name);
  }

  public HashMap<String, AbstractChallengeAction> getActions() {
    return actions;
  }

  public HashMap<String, AbstractChallengeCondition> getConditions() {
    return conditions;
  }

}
