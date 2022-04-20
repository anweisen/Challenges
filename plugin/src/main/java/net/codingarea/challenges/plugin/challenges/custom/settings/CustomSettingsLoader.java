package net.codingarea.challenges.plugin.challenges.custom.settings;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.ChallengeAction;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.*;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.ChallengeTrigger;
import net.codingarea.challenges.plugin.challenges.custom.settings.trigger.impl.*;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
public class CustomSettingsLoader {

  private final Map<String, ChallengeTrigger> triggers;
  private final Map<String, ChallengeAction> actions;

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
        new InLiquidTrigger("in_liquid"),
        new GetItemTrigger("get_item")
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
        new JumpAndRunAction("jnr"),
        new RandomHotBarAction("random_hotbar"),
        new ChangeWorldBorderAction("modify_border")
    );
  }

  public void registerTriggers(ChallengeTrigger... trigger) {
    for (ChallengeTrigger trigger1 : trigger) {
      triggers.put(trigger1.getName(), trigger1);
      Bukkit.getPluginManager().registerEvents(trigger1, Challenges.getInstance());
    }
  }

  public void registerActions(ChallengeAction... action) {
    for (ChallengeAction action1 : action) {
      actions.put(action1.getName(), action1);
    }
  }

  @Nullable
  public ChallengeAction getActionByName(String name) {
    return actions.get(name);
  }

  @Nullable
  public ChallengeTrigger getTriggerByName(String name) {
    return triggers.get(name);
  }

  public Map<String, ChallengeAction> getActions() {
    return actions;
  }

  public Map<String, ChallengeTrigger> getTriggers() {
    return triggers;
  }

}
