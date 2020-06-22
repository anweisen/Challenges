package net.codingarea.challengesplugin.challenges.settings;

import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Modifier;
import net.codingarea.challengesplugin.manager.ChallengeManager;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.BackpackUtil;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-29-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class BackpackModifier extends Modifier implements Listener, CommandExecutor {

    private YamlConfig config;

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.CHEST, ItemTranslation.BACKPACK).getItem();
    }

    public BackpackModifier(int size, ChallengeManager manager) {
        this.menu = MenuType.SETTINGS;
        this.maxValue = 3;
        this.config = Challenges.getInstance().getConfigManager().getBackpackConfig();

        this.manager = manager;
        this.size = size;

        if (this.size <= 0) this.size = 1;
        if (this.size > 6) this.size = 6;

        this.size *= 9;

        YamlConfig yamlConfig = manager.getPlugin().getConfigManager().getBackpackConfig();
        FileConfiguration config = yamlConfig.getConfig();
        ItemStack[] content = BackpackUtil.getContent(config, "team", this.size);

        if (content == null) {
            BackpackUtil.saveArrayToConfig(config, "team", Bukkit.createInventory(null, this.size, "§5Team-Backpack").getContents());
        }
        this.teamBackpack = Bukkit.createInventory(null, this.size, "§5Team-Backpack");
        try {
            this.teamBackpack.setContents(BackpackUtil.getContent(config, "team", this.size));
            yamlConfig.save();
        } catch (Exception ignored) { }

    }

    @Override
    public void onMenuClick(ChallengeEditEvent event) {  }

    @Override
    public ItemStack getActivationItem() {
        if (this.value == 1) {
            return Challenges.getInstance().getItemManager().getNotActivatedItem();
        } else if (this.value == 2) {
            return new ItemBuilder(Material.PLAYER_HEAD, "§6Player").getItem();
        } else {
            return new ItemBuilder(Material.ENDER_CHEST, "§5Team").getItem();
        }
    }

    @EventHandler
    public void handle(InventoryCloseEvent event) {

        Bukkit.getScheduler().runTaskAsynchronously(Challenges.getInstance(), () -> {
            if (event.getView().getTitle().equals("§5Team-Backpack")) {
                BackpackUtil.saveArrayToConfig(config.getConfig(), "team", event.getInventory().getContents());
                AnimationSound.PLOP_SOUND.play((Player) event.getPlayer());
                config.save();
            } else if (event.getView().getTitle().equals("§6Backpack")) {
                BackpackUtil.saveArrayToConfig(config.getConfig(), "players." + event.getPlayer().getUniqueId().toString(), event.getInventory().getContents());
                AnimationSound.PLOP_SOUND.play((Player) event.getPlayer());
                config.save();
            }
        });

    }

    private ChallengeManager manager;
    private int size;
    Inventory teamBackpack;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (value == 1 || !Challenges.timerIsStarted()) {
            player.sendMessage(Challenges.getInstance().getStringManager().BACKPACK_PREFIX + Translation.BACKBACKS_NOT_ACTIVE.get());
            AnimationSound.OFF_SOUND.play(player);
            return true;
        }

        YamlConfig yamlConfig = manager.getPlugin().getConfigManager().getBackpackConfig();
        FileConfiguration config = yamlConfig.getConfig();

        if (value == 2) {

            String uuid = player.getUniqueId().toString();
            if (BackpackUtil.getContent(config, "players." + uuid, this.size) == null) {
                BackpackUtil.saveArrayToConfig(config, "team", Bukkit.createInventory(null, size, "§6Backpack").getContents());
                yamlConfig.save();
            }

            Inventory inventory = Bukkit.createInventory(null, this.size, "§6Backpack");

            try {
                inventory.setContents(BackpackUtil.getContent(config, "players." + uuid, this.size));
            } catch (NullPointerException ignored) { }

            player.openInventory(inventory);
            player.sendMessage(Challenges.getInstance().getStringManager().BACKPACK_PREFIX + Translation.BACKBACKS_OPEN.get().replace("%backpack%", "§6Backpack"));

        } else if (value == 3) {
            player.openInventory(teamBackpack);
            player.sendMessage(Challenges.getInstance().getStringManager().BACKPACK_PREFIX + Translation.BACKBACKS_OPEN.get().replace("%backpack%", "§5Team-Backpack"));
        }

        AnimationSound.OPEN_SOUND.play(player);

        return true;
    }

}
