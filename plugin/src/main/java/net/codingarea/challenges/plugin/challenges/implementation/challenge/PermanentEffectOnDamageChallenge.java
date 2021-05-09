package net.codingarea.challenges.plugin.challenges.implementation.challenge;

import net.anweisen.utilities.commons.annotations.Since;
import net.anweisen.utilities.commons.common.Tuple;
import net.anweisen.utilities.commons.config.Document;
import net.codingarea.challenges.plugin.challenges.type.SettingModifier;
import net.codingarea.challenges.plugin.challenges.type.helper.ChallengeHelper;
import net.codingarea.challenges.plugin.language.Message;
import net.codingarea.challenges.plugin.language.Prefix;
import net.codingarea.challenges.plugin.management.menu.MenuType;
import net.codingarea.challenges.plugin.management.scheduler.task.TimerTask;
import net.codingarea.challenges.plugin.management.scheduler.timer.TimerStatus;
import net.codingarea.challenges.plugin.utils.item.DefaultItem;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import net.codingarea.challenges.plugin.utils.misc.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
@Since("2.0")
public class PermanentEffectOnDamageChallenge extends SettingModifier {

	private static final int GLOBAL_EFFECT = 1;
	private final Random random = new Random();

	public PermanentEffectOnDamageChallenge() {
		super(MenuType.CHALLENGES, 1, 2);
	}

	@Nonnull
	@Override
	public ItemBuilder createDisplayItem() {
		return new ItemBuilder(Material.MAGMA_CREAM, Message.forName("item-permanent-effect-on-damage-challenge"));
	}

	@Override
	protected void onEnable() {
		if (!shouldExecuteEffect()) return;
		updateEffects();
	}

	@Override
	protected void onDisable() {
		clearEffects();
	}

	@TimerTask(status = TimerStatus.RUNNING, async = false)
	public void onTimerStart() {
		if (!isEnabled()) return;
		updateEffects();
	}

	@TimerTask(status = TimerStatus.PAUSED, async = false)
	public void onTimerPause() {
		if (!isEnabled()) return;
		clearEffects();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		updateEffects();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerItemConsume(@Nonnull PlayerItemConsumeEvent event) {
		if (!shouldExecuteEffect()) return;
		if (ignorePlayer(event.getPlayer())) return;
		updateEffects();
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerGameModeChange(@Nonnull PlayerGameModeChangeEvent event) {
		if (!shouldExecuteEffect()) return;
		Bukkit.getScheduler().runTask(plugin, () -> {
			clearEffects();
			updateEffects();
		});
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerDamage(@Nonnull EntityDamageEvent event) {
		if (!shouldExecuteEffect()) return;
		if (event.getFinalDamage() <= 0) return;
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		if (ignorePlayer(player)) return;
		DamageCause cause = event.getCause();
		if (cause == DamageCause.POISON || cause == DamageCause.WITHER) return;

		addRandomEffect(player);
		updateEffects();
	}

	private void addRandomEffect(@Nonnull Player eventPlayer) {
		PotionEffectType randomEffect = getNewRandomEffect();
		if (randomEffect == null) return;
		applyNewEffect(eventPlayer, randomEffect);
	}

	private void applyNewEffect(@Nonnull Player player, @Nonnull PotionEffectType potionEffectType) {
		String path = player.getUniqueId().toString();
		Document effects = getGameStateData().getDocument(path);

		int amplifier = 1;
		if (!effects.contains(potionEffectType.getName())) {
			effects.set(potionEffectType.getName(), 1);
		} else {
			amplifier = effects.getInt(potionEffectType.getName()) + 1;
			effects.set(potionEffectType.getName(), amplifier);
		}

		if (effectsToEveryone()) {
			Message.forName("new-effect").broadcast(Prefix.CHALLENGES, potionEffectType.getName(), amplifier);
		} else {
			Message.forName("new-effect").send(player, Prefix.CHALLENGES, potionEffectType.getName(), amplifier);
		}

		getGameStateData().set(path, effects);
	}

	@Nullable
	private PotionEffectType getNewRandomEffect() {
		ArrayList<PotionEffectType> possibleEffects = new ArrayList<>(Arrays.asList(PotionEffectType.values()));
		possibleEffects.remove(PotionEffectType.HEAL);
		possibleEffects.remove(PotionEffectType.HARM);
		return possibleEffects.get(random.nextInt(possibleEffects.size()));
	}

	private void updateEffects() {
		forEachEffect((player, effectType, amplifier) -> {
			if (effectsToEveryone()) {
				broadcastFiltered(currentPlayer -> {
					addEffect(currentPlayer, effectType, amplifier);
				});
			} else {
				if (!ignorePlayer(player)) {
					addEffect(player, effectType, amplifier);
				}
			}
		});
	}

	private void addEffect(@Nonnull Player player, @Nonnull PotionEffectType effectType, int amplifier) {
		player.removePotionEffect(effectType);
		player.addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier - 1));
	}

	private void clearEffects() {
		forEachEffect((player, potionEffectType, amplifier) -> {
			broadcast(currentPlayer -> {
				currentPlayer.removePotionEffect(potionEffectType);
			});
		});
	}

	private void forEachEffect(@Nonnull TriConsumer<Player, PotionEffectType, Integer> action) {
		List<Tuple<PotionEffectType, Integer>> effects = new ArrayList<>();

		for (String uuid : getGameStateData().keys()) {

			Document effectsDocument = getGameStateData().getDocument(uuid);
			for (String effectName : effectsDocument.keys()) {
				PotionEffectType effectType = PotionEffectType.getByName(effectName);
				int amplifier = effectsDocument.getInt(effectName);
				if (effectType == null) continue;

				if (effectsToEveryone()) {
					addEffectToList(effects, effectType, amplifier);
				} else {
					try {
						Player player = Bukkit.getPlayer(UUID.fromString(uuid));
						action.accept(player, effectType, amplifier);
					} catch (Exception ex) {
						Logger.debug("UUID formation for uuid '" + uuid + "' failed in permanent effect challenge!");
					}
				}
			}

		}

		effects.forEach(tuple -> {
			broadcastFiltered(player -> {
				action.accept(player, tuple.getFirst(), tuple.getSecond());
			});
		});
	}

	private void addEffectToList(@Nonnull List<Tuple<PotionEffectType, Integer>> effectsList, @Nonnull PotionEffectType effectType, int amplifier) {
		Tuple<PotionEffectType, Integer> effectTuple = effectsList.stream().filter(tuple -> tuple.getFirst() == effectType).findFirst()
				.orElse(new Tuple<>(effectType, 0));
		effectTuple.setSecond(effectTuple.getSecond() + amplifier);

		if (!effectsList.contains(effectTuple)) effectsList.add(effectTuple);
	}

	private boolean effectsToEveryone() {
		return GLOBAL_EFFECT == getValue();
	}

	@Override
	protected void onValueChange() {
		if (!shouldExecuteEffect()) return;
		clearEffects();
		updateEffects();
	}

	@Nonnull
	@Override
	public ItemBuilder createSettingsItem() {
		if (!isEnabled()) return DefaultItem.disabled();
		if (getValue() == GLOBAL_EFFECT) return DefaultItem.create(Material.ENDER_CHEST, "§5Everyone")
				.appendLore("", Message.forName("item-permanent-effect-target-everyone-description").toString());
		return DefaultItem.create(Material.CHEST, "§6Only active player")
				.appendLore("", Message.forName("item-permanent-effect-target-player-description").toString());
	}

	@Override
	public void playValueChangeTitle() {
		if (GLOBAL_EFFECT == getValue()) {
			ChallengeHelper.playChangeChallengeValueTitle(this, "§5Everyone");
		} else {
			ChallengeHelper.playChangeChallengeValueTitle(this, "§6Only active player");
		}

	}

}