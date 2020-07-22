package net.codingarea.challengesplugin.challenges.challenges.randomizer;

import net.codingarea.challengesplugin.challengetypes.Setting;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.RandomizerUtil;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author anweisen & Dominik
 * Challenges developed on 06-08-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class EntitySpawnRandomizerChallenge extends Setting implements Listener {

    private HashMap<EntityType, EntityType> entities;

    public EntitySpawnRandomizerChallenge() {
        super(MenuType.CHALLENGES);
        load();
    }

    @Override
    public String getChallengeName() {
        return "mobrandomizer";
    }

    private void load() {
        entities = new HashMap<>();
        List<EntityType> types = RandomizerUtil.getRandomizerEntities();
        Collections.shuffle(types);
        Random random = new Random();

        for (EntityType entity : RandomizerUtil.getRandomizerEntities()) {
            entities.put(entity, types.remove(random.nextInt(types.size())));

        }
        spawned = false;
    }

    boolean spawned;

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!this.enabled) return;
        if (spawned) return;
        if (!entities.containsKey(event.getEntityType())) return;

        event.setCancelled(true);
        spawned = true;
        event.getEntity().getWorld().spawnEntity(event.getLocation(), entities.get(event.getEntityType()));
        spawned = false;

    }

    @Override
    public void onEnable(ChallengeEditEvent event) { }

    @Override
    public void onDisable(ChallengeEditEvent event) { }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.COMMAND_BLOCK_MINECART, ItemTranslation.MOB_RANDOMIZER).build();
    }
}