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
import net.codingarea.challengesplugin.challengetypes.AbstractChallenge;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * @author anweisen & Dominik
 * Challenges developed on 05-30-2020
 * https://github.com/anweisen
 * https://github.com/KxmischesDomi
 */

public class PluginChallengeLoader {

	private final Challenges plugin;

	public PluginChallengeLoader(Challenges plugin) {
		this.plugin = plugin;
	}

	public void loadChallenges() {

		// SETTINGS
		registerChallenge(new DamageDisplay());
		registerChallenge(new PreGameMovement());
		registerChallenge(new DeathMessages());
		registerChallenge(new PlayerGlowing());
		registerChallenge(new SoupSetting());
		registerChallenge(new UnbreakableSetting());
		registerChallenge(new NoHungerSetting());
		registerChallenge(new KeepInventorySetting());
		registerChallenge(new BackpackModifier(plugin.getConfig().getInt("backpack-size")), "backpack");
		registerChallenge(new TimberSetting());
		registerChallenge(new PvPSetting());
		registerChallenge(new NoHitDelay());
		registerChallenge(new UpCommand(), "up");

		// CHALLENGES
		//registerChallengeWithCommands(new GuessTheFlag(), "skipflag", "currentflag");
		registerChallenge(new SnakeChallenge());
		registerChallenge(new CoronaChallenge());
		registerChallenge(new DamagePerBlockChallenge());
		registerChallenge(new FoodOnce());
		registerChallenge(new TheFloorIsLavaChallenge());
		registerChallenge(new FloorDisappearsChallenge());
		registerChallenge(new BedrockWallChallenge());
		registerChallenge(new BedrockPathChallenge());
		registerChallenge(new WaterMLGChallenge());
		registerChallenge(new ReverseDamage());
		registerChallenge(new HydraChallenge());
		registerChallenge(new DupedSpawnChallenge());
		registerChallenge(new AnvilRainChallenge());
		registerChallenge(new NoXPChallenge());
		registerChallenge(new NoTrading());
		registerChallenge(new AchievmentDamage());
		registerChallenge(new BlockBreakDamage());
		registerChallenge(new OnlyDirtChallenge());
		if (Challenges.getVersion() != 16) registerChallenge(new JumpHigherChallenge());
		registerChallenge(new InventoryClearOnDamage());
		registerChallenge(new OneDurabilityTools());
		registerChallenge(new BlockRandomizerChallenge());
		registerChallenge(new CraftingRandomizerChallenge());
		registerChallenge(new EntitySpawnRandomizerChallenge());
		registerChallenge(new NoSneakChallenge());
		if (Challenges.getVersion() != 16) registerChallenge(new NoJumpChallenge());
		registerChallenge(new ForceHighChallenge());
		registerChallenge(new ForceBlockChallenge(), "skipblock");
		registerChallenge(new ChunkDeconstructChallenge());

		// GOALS
		registerChallenge(new EnderDragonGoal());
		registerChallenge(new WitherGoal());
		registerChallenge(new CollectDeathsGoal());
		registerChallenge(new CollectItemsGoal());
		registerChallenge(new CollectAllWood());
		//registerChallengeWithCommands(new Bingo(), "bingo", "resetbingo", "teams");
		registerChallenge(new BreakBlockGoal());
		registerChallenge(new MineDiamondsGoal());

		// DIFFICULTY
		registerChallenge(new DifficultyModifier());
		registerChallenge(new RegenerationModifier());
		registerChallenge(new SplitHealthSetting());
		registerChallenge(new MaxHealthModifier());
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
		if (Challenges.getVersion() > 13) registerChallenge(new MaterialRule(Material.FLETCHING_TABLE, "§e"));
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

	private void registerChallenge(AbstractChallenge challenge, String... commands) {
		ChallengeLoader.register(challenge, plugin, commands);
	}

}
