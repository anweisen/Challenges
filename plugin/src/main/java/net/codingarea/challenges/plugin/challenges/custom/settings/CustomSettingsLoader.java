package net.codingarea.challenges.plugin.challenges.custom.settings;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.AbstractChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.AddPermanentEffectAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.BoostEntityInAirAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.CancelEventAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.ClearInventoryAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.DamageEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.DropRandomItemAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.FreezeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.HealEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.HungerPlayerAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.InvertHealthAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.JumpAndRunAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.KillEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.ModifyMaxHealthAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.PotionEffectAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.RandomItemAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.RandomMobAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.RandomPotionEffectAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.SwapRandomItemAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.UncraftInventoryAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.WaterMLGAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.AbstractChallengeCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.AdvancementCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.CraftItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.DropItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.GainXPCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.InLiquidCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.LevelUpCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.MoveBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.BreakBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.ConsumeItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDamageByPlayerCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDamageCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.EntityDeathCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.HungerCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.IntervallCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.MoveCameraCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.MoveDownCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.MoveUpCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PickupItemCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlaceBlockCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlayerJumpCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.PlayerSneakCondition;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.StandsNotOnSpecificBlock;
import net.codingarea.challenges.plugin.challenges.custom.settings.condition.impl.StandsOnSpecificBlock;
import org.bukkit.Bukkit;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomSettingsLoader {

  private final Map<String, AbstractChallengeCondition> conditions;
  private final Map<String, AbstractChallengeAction> actions;

  public CustomSettingsLoader() {
    actions = new LinkedHashMap<>();
    conditions = new LinkedHashMap<>();
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
        new MoveBlockCondition("move_block"),
        new BreakBlockCondition("block_break"),
        new PlaceBlockCondition("block_place"),
        new EntityDeathCondition("death"),
        new EntityDamageCondition("damage"),
        new EntityDamageByPlayerCondition("damage_by_player"),
        new ConsumeItemCondition("consume_item"),
        new PickupItemCondition("pickup_item"),
        new DropItemCondition("drop_item"),
        new AdvancementCondition("advancement"),
        new HungerCondition("hunger"),
        new MoveUpCondition("move_up"),
        new MoveDownCondition("move_down"),
        new MoveCameraCondition("move_camera"),
        new StandsOnSpecificBlock("stands_on_specific_block"),
        new StandsNotOnSpecificBlock("stands_not_on_specific_block"),
        new GainXPCondition("gain_xp"),
        new LevelUpCondition("level_up"),
        new CraftItemCondition("item_craft"),
        new InLiquidCondition("in_liquid")
    );
  }

  private void loadActions() {
    registerActions(
        new CancelEventAction("cancel"),
        new KillEntityAction("kill"),
        new DamageEntityAction("damage"),
        new HealEntityAction("heal"),
        new ModifyMaxHealthAction("max_health"),
        new HungerPlayerAction("hunger"),
        new RandomMobAction("random_mob"),
        new RandomItemAction("random_item"),
        new UncraftInventoryAction("uncraft_inventory"),
        new BoostEntityInAirAction("boost_in_air"),
        new PotionEffectAction("potion_effect"),
        new AddPermanentEffectAction("permanent_effect"),
        new RandomPotionEffectAction("random_effect"),
        new ClearInventoryAction("clear_inventory"),
        new DropRandomItemAction("drop_random_item"),
        new DropRandomItemAction("remove_random_item"),
        new SwapRandomItemAction("swap_random_item"),
        new FreezeAction("freeze"),
        new InvertHealthAction("invert_health"),
        new WaterMLGAction("water_mlg"),
        new JumpAndRunAction("jnr")
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

  public Map<String, AbstractChallengeAction> getActions() {
    return actions;
  }

  public Map<String, AbstractChallengeCondition> getConditions() {
    return conditions;
  }

}
