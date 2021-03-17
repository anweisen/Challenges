package net.codingarea.challenges.plugin.challenges.implementation.setting;

import net.codingarea.challenges.plugin.ChallengeAPI;
import net.codingarea.challenges.plugin.challenges.type.Setting;
import net.codingarea.challenges.plugin.lang.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.utils.animation.SoundSample;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.0
 */
public class SoupSetting extends Setting {

	public SoupSetting() {
		super(MenuType.SETTINGS);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.MUSHROOM_STEW, Message.forName("item-soup-setting"));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!isEnabled() || ChallengeAPI.isPaused()) return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getItem() == null) return;
		if (event.getItem().getType() != Material.MUSHROOM_STEW) return;

		Player player = event.getPlayer();
		if (player.getHealth() == player.getMaxHealth()) return;

		player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
		player.setItemInHand(new ItemBuilder(Material.BOWL).build());
		player.updateInventory();
		SoundSample.EAT.play(player);

	}

}
