package net.codingarea.challengesplugin.manager.loader;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challenges.challenges.*;
import net.codingarea.challengesplugin.challenges.challenges.force.ForceBlockChallenge;
import net.codingarea.challengesplugin.challenges.challenges.force.ForceHighChallenge;
import net.codingarea.challengesplugin.challenges.challenges.randomizer.BlockRandomizerChallenge;
import net.codingarea.challengesplugin.challenges.challenges.randomizer.CraftingRandomizerChallenge;
import net.codingarea.challengesplugin.challenges.challenges.randomizer.EntitySpawnRandomizerChallenge;
import net.codingarea.challengesplugin.challenges.difficulty.*;
import net.codingarea.challengesplugin.challenges.goal.*;
import net.codingarea.challengesplugin.challenges.rules.DamageRule;
import net.codingarea.challengesplugin.challenges.rules.MaterialRule;
import net.codingarea.challengesplugin.challenges.settings.*;
import net.codingarea.challengesplugin.challengetypes.GeneralChallenge;
import net.codingarea.challengesplugin.challengetypes.Goal;
import net.codingarea.challengesplugin.manager.ChallengeManager;
import net.codingarea.challengesplugin.utils.Utils;
import com.github.challengesplugin.challenges.challenges.*;
import com.github.challengesplugin.challenges.challenges.force.*;
import com.github.challengesplugin.challenges.challenges.randomizer.*;
import com.github.challengesplugin.challenges.difficulty.*;
import com.github.challengesplugin.challenges.goal.*;
import com.github.challengesplugin.challenges.rules.*;
import com.github.challengesplugin.challenges.settings.*;
import net.godingarea.challengesplugin.challenges.challenges.*;
import net.godingarea.challengesplugin.challenges.difficulty.*;
import net.godingarea.challengesplugin.challenges.goal.*;
import net.godingarea.challengesplugin.challenges.settings.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/Traolian
 */

public class ChallengeLoader {

	private static ChallengeManager challengeManager;

	public ChallengeLoader(ChallengeManager manager) {
		challengeManager = manager;
	}

	public void loadChallenges() {

		// SETTINGS
		registerChallenge(new DamageDisplay());
		registerChallenge(new NoTradingSetting());
		registerChallenge(new SoupSetting());
		registerChallenge(new UnbreakableSetting());
		registerChallenge(new KeepInventorySetting());
		registerChallengeWithCommand("backpack", new BackpackModifier(challengeManager.getPlugin().getConfig().getInt("backpack-size"), challengeManager));
		registerChallenge(new TimberSetting());
		registerChallengeWithCommand("up", new UpCommand());

		// CHALLENGES
		registerChallenge(new TheFloorIsLavaChallenge());
		registerChallenge(new FloorDisappearsChallenge());
		registerChallenge(new BedrockWallChallenge());
		registerChallenge(new BedrockPathChallenge());
		registerChallenge(new WaterMLGChallenge());
		registerChallenge(new AnvilRainChallenge());
		registerChallenge(new NoExpChallenge());
		registerChallenge(new HydraChallenge());
		registerChallenge(new OnlyDirtChallenge());
		registerChallenge(new BlockRandomizerChallenge());
		registerChallenge(new CraftingRandomizerChallenge());
		registerChallenge(new EntitySpawnRandomizerChallenge());
		registerChallenge(new ForceHighChallenge());
		registerChallenge(new ForceBlockChallenge());
		registerChallenge(new ChunkDeconstructChallenge());

		// GOALS
		registerChallenge(new EnderDragonGoal());
		registerChallenge(new WitherGoal());
		registerChallenge(new CollectDeathsGoal());
		registerChallenge(new CollectItemsGoal());
		registerChallenge(new BreakBlockGoal());
		registerChallenge(new MineDiamondsGoal());

		// DIFFICULTY
		registerChallenge(new DifficultyModifier());
		registerChallenge(new RegenerationModifier());
		registerChallenge(new MaxHealthModifier());
		registerChallenge(new SplitHealthSetting());
		registerChallenge(new DamageMultiplierModifier());
		registerChallenge(new PlayerRespawnSetting());
		registerChallenge(new KillAllOnPlayerDeath());

		// BLOCK & ITEMS
		registerChallenge(new MaterialRule(Material.IRON_CHESTPLATE, "§7Armor", Utils.getArmors()));
		registerChallenge(new MaterialRule(Material.GOLDEN_APPLE, "§6Golden Apple", Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE));
		registerChallenge(new MaterialRule(Material.CRAFTING_TABLE, "§6"));
		registerChallenge(new MaterialRule(Material.CHEST, "§6"));
		registerChallenge(new MaterialRule(Material.FURNACE, "§7"));
		registerChallenge(new MaterialRule(Material.ENCHANTING_TABLE, "§5"));
		registerChallenge(new MaterialRule(Material.ANVIL, "§7Anvil", Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL));
		registerChallenge(new MaterialRule(Material.FLETCHING_TABLE, "§e"));
		registerChallenge(new MaterialRule(Material.BREWING_STAND, "§d"));
		registerChallenge(new MaterialRule(Material.BOW , "§c"));
		registerChallenge(new MaterialRule(Material.SNOWBALL , "§f"));
		registerChallenge(new MaterialRule(Material.FLINT_AND_STEEL , "§7"));

		// DAMAGE
		registerChallenge(new DamageRule(Material.FLINT_AND_STEEL, "§4Fire Damage", DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.HOT_FLOOR));
		registerChallenge(new DamageRule(Material.LAVA_BUCKET, "§6Lava Damage", DamageCause.LAVA));
		registerChallenge(new DamageRule(Material.FEATHER, "§fFall Damage", DamageCause.FALL));
		registerChallenge(new DamageRule(Material.TNT, "§cExplosion Damage", DamageCause.BLOCK_EXPLOSION, DamageCause.ENTITY_EXPLOSION));
		registerChallenge(new DamageRule(Material.IRON_SWORD, "§dAttack Damage", DamageCause.PROJECTILE, DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_SWEEP_ATTACK));
		registerChallenge(new DamageRule(Material.FERMENTED_SPIDER_EYE, "§5Magic Damage", DamageCause.MAGIC, DamageCause.WITHER, DamageCause.POISON, DamageCause.DRAGON_BREATH, DamageCause.LIGHTNING));
		registerChallenge(new DamageRule(Material.GRAVEL, "§cBlock Damage", DamageCause.FALLING_BLOCK, DamageCause.SUFFOCATION, DamageCause.FLY_INTO_WALL, DamageCause.CONTACT));

	}

	public static void registerChallengeWithCommand(String command, GeneralChallenge challenge) {

		if (!(challenge instanceof CommandExecutor)) throw new IllegalArgumentException("No command!");

		registerChallenge(challenge);
		challengeManager.getPlugin().getCommand(command).setExecutor((CommandExecutor) challenge);

	}

	public static void regenerateInventories() {
		Challenges.getInstance().getChallengeManager().generateInventories();
	}

	public static void registerChallenge(GeneralChallenge challenge) {

		if (challenge == null) throw new NullPointerException("Challenge cannot be null!");

		challengeManager.getChallenges().add(challenge);

		if (challenge instanceof Goal) {
			challengeManager.getGoalManager().addGoal((Goal) challenge);
		}
		if (challenge instanceof Listener) {
			Bukkit.getPluginManager().registerEvents((Listener) challenge, challengeManager.getPlugin());
		}
	}

}
