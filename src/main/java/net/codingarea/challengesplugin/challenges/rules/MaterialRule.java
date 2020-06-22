package net.codingarea.challengesplugin.challenges.rules;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-2-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class MaterialRule extends Setting implements Listener {

    private Material[] material;
    private String name;

    private Material itemMaterial;

    public MaterialRule(Material material, String prefix) {
        this.material = new Material[1];
        this.material[0] = material;
        this.itemMaterial = material;
        this.menu = MenuType.BLOCK_ITEMS;
        this.name = prefix + Utils.getEnumName(material.name());
        this.enabled = true;
    }

    public MaterialRule(Material itemMaterial, String name, Material... material) {
        this.material = material;
        this.itemMaterial = itemMaterial;
        this.menu = MenuType.BLOCK_ITEMS;
        this.enabled = true;
        this.name = name;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(itemMaterial, ItemTranslation.MATERIAL_RULE, name).hideAttributes().getItem();
    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (enabled || !Challenges.timerIsStarted()) return;
        if (event.getCurrentItem() == null) return;
        if (Arrays.asList(material).contains(event.getCurrentItem().getType())) event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (enabled || !Challenges.timerIsStarted()) return;
        if (Arrays.asList(material).contains(event.getItem().getItemStack().getType())) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (enabled || !Challenges.timerIsStarted()) return;
        if (event.getItem() != null && Arrays.asList(material).contains(event.getMaterial())) event.setCancelled(true);
        if (event.getClickedBlock() != null && Arrays.asList(material).contains(event.getClickedBlock().getType())) event.setCancelled(true);
    }

}