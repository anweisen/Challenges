package net.codingarea.challenges.plugin.challenges.implementation;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.Menu;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.spigot.events.PlayerInventoryClickEvent;
import net.codingarea.challenges.plugin.spigot.events.PlayerPickupItemEvent;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BlockMaterialSetting extends Setting {

	private final String name;
	private final ItemBuilder preset;
	private final Object[] replacements;
	private final Material[] materials;

	public BlockMaterialSetting(@Nonnull String name, @Nonnull ItemBuilder preset, @Nonnull Object[] replacements, @Nonnull Material... materials) {
		super(MenuType.ITEMS_BLOCKS);
		this.name = name;
		this.preset = preset;
		this.replacements = replacements;
		this.materials = materials;
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return preset.clone().applyFormat(Message.forName(name).asItemDescription(replacements));
	}

	private boolean blockMaterial(@Nonnull Material material) {
		return Arrays.asList(materials).contains(material);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (isEnabled()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (ChallengeAPI.isWorldInUse()) return;
		if (!blockMaterial(event.getMaterial())) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInventoryClickEvent event) {
		if (isEnabled()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (ChallengeAPI.isWorldInUse()) return;
		if (event.getCurrentItem() == null) return;
		if (!blockMaterial(event.getCurrentItem().getType())) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerPickupItemEvent event) {
		if (isEnabled()) return;
		if (ignorePlayer(event.getPlayer())) return;
		if (ChallengeAPI.isWorldInUse()) return;
		if (!blockMaterial(event.getItem().getItemStack().getType())) return;
		event.setCancelled(true);
	}

}