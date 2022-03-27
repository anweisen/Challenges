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
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.ExecuteCommandAction;
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
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.SpawnEntityAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.SwapRandomItemAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.UncraftInventoryAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.WaterMLGAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.WinChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.AbstractChallengeTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.AdvancementTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.CraftItemTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.DropItemTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.GainXPTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.InLiquidTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.LevelUpTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.MoveBlockTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.BreakBlockTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.ConsumeItemTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.EntityDamageByPlayerTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.EntityDamageTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.EntityDeathTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.HungerTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.IntervallTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.MoveCameraTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.MoveDownTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.MoveUpTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.PickupItemTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.PlaceBlockTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.PlayerJumpTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.PlayerSneakTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.StandsNotOnSpecificBlockTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.StandsOnSpecificBlockTrigger;
import org.bukkit.Bukkit;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomSettingsLoader {

  private final Map<String, AbstractChallengeTrigger> triggers;
  private final Map<String, AbstractChallengeAction> actions;

  public CustomSettingsLoader() {
    actions = new LinkedHashMap<>();
    triggers = new LinkedHashMap<>();
  }

  public void enable() {
    loadTrigger();
    loadActions();
  }

  private void loadTrigger() {
    registerTriggers(
        new IntervallTrigger("intervall"),
        new PlayerJumpTrigger("jump"),
        new PlayerSneakTrigger("sneak"),
        new MoveBlockTrigger("move_block"),
        new BreakBlockTrigger("block_break"),
        new PlaceBlockTrigger("block_place"),
        new EntityDeathTrigger("death"),
        new EntityDamageTrigger("damage"),
        new EntityDamageByPlayerTrigger("damage_by_player"),
        new ConsumeItemTrigger("consume_item"),
        new PickupItemTrigger("pickup_item"),
        new DropItemTrigger("drop_item"),
        new AdvancementTrigger("advancement"),
        new HungerTrigger("hunger"),
        new MoveUpTrigger("move_up"),
        new MoveDownTrigger("move_down"),
        new MoveCameraTrigger("move_camera"),
        new StandsOnSpecificBlockTrigger("stands_on_specific_block"),
        new StandsNotOnSpecificBlockTrigger("stands_not_on_specific_block"),
        new GainXPTrigger("gain_xp"),
        new LevelUpTrigger("level_up"),
        new CraftItemTrigger("item_craft"),
        new InLiquidTrigger("in_liquid")
    );
  }

  private void loadActions() {
    registerActions(
        new CancelEventAction("cancel"),
        new WinChallengeAction("win"),
        new ExecuteCommandAction("command"),
        new KillEntityAction("kill"),
        new DamageEntityAction("damage"),
        new HealEntityAction("heal"),
        new ModifyMaxHealthAction("max_health"),
        new HungerPlayerAction("hunger"),
        new SpawnEntityAction("spawn_entity"),
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

  public void registerTriggers(AbstractChallengeTrigger... trigger) {
    for (AbstractChallengeTrigger trigger1 : trigger) {
      triggers.put(trigger1.getName(), trigger1);
      Bukkit.getPluginManager().registerEvents(trigger1, Challenges.getInstance());
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
  public AbstractChallengeTrigger getTriggerByName(String name) {
    return triggers.get(name);
  }

  public Map<String, AbstractChallengeAction> getActions() {
    return actions;
  }

  public Map<String, AbstractChallengeTrigger> getTriggers() {
    return triggers;
  }

}
