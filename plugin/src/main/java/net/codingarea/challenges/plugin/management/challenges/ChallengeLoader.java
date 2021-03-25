package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.*;
import net.codingarea.challenges.plugin.challenges.implementation.damage.DamageRuleSetting;
import net.codingarea.challenges.plugin.challenges.implementation.goal.*;
import net.codingarea.challenges.plugin.challenges.implementation.setting.*;
import net.codingarea.challenges.plugin.challenges.type.IChallenge;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import net.codingarea.challenges.plugin.utils.logging.Logger;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * This class loads all challenges of this plugin.
 * In pre2 versions, this class was known as PluginChallengeLoader.
 *
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public final class ChallengeLoader {

	public void load() {
		// Settings
		register(DifficultySetting.class);
		register(OneLifeSetting.class);
		register(RespawnSetting.class);
		register(DamageDisplaySetting.class);
		register(PregameMovementSetting.class);
		register(DeathMessageSetting.class);
		register(HealthDisplaySetting.class);
		registerWithCommand(PositionSetting.class, "position");
		register(PlayerGlowSetting.class);
		register(SoupSetting.class);
		register(NoHungerSetting.class);
		register(NoItemDamageSetting.class);
		register(KeepInventorySetting.class);
		registerWithCommand(BackpackSetting.class, "backpack");
		register(TimberSetting.class); // TODO: Handle break event
		register(PvPSetting.class);
		register(NoHitDelaySetting.class);
		registerWithCommand(TopCommandSetting.class, "top");
		register(MaxHealthModifier.class);
		register(DamageMultiplierModifier.class);
		register(CutCleanSetting.class);
		register(FortressSpawnSetting.class);

		// Challenges
		register(TrafficLightChallenge.class);
		register(SnakeChallenge.class);
		register(JumpAndRunChallenge.class);
		// Corona
		register(RandomizedHPChallenge.class);
		register(DamagePerBlockChallenge.class);
		// Food once
		// Floor is Lava
		//
		register(SurfaceHoleChallenge.class);
		register(BedrockWallChallenge.class);
		register(BedrockPathChallenge.class);
		// Water MLG
		register(ReversedDamageChallenge.class);
		// Hydra
		register(DupedSpawningChallenge.class);
		// Anvil rain
		register(NoExpChallenge.class);
		register(NoTradingChallenge.class);
		// Achievement damage
		register(BlockBreakDamage.class);
		register(BlockPlaceDamage.class);
		// Only dirt
		// High jumps
		register(DamageInventoryClearChallenge.class);
		// One durability
		register(BlockRandomizerChallenge.class);
		register(CraftingRandomizerChallenge.class);
		// Mob randomizer
		// No Sneak
		// No Jump
		// Force height
		// Force block
		// Force mob kill
		register(InvertHealthChallenge.class);
		register(StoneSightChallenge.class);

		// Goal
		register(KillEnderDragonGoal.class);
		register(KillWitherGoal.class);
		register(LastManStandingGoal.class);
		register(CollectMostDeathsGoal.class);
		register(CollectMostItemsGoal.class);
		register(MineMostBlocksGoal.class);

		// Damage Rules
		registerDamageRule("fire",      Material.LAVA_BUCKET,               DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA);
		registerDamageRule("attack",    Material.DIAMOND_SWORD,             DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_SWEEP_ATTACK, DamageCause.ENTITY_EXPLOSION);
		registerDamageRule("projectile",Material.ARROW,                     DamageCause.PROJECTILE);
		registerDamageRule("fall",      Material.FEATHER,                   DamageCause.FALL);
		registerDamageRule("explosion", Material.TNT,                       DamageCause.ENTITY_EXPLOSION, DamageCause.BLOCK_EXPLOSION);
		registerDamageRule("drowning",  PotionBuilder.createWaterBottle(),  DamageCause.DROWNING);
		registerDamageRule("block",     Material.SAND,                      DamageCause.FALLING_BLOCK, DamageCause.SUFFOCATION);
	}

	private void registerWithCommand(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull String[] commandNames, @Nonnull Class<?>[] parameterClasses, @Nonnull Object... parameters) {
		try {

			Constructor<? extends IChallenge> constructor = classOfChallenge.getDeclaredConstructor(parameterClasses);
			IChallenge challenge = constructor.newInstance(parameters);

			Challenges.getInstance().getChallengeManager().register(challenge);

			if (challenge instanceof CommandExecutor) {
				Challenges.getInstance().registerCommand((CommandExecutor) challenge, commandNames);
			}
			if (challenge instanceof Listener) {
				Challenges.getInstance().registerListener((Listener) challenge);
			}

		} catch (Throwable ex) {
			Logger.severe("Could not register challenge " + classOfChallenge.getSimpleName(), ex);
		}
	}

	private void register(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull Class<?>[] parameterClasses, @Nonnull Object... parameters) {
		registerWithCommand(classOfChallenge, new String[0], parameterClasses, parameters);
	}

	private void register(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull Object... parameters) {

		Class<?>[] parameterClasses = new Class[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterClasses[i] = Optional.ofNullable(parameters[i]).<Class<?>>map(Object::getClass).orElse(Object.class);
		}

		register(classOfChallenge, parameterClasses, parameters);

	}

	private void registerWithCommand(@Nonnull Class<? extends IChallenge> classOfChallenge, @Nonnull String... commandNames) {
		registerWithCommand(classOfChallenge, commandNames, new Class[0]);
	}


	private void registerDamageRule(@Nonnull String name, @Nonnull Material material, @Nonnull DamageCause... causes) {
		registerDamageRule(name, new ItemBuilder(material), causes);
	}

	private void registerDamageRule(@Nonnull String name, @Nonnull ItemBuilder preset, @Nonnull DamageCause... causes) {
		register(DamageRuleSetting.class, new Class[] { ItemBuilder.class, String.class, DamageCause[].class }, preset, name, causes);
	}

}
