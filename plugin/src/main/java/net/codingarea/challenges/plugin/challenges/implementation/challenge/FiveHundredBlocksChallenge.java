package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.anweisen.utilities.common.annotations.Since;
import net.anweisen.utilities.common.collection.IRandom;
import net.anweisen.utilities.common.config.Document;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.impl.RandomItemAction;
import net.codingarea.challenges.plugin.challenges.type.abstraction.Setting;
import net.codingarea.challenges.plugin.content.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.misc.BlockUtils;
import net.codingarea.challenges.plugin.utils.misc.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.1.0
 */
@Since("2.1.0")
public class FiveHundredBlocksChallenge extends Setting {

  private static final int blocksToWalk = 500;

  private final Map<UUID, Integer> blocksWalked = new HashMap<>();

  public FiveHundredBlocksChallenge() {
    super(MenuType.CHALLENGES);
  }

  @Override
  protected void onEnable() {
    bossbar.setContent((bar, player) -> {
      int walked = blocksWalked.getOrDefault(player.getUniqueId(), 0);
      bar.setTitle(Message.forName("bossbar-five-hundred-blocks").asString(walked, blocksToWalk));
    });
    bossbar.show();
  }

  @Override
  protected void onDisable() {
    bossbar.hide();
  }

  @NotNull
  @Override
  public ItemBuilder createDisplayItem() {
    return new ItemBuilder(Material.OAK_SIGN, Message.forName("item-five-hundred-blocks-challenges"));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onMove(PlayerMoveEvent event) {
    if (!shouldExecuteEffect()) return;
    Player player = event.getPlayer();
    if (ignorePlayer(player)) return;
    if (BlockUtils.isSameBlockIgnoreHeight(event.getFrom(), event.getTo())) return;

    if (updateOrReset(player)) {
      Material material = IRandom.threadLocal().choose(
          RandomItemAction.items);
      InventoryUtils.giveItem(player.getInventory(), player.getLocation(), new ItemStack(material, 64));
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onBlockBreak(BlockBreakEvent event) {
    if (!shouldExecuteEffect()) return;
    if (ignorePlayer(event.getPlayer())) return;
    event.setDropItems(false);
    event.setExpToDrop(0);
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityDeath(EntityDeathEvent event) {
    if (!shouldExecuteEffect()) return;
    event.getDrops().clear();
    event.setDroppedExp(0);
  }

  /**
   * @return if 500 blocks were reached
   */
  private boolean updateOrReset(@Nonnull Player player) {
    UUID uuid = player.getUniqueId();

    int blocksWalked = this.blocksWalked.getOrDefault(uuid, 0);

    blocksWalked++;

    boolean reached = false;
    if (blocksWalked >= blocksToWalk) {
      blocksWalked = 0;
      reached = true;
    }

    this.blocksWalked.put(uuid, blocksWalked);

    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
      bossbar.update(player);
    });
    return reached;
  }

  @Override
  public void loadGameState(@NotNull Document document) {
    blocksWalked.clear();
    for (String key : document.keys()) {
      try {
        UUID uuid = UUID.fromString(key);
        int blocks = document.getInt(key);
        blocksWalked.put(uuid, blocks);
      } catch (IllegalArgumentException exception) {
        plugin.getLogger().severe("Error while loading 500 Blocks Challenge, "
            + "key '" + key + "' is not a valid uuid");
        exception.printStackTrace();
      }
    }
  }

  @Override
  public void writeGameState(@NotNull Document document) {
    blocksWalked.forEach((uuid, integer) -> {
      document.set(uuid.toString(), integer);
    });
  }

}
