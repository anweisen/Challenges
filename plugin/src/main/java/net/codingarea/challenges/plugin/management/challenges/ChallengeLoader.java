package net.codingarea.challenges.plugin.management.challenges;

import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.challenges.implementation.challenge.*;
import net.codingarea.challenges.plugin.challenges.implementation.goal.*;
import net.codingarea.challenges.plugin.challenges.implementation.setting.*;
import net.codingarea.challenges.plugin.utils.item.ItemBuilder.PotionBuilder;
import net.codingarea.challenges.plugin.utils.misc.ItemUtils;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * This class loads all challenges of this plugin.
 * In pre2 versions, this class was known as PluginChallengeLoader.
 *
 * @author anweisen | https://github.com/anweisen
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public final class ChallengeLoader extends ModuleChallengeLoader {

	public ChallengeLoader() {
		super(Challenges.getInstance());
	}

	public void load() {
		// Settings
		registerWithCommand(DifficultySetting.class, "difficulty");
		register(RegenerationSetting.class);
		register(OneTeamLifeSetting.class);
		register(RespawnSetting.class);
		register(SplitHealthSetting.class);
		register(DamageDisplaySetting.class);
		register(PregameMovementSetting.class);
		register(DeathMessageSetting.class);
		register(HealthDisplaySetting.class);
		registerWithCommand(PositionSetting.class, "position");
		register(DeathPositionSetting.class);
		register(PlayerGlowSetting.class);
		register(SoupSetting.class);
		register(NoHungerSetting.class);
		register(NoItemDamageSetting.class);
		register(KeepInventorySetting.class);
		registerWithCommand(BackpackSetting.class, "backpack");
		registerWithCommand(EnderChestCommandSetting.class, "enderchest");
		register(TimberSetting.class);
		register(PvPSetting.class);
		register(NoHitDelaySetting.class);
		registerWithCommand(TopCommandSetting.class, "top");
		register(MaxHealthSetting.class);
		register(DamageMultiplierModifier.class);
		register(CutCleanSetting.class);
		register(FortressSpawnSetting.class);
		register(BastionSpawnSetting.class);
		register(NoOffhandSetting.class);
		register(ImmediateRespawnSetting.class);
		register(SlotLimitSetting.class);


		// Challenges
		register(RandomChallengeChallenge.class);
		register(WaterMLGChallenge.class);
		register(NoTradingChallenge.class);
		register(NoExpChallenge.class);
		register(OneDurabilityChallenge.class);
		register(OnlyDirtChallenge.class);
		register(RandomizedHPChallenge.class);

		register(HungerPerBlockChallenge.class);
		register(DamagePerBlockChallenge.class);
		register(SneakDamageChallenge.class);
		register(JumpDamageChallenge.class);
		register(BlockBreakDamageChallenge.class);
		register(BlockPlaceDamageChallenge.class);
		register(AdvancementDamageChallenge.class);

		register(SurfaceHoleChallenge.class);
		register(BedrockWallChallenge.class);
		register(BedrockPathChallenge.class);
		register(FloorIsLavaChallenge.class);
		register(ChunkDeconstructionChallenge.class);
		register(AllBlocksDisappearChallenge.class);
		register(TsunamiChallenge.class);

		register(JumpAndRunChallenge.class);
		register(SnakeChallenge.class);
		register(AnvilRainChallenge.class);
		register(TrafficLightChallenge.class);
		register(ReversedDamageChallenge.class);
		register(DeathOnFallChallenge.class);
		register(FoodOnceChallenge.class);

		register(HydraNormalChallenge.class);
		register(HydraPlusChallenge.class);
		register(DupedSpawningChallenge.class);
		register(LowDropRateChallenge.class);
		register(BlockRandomizerChallenge.class);
		register(CraftingRandomizerChallenge.class);
		register(MobRandomizerChallenge.class);

		register(ForceHeightChallenge.class);
		register(ForceBlockChallenge.class);
		register(ForceMobChallenge.class);
		register(ForceItemChallenge.class);
		register(ForceBiomeChallenge.class);
		register(MaxBiomeTimeChallenge.class);
		register(MaxHeightTimeChallenge.class);

		register(PermanentItemChallenge.class);
		register(RandomItemDroppingChallenge.class);
		register(RandomItemSwappingChallenge.class);
		register(RandomItemRemovingChallenge.class);
		register(NoDupedItemsChallenge.class);
		register(MovementItemRemovingChallenge.class);
		register(DamageInventoryClearChallenge.class);

		register(RandomItemChallenge.class);
		register(RandomEventChallenge.class);
		register(NoMoveMouseChallenge.class);
		register(PickupItemLaunchChallenge.class);
		register(HigherJumpsChallenge.class);
		register(AlwaysRunningChallenge.class);
		register(OnlyDownChallenge.class);

		register(MobTransformationChallenge.class);
		register(NewEntityOnJumpChallenge.class);
		register(AllMobsToDeathPoint.class);
		register(EnderGamesChallenge.class);
		register(InvisibleMobsChallenge.class);
		register(MobSightDamageChallenge.class);
		register(StoneSightChallenge.class);

		register(InfectionChallenge.class);
		register(PermanentItemChallenge.class);
		register(RandomPotionEffectChallenge.class);
		register(WaterAllergyChallenge.class);
		register(InvertHealthChallenge.class);


		// Goal
		register(KillEnderDragonGoal.class);
		register(KillWitherGoal.class);
		register(KillElderGuardianGoal.class);
		register(KillAllBossesGoal.class);
		register(LastManStandingGoal.class);
		registerWithCommand(CollectAllItemsGoal.class, "skipitem");
		register(CollectMostDeathsGoal.class);
		register(CollectMostItemsGoal.class);
		register(MineMostBlocksGoal.class);
		register(CollectMostExpGoal.class);
		register(FirstOneToDieGoal.class);
		register(CollectWoodGoal.class);
		register(FinishRaidGoal.class);

		// Damage Rules
		registerDamageRule("fire",      Material.LAVA_BUCKET,               DamageCause.FIRE, DamageCause.FIRE_TICK, DamageCause.LAVA);
		registerDamageRule("attack",    Material.DIAMOND_SWORD,             DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_SWEEP_ATTACK, DamageCause.ENTITY_EXPLOSION);
		registerDamageRule("projectile",Material.ARROW,                     DamageCause.PROJECTILE);
		registerDamageRule("fall",      Material.FEATHER,                   DamageCause.FALL);
		registerDamageRule("explosion", Material.TNT,                       DamageCause.ENTITY_EXPLOSION, DamageCause.BLOCK_EXPLOSION);
		registerDamageRule("drowning",  PotionBuilder.createWaterBottle(),  DamageCause.DROWNING);
		registerDamageRule("block",     Material.SAND,                      DamageCause.FALLING_BLOCK, DamageCause.SUFFOCATION, DamageCause.CONTACT);
		registerDamageRule("magic",     Material.BREWING_STAND,             DamageCause.MAGIC, DamageCause.POISON, DamageCause.WITHER);

		// Material Rules
		registerMaterialRule("§cArmor", "Armor", ItemUtils.getArmor());
		registerMaterialRule("§6Golden Apple", "Golden Apple", Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE);
		registerMaterialRule("§6Crafting Table", "Crafting Table", Material.CRAFTING_TABLE);
		registerMaterialRule("§6Chest", "Chest", Material.CHEST);
		registerMaterialRule("§cFurnace", "Furnace", Material.FURNACE, Material.FURNACE_MINECART);
		registerMaterialRule("§5Enchanting Table", "Enchanting Table", Material.ENCHANTING_TABLE);
		registerMaterialRule("§cAnvil", "Anvil", Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL);
		registerMaterialRule("§dBrewing Stand", "Brewing Stand", Material.BREWING_STAND);
		registerMaterialRule("§cBow", "Bow", Material.BOW);
		registerMaterialRule("§fSnowball", "Snowball", Material.SNOWBALL);
		registerMaterialRule("§cFlint and Steel", "Flint and Steel", Material.FLINT_AND_STEEL);
		registerMaterialRule("§cBucket", "Bucket", Material.BUCKET);
	}

}
