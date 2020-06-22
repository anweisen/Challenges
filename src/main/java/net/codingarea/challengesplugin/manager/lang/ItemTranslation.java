package net.codingarea.challengesplugin.manager.lang;

import net.codingarea.challengesplugin.manager.lang.LanguageManager.Language;
import net.codingarea.challengesplugin.utils.Replacement;

import java.util.ArrayList;
import java.util.List;

import static net.codingarea.challengesplugin.utils.Replacement.replace;

/**
 * @author anweisen
 * Challenges developed on 06-12-2020
 * https://github.com/anweisen
 */

public enum ItemTranslation {

	/*
	 * Example: TEST(new String[]{"name1", "name2"}, new String[][]{{"lore1.line1", "lore1.line2"}, {"lore2.line1", "lore2.line2"}});
	 * Template: new String[]{}, new String[][]{{},{}}
	 * Template a(), b(a(), a())
	 */

	ANVIL_RAIN(a("§8Anvilrain", "§8Ambossregen"), b(a("The setted count of *anvils* ", "will fall per *second*"), a("Die eingestellte *Anzahl* von *Ambossen*", "wird jede *Sekunde* vom Himmel fallen"))),
	BACKPACK(a("§6Backpack", "§6Rucksack"), b(a("With */bp* you can open", "a *private* or *team* backpack"), a("Mit */bp* kannst du einen", "*privaten* oder *gemeinsamen* Rucksack öffnen"))),
	BEDROCK_PATH(a("§8Bedrock path", "§8Bedrock-Pfad"), b(a("Every block you stand on will", "be transformed to *bedrock*"), a("Jeder Block, auf dem du stehst,", "wird zu *Bedrock*"))),
	BEDROCK_WALL(a("§8Bedrock walls", "§8Bedrock-Wände"), b(a("Behind you *giant bedrock walls*", "will be spawned"), a("Hinter dir werden *riesige Bedrock-Wände* spawnen"))),
	BLOCK_DROP_RANDOMIZER(a("§4Block randomizer"), b(a("If you break a *block* a", "random *item* will be dropped"), a("Wenn du einen *Block* abbaust, ", "wird ein zufälliges Item gedroppt."))),
	CHUNK_DECONSTRUCT(a("§bChunk Deconstruction"), b(a("*Chunks* with players in are *degrading* from *above*.", "The *setted number* says how fast"), a("*Chunks*, in denen sich Spieler befinden, *bauen* sich von *oben aus* ab.", "Die *eingestellte Zahl* sagt in welchem *Sekundentakt*"))),
	CHUNK_RACE(a(""), b(a("", ""), a("", ""))),
	CRAFTING_RANDOMIZER(a("§6Crafting Randomizer"), b(a("When you *craft* something", "you will get a *random item*"), a("Wenn du ein Item *craftest*,", "bekommst du ein *zufälliges Item*"))),
	FLOOR_HOLE(a("§cFloor hole"), b(a("When you *walk*, every block from *top* to *bottom*", "will be destroyed after a *certain time*"), a("Jeder Block, auf dem du läufst, wird von oben bis unten", "nach einer *bestimmten Zeit* hinter dir zerstört"))),
	FORCE_BLOCK(a("§bForce block"), b(a("You *must* be on *specific blocks*", "at a *specific time*"), a("Du musst zu *bestimmten Zeiten*", "auf *bestimmten Blöcken* sein"))),
	FORCE_CORDS(a("§bForce coords"), b(a("You *must* be on *specific coordinates*", "at a *specific time*"), a("Du musst zu *bestimmten Zeiten*", "auf *bestimmten Koordinaten* sein"))),
	FORCE_HIGH(a("§bForce height"), b(a("You *must* be on *specific heights*", "at a *specific time*"), a("Du musst zu *bestimmten Zeiten*", "auf *bestimmten Höhen* sein"))),
	HYDRA(a("§5Hydra"), b(a("When you kill a *mob*,", "it will spawn *twice*"), a("Wenn du ein *Mob* tötest,", "spawnt es *zweimal*"))),
	KEEP_INVENTORY(a("§5KeepInventory"), b(a("You won't *lose*", "your *inventory* if you *die*"), a("Du verlierst keine *Items*,", "wenn du *stirbst*"))),
	MOB_RANDOMIZER(a("§dMod randomizer"), b(a("Mobs will be *switched*.", "ex: instead of a *creeper* a *ghast* will spawn"), a("Mobs sind *getauscht*.", "z.B.: Anstatt von eines *Creepers* wird", "ein *Ghast* spawnen"))),
	NO_ITEM_DAMAGE(a("§8No item damage", "§8Kein Itemschaden"), b(a("All *items* are *unbreakable*"), a("*Items* werden *unzerstörbar* sein"))),
	NO_TRADING(a("§2No trading", "§2Kein Handeln"), b(a("You won't be able to", "*trade* with *villagers*"), a("Du kannst *nicht* mit", "*Villagern* handeln"))),
	NO_XP(a("§aNo XP", "§aKeine XP"), b(a("If you pickup *XP*, you will *die*"), a("Wenn du *XP* aufsammelst, *stirbst* du"))),
	ONLY_DIRT(a("§cOnly dirt", "§cNur Erde"), b(a("You may *only* stand on *dirt*.", " Otherwise you will *die*"), a("Wenn du nicht auf *Erde* stehst,", "dann *stirbst* du"))),
	SOUP_HEALTH(a("§cSoup healing", "§cSuppenheilung"), b(a("A *mushroom soup* gives", "you *4 hearts* healing"), a("*Pilzsuppen heilen* dich um *4 Herzen*"))),
	THE_FLOOR_IS_LAVA(a("§6The floor is lava", "§6Der Boden ist Lava"), b(a("*Every block* you stand on will be", "transformed to *magma* and later to *lava*"), a("Alle *Blöcke* unter dir verwandeln sich", "erst zu *Magma* und dann zu *Lava*"), a())),
	TIMBER(a("§bTimber"), b(a("You can destroy *trees* by", "destroying *one block* of the tree"), a("Du kannst einen *Baum* zerstören,", "indem du *einen Block* des Baums abbaust"))),
	UP_COMMAND(a("§dUpCommand"), b(a("With */top* you can teleport", "yourself to the *top* of the world"), a("Mit */top* kannst du dich", "zur *Oberfläche* teleportieren"))),
	WATER_MLG(a("§9Water MLG", "§9Wasser-MLG"), b(a("Every few *minutes* you have to", "do a *WaterMLG*. If you *die*, you *lose*"), a("Du musst alle *paar Minuten* einen *Wasser-MLG* machen.", "Wenn diesen nicht schaffst, *stirbst* du"))),
	DAMAGE_RULE(a("Angelo ist cool und Domi auch hehe"), b(a("You wont take *%info%*"), a("Du wirst kein *%info%* bekommen"))),
	MATERIAL_RULE(a("Wenn du das hier liest solltest du das Plugin updaten xd"), b(a("You are not able to use a *%info%*"), a("Du wirst kein *%info%* bekommen"))),
	DAMAGE_DISPLAY(a("§eDamage display"), b(a("When somebody takes *damage* it ", "will be shown *in chat*"), a("Wenn jemand *Schaden bekommt*, wird", "es im *Chat* angezeigt"))),
	DIFFICULTY(a("§bDifficulty", "§bSchwierigkeit"), b(a("Sets the *difficulty*"), a("Stell die *Schwierigkeit* ein"))),
	REGENERATION(a("§cRegeneration"), b(a("Sets how and whether to", "*regenerate*"), a("Stellt ein, wie und ob man", "*regeneriert*"))),
	MAX_HEALTH(a("§aMax health", "§aMaximale Herzen"), b(a("Sets how many *health*", "you have"), a("Stellt ein, wie viele", "*Herzen* man hat"))),
	SPLIT_HEALTH(a("§dSplit health", "§dGemeinsame Gesundheit"), b(a("Sets if all *players*", "have the same *health*"), a("Stellt ein, ob alle *Spieler*", "die gleiche *Gesundheit* haben"))),
	DAMAGE_MULTIPLIER(a("§cDamage multiplier"), b(a("Sets how much *damage* the", "player receives should *multiply*"), a("Stellt ein, um wie viel sich der *Schaden*,", "den der Spieler bekommt, *multiplizieren* soll"))),
	RESPAWN(a("§4Respawn"), b(a("Sets if the *player*", "should *respawn*"), a("Stellt ein, ob der *Spieler*", "*wiederbelebt* werden soll"))),
	ONE_TEAM_LIVE(a("§8One team life", "§8Ein Team-Leben"), b(a("Sets if the *team* should have", "only *one life*"), a("Stellt ein, ob alle alle *Spieler*", "nur ein gemeinsames Leben haben"))),
	Kill_ENDERDRAGON(a("§5Kill the dragon", "§5Töte den Enderdrachen"), b(a("*Kill* the *enderdragon* to win"), a("Töte den *Enderdrachen* um tu *gewinnen"))),
	KILL_WHITER(a("§5Kill a wither", "§5Töte einen Wither"), b(a("Summon and *kill* a *wither* to *win*"), a("Erschaffe und *tote* einen *Wither*"))),
	MOST_DEATHS(a("§6Most deaths", "§6Meiste Tode"), b(a("*Die* as often as you can in *different* ways"), a("Sammel die meisten *verschiedene Tode*"))),
	COLLECT_ITEMS(a("§dCollect items", "§dItems sammeln"), b(a("Collect as many *different items*", "as you can in order to *win*"), a("Sammel die meisten *verschiedenen Items*", "um zu *gewinnen*"))),
	BREAK_BLOCKS(a("§cBreak blocks", "§cBlöcke abbauen"), b(a("Mine the most *blocks* to win"), a("baue die meisten *Blöcke* ab", "um zu gewinnen"))),
	MINE_DIAMONDS(a("§bMine diamonds"), b(a("Mine the most *diamonds* to win"), a("Baue die meisten *Diamanten*", "ab um zu *gewinnen*"))),
	;

	private String[] name;
	private String[][] lore;

	ItemTranslation(String[] name, String[][] lore) {
		this.name = name;
		this.lore = lore;
	}

	private String[] getLore(Language language) {
		return lore[language.getID()];
	}

	public String getName() {
		return getName(LanguageManager.getLanguage());
	}

	private String getName(Language language) {
		if (name.length == 1) {
			return name[0];
		} else {
			try {
				return name[language.getID()];
			} catch (IndexOutOfBoundsException ignored) {
				return "§4§l404";
			}
		}
	}

	public String[] getLore() {
		return getLore(LanguageManager.getLanguage());
	}

	public static List<String> formatLore(String[] loreLines, String color, Replacement... replacements) {

		if (loreLines == null) return new ArrayList<>();
		if (loreLines.length == 0) return new ArrayList<>();

		String[] lore = loreLines.clone();

		List<String> format = new ArrayList<>();

		for (int i = 0; i < lore.length; i++) {
			lore[i] = replace(loreLines[i], replacements);
		}

		format.add(" ");
		for (String currentLore : lore) {
			StringBuilder builder = new StringBuilder();
			builder.append("§7");
			boolean colored = false;
			boolean nextIsColorCode = false;
			String lastColor = "7";
			for (String currentChar : currentLore.split("")) {

				if (currentChar.equals("§")) {
					nextIsColorCode = true;
				}
				if (nextIsColorCode) {
					nextIsColorCode = false;
					lastColor = currentChar;
				}

				if (currentChar.equals("*")) {
					if (colored) {
						colored = false;
						builder.append("§").append(lastColor);
					} else {
						colored = true;
						builder.append(color);
					}
				} else {
					builder.append(currentChar);
				}
			}
			format.add(builder.toString());
		}
		format.add(" ");

		return format;
	}

	public static String[] a(String... s) {
		return s;
	}

	public static String[][] b(String[]... s) {
		String[][] array = new String[s.length][];
		System.arraycopy(s, 0, array, 0, s.length);
		return array;
	}

}
