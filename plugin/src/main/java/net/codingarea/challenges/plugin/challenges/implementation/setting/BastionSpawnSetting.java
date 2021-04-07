package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.anweisen.utilities.bukkit.utils.MinecraftVersion;
import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.NetherPortalSpawnSetting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.challenges.annotations.RequireVersion;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.StructureType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
@Since("2.0")
@RequireVersion(MinecraftVersion.V1_16)
public class BastionSpawnSetting extends NetherPortalSpawnSetting {

	public BastionSpawnSetting() {
		super(MenuType.SETTINGS, StructureType.BASTION_REMNANT, "unable-to-find-bastion",
			  Arrays.stream(Material.values()).filter(material -> material.name().contains("BASALT")).collect(Collectors.toList()));
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.POLISHED_BLACKSTONE_BRICKS, Message.forName("item-bastion-spawn-setting"));
	}

}
