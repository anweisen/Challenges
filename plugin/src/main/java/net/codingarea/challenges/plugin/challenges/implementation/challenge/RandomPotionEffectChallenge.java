package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.codingarea.challenges.plugin.challenges.type.MenuSetting;
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
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class RandomPotionEffectChallenge extends MenuSetting {

	private final Random random = new Random();
	int currentTime = 0;

	public RandomPotionEffectChallenge() {
		super(MenuType.CHALLENGES, "Random Effect");
		registerSetting("time", new NumberSubSetting(
				() -> new ItemBuilder(Material.CLOCK, Message.forName("item-random-effect-time-challenge")),
				value -> null,
				value -> "§e" + value + " §7" + Message.forName(value == 1 ? "second" : "seconds").asString(),
				1,
				60,
				30
				)
		);
		registerSetting("length", new NumberSubSetting(
				() -> new ItemBuilder(Material.ARROW, Message.forName("item-random-effect-length-challenge")),
				value -> null,
				value -> "§e" + value + " §7" + Message.forName(value == 1 ? "second" : "seconds").asString(),
				1,
				20,
				10
				)
		);
		registerSetting("strength", new NumberSubSetting(
				() -> new ItemBuilder(Material.STONE_SWORD, Message.forName("item-random-effect-strength-challenge")),
				value -> null,
				value -> "§7" + Message.forName("strength") + " §e" + value,
				1,
				8,
				3
				)
		);

	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.BREWING_STAND, Message.forName("item-random-effect-challenge"));
	}

	@ScheduledTask(ticks = 20, async = false)
	public void onSecond() {
		currentTime++;

		if (currentTime > getSetting("time").getAsInt()) {
			currentTime = 0;
			applyRandomEffect();
		}

	}

	private void applyRandomEffect() {
		Bukkit.getOnlinePlayers().forEach(this::applyRandomEffect);
	}

	private void applyRandomEffect(@Nonnull Player player) {
		PotionEffectType effect = getNewRandomEffect(player);
		if (effect == null) return;
		applyEffect(effect);
	}

	@Nullable
	private PotionEffectType getNewRandomEffect(@Nonnull Player player) {
		List<PotionEffectType> activeEffects = player.getActivePotionEffects().stream().map(PotionEffect::getType).collect(Collectors.toList());

		ArrayList<PotionEffectType> possibleEffects = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
		possibleEffects.removeAll(activeEffects);
		possibleEffects.remove(PotionEffectType.GLOWING);
		possibleEffects.remove(PotionEffectType.HEAL);
		possibleEffects.remove(PotionEffectType.HARM);
		return possibleEffects.get(random.nextInt(possibleEffects.size()));
	}

	private void applyEffect(@Nonnull PotionEffectType effectType) {
		Bukkit.getOnlinePlayers().forEach(player -> applyEffect(player, effectType));
	}

	private void applyEffect(@Nonnull Player player, @Nonnull PotionEffectType effectType) {
		PotionEffect potionEffect = new PotionEffect(effectType, (getSetting("length").getAsInt() + 1) * 20, getSetting("strength").getAsInt() - 1);
		player.addPotionEffect(potionEffect);
	}

}