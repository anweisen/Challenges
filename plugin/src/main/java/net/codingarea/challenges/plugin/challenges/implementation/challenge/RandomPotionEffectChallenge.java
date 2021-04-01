package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.codingarea.challenges.plugin.challenges.type.MenuSetting;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.ScheduledTask;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class RandomPotionEffectChallenge extends MenuSetting {

	/**
	 * Effekt Infos:
	 *  - Länge Auswählbar in Sekunden
	 *  - Stärke: Auswöhlbar
	 *
	 * Blacklist:
	 *  - Playerglow
	 *  - Instant Effekte
	 *
	 */

	int currentTime = 0;

	public RandomPotionEffectChallenge() {
		super(MenuType.CHALLENGES, "Random Effect");
		registerSetting("time", new NumberSubSetting(
				() -> new ItemBuilder(Material.CLOCK, Message.forName("item-random-effect-time-challenge")),
				value -> null,
				value -> "§e" + value + " §7" +Message.forName(value == 1 ? "second" : "seconds").asString(),
				1,
				60,
				30)
		);
		registerSetting("length", new NumberSubSetting(
				() -> new ItemBuilder(Material.ARROW, Message.forName("item-random-effect-length-challenge")),
				value -> null,
				value -> "§e" + value + " §7" + Message.forName(value == 1 ? "second" : "seconds").asString(),
				1,
				20,
				10)
		);
		registerSetting("strength", new NumberSubSetting(
				() -> new ItemBuilder(Material.STONE_SWORD, Message.forName("item-random-effect-strength-challenge")),
				value -> null,
				value -> "§7" + Message.forName("strength") + " §e" + value,
				1,
				8,
				3)
		);

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BREWING_STAND, Message.forName("item-random-effect-challenge"));
	}

	@ScheduledTask(ticks = 20)
	public void onSecond() {
		currentTime++;

		if (currentTime > getSetting("time").getAsInt()) {
			currentTime = 0;
			Bukkit.getScheduler().runTask(plugin, this::applyRandomEffect);
		}

	}

	private void applyRandomEffect() {
		applyEffect(getRandomEffect());
	}

	private PotionEffectType getRandomEffect() {
		ArrayList<PotionEffectType> list = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
		list.remove(PotionEffectType.GLOWING);
		list.remove(PotionEffectType.HEAL);
		list.remove(PotionEffectType.HARM);
		return list.get(new Random().nextInt(list.size()));
	}

	private void applyEffect(@Nonnull PotionEffectType effectType) {
		Bukkit.getOnlinePlayers().forEach(player -> applyEffect(player, effectType));
	}

	private void applyEffect(@Nonnull Player player, @Nonnull PotionEffectType effectType) {
		PotionEffect potionEffect = new PotionEffect(effectType, (getSetting("length").getAsInt() + 1) * 20, getSetting("strength").getAsInt() - 1);
		player.addPotionEffect(potionEffect);
	}

}