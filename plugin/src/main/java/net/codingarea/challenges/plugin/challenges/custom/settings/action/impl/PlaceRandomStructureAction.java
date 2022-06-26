package net.codingarea.challenges.plugin.challenges.custom.settings.action.impl;

import net.anweisen.utilities.bukkit.utils.misc.MinecraftVersion;
import net.anweisen.utilities.common.collection.IRandom;
import net.codingarea.challenges.plugin.challenges.custom.settings.action.EntityTargetAction;
import net.codingarea.challenges.plugin.challenges.type.helper.SubSettingsHelper;
import net.codingarea.challenges.plugin.management.challenges.annotations.RequireVersion;
import org.bukkit.*;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sehrschlechtYT | https://github.com/sehrschlechtYT
 * @since 2.2.2
 */
@RequireVersion(MinecraftVersion.V1_19)
public class PlaceRandomStructureAction extends EntityTargetAction {

    private List<NamespacedKey> structureKeys;
    private List<NamespacedKey> villageKeys;

    public PlaceRandomStructureAction(String name) {
        super(name, SubSettingsHelper.createEntityTargetSettingsBuilder(false, true));
    }

    @Override
    public Material getMaterial() {
        return Material.STRUCTURE_BLOCK;
    }

    @Override
    public void executeFor(Entity entity, Map<String, String[]> subActions) {
        if(structureKeys == null) {
            reloadStructureKeys();
        }
        Location location = entity.getLocation();
        String locationString = (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ();
        NamespacedKey structure = structureKeys.get(IRandom.singleton().nextInt(structureKeys.size()));
        if(structure == StructureType.VILLAGE.getKey()) {
            structure = villageKeys.get(IRandom.singleton().nextInt(villageKeys.size()));
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:place structure " + structure + " " + locationString);
    }

    private void reloadStructureKeys() {
        structureKeys = StructureType.getStructureTypes().values().stream()
                .map(StructureType::getKey)
                .collect(Collectors.toList());

        structureKeys.remove(StructureType.OCEAN_RUIN.getKey());
        structureKeys.add(NamespacedKey.minecraft("ocean_ruin_cold"));
        structureKeys.add(NamespacedKey.minecraft("ocean_ruin_warm"));

        villageKeys = new ArrayList<>();
        villageKeys.add(NamespacedKey.minecraft("village_desert"));
        villageKeys.add(NamespacedKey.minecraft("village_plains"));
        villageKeys.add(NamespacedKey.minecraft("village_savanna"));
        villageKeys.add(NamespacedKey.minecraft("village_snowy"));
        villageKeys.add(NamespacedKey.minecraft("village_taiga"));
    }

}
