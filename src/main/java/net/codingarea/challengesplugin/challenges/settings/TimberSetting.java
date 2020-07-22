package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.ItemManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-07-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */
public class TimberSetting extends Modifier implements Listener {

    public TimberSetting() {
        super(MenuType.SETTINGS, 3);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.DIAMOND_AXE, ItemTranslation.TIMBER).hideAttributes().build();
    }

    @Override
    public void onMenuClick(ChallengeEditEvent event) {}

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (value == 1 || !Challenges.timerIsStarted()) return;
        if (!isLog(event.getBlock().getType())) return;

        boolean broken = onTimber(event.getPlayer().getItemInHand(), event.getBlock(), LogType.getType(event.getBlock().getType()), false, 0);
        if (broken) event.getPlayer().getInventory().removeItem(event.getPlayer().getItemInHand());
        event.getPlayer().updateInventory();
    }

    private boolean isLog(Material material) {
        return material.name().toLowerCase().contains("log");
    }
    private boolean isLeaves(Material material) {
        return material.name().toLowerCase().contains("leaves");
    }

    /**
     * @return returns if the axe used should be broken now
     */
    private boolean onTimber(ItemStack axe, Block block, LogType type, boolean byLeaves, int i) {

        // This will cancel the timber event after x blocks destroyed to prevent lags
        if (i >= 100) return false;
        i++;

        boolean broken = false;
        List<Block> blocksAround = getBlocksAroundBlock(block.getLocation());
        for (Block currentBlock : blocksAround) {
            boolean isLog = isLog(currentBlock.getType());
            if (!(isLog || (isLeaves(currentBlock.getType()) && value == 3)) || LogType.getType(currentBlock.getType()) != type) continue;
            if (byLeaves && isLog) continue;
            currentBlock.breakNaturally();

            // This is checking if the axe has enough durability to break the block and will subtract one durability, otherwise the axe will be destroyed
            if (!broken && isLog && axe != null && axe.getType().getMaxDurability() > 25) {
                short durability = axe.getDurability();
                if ((durability + 1) >= axe.getType().getMaxDurability()) {
                    broken = true;
                } else {
                    axe.setDurability((short) (axe.getDurability() + 1));
                }
            }

            onTimber(axe, currentBlock, type, !isLog, i);
        }

        return broken;

    }

    /**
     * @param location middle
     * @return returns the block above, under, in the front, behind, to the left and to the right of the middle block
     */
    private List<Block> getBlocksAroundBlock(Location location) {
        List<Block> list = new ArrayList<>();
        list.add(location.clone().add(0, 1, 0).getBlock());
        list.add(location.clone().add(0, -1, 0).getBlock());
        list.add(location.clone().add(1, 0, 0).getBlock());
        list.add(location.clone().add(-1, 0, 0).getBlock());
        list.add(location.clone().add(0, 0, 1).getBlock());
        list.add(location.clone().add(0, 0, -1).getBlock());
        return list;
    }

    @Override
    public ItemStack getActivationItem() {
        if (value == 1) {
            return ItemManager.getNotActivatedItem();
        } else if (value == 2) {
            return new ItemBuilder(Material.OAK_LOG, "§6Logs").build();
        } else {
            return new ItemBuilder(Material.OAK_LEAVES, "§2Logs & Leaves").build();
        }
    }

    public enum LogType {

        DARK_OAK,
        BIRCH,
        SPRUCE,
        OAK,
        ACACIA,
        JUNGLE;

        public static LogType getType(Material material) {

            if (material == null) return null;

            String name = material.name().toLowerCase();

            for (LogType currentLogType : values()) {
                if (name.contains(currentLogType.name().toLowerCase())) return currentLogType;
            }

            return null;

        }

    }

}
