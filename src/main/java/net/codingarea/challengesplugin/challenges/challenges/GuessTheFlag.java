package net.codingarea.challengesplugin.challenges.challenges;

import net.codingarea.challengesplugin.Challenges;
import net.codingarea.challengesplugin.challengetypes.Challenge;
import net.codingarea.challengesplugin.challengetypes.extra.ITimerStatusExecutor;
import net.codingarea.challengesplugin.manager.ServerManager;
import net.codingarea.challengesplugin.manager.events.ChallengeEditEvent;
import net.codingarea.challengesplugin.manager.events.ChallengeEndCause;
import net.codingarea.challengesplugin.manager.lang.ItemTranslation;
import net.codingarea.challengesplugin.manager.lang.Translation;
import net.codingarea.challengesplugin.manager.menu.MenuType;
import net.codingarea.challengesplugin.utils.AnimationUtil.AnimationSound;
import net.codingarea.challengesplugin.utils.ImageUtils;
import net.codingarea.challengesplugin.utils.ItemBuilder;
import net.codingarea.challengesplugin.utils.StringUtils;
import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static net.codingarea.challengesplugin.manager.lang.ItemTranslation.a;

/**
 * @author anweisen
 * Challenges developed on 06-30-2020
 * https://github.com/anweisen
 */

public class GuessTheFlag extends Challenge implements Listener, CommandExecutor, ITimerStatusExecutor {

	private final Random random;
	private Flag currentFlag;

	public GuessTheFlag() {
		menu = MenuType.CHALLENGES;
		random = new Random();
	}

	@Override
	public void onEnable(ChallengeEditEvent event) {
		setNextSeconds();
	}

	@Override
	public void onTimerStop() {
		setNextSeconds();
	}

	private void setNextSeconds() {
		currentFlag = null;
		int min = 6*60;
		int max = 10*60;
		nextActionInSeconds = random.nextInt(max - min) + min;
	}

	@Override
	public void onDisable(ChallengeEditEvent event) { }

	@Override
	public void onTimeActivation() {
		if (currentFlag == null) {
			setNewFlag();
		} else {
			onTimeIsUp();
			setNextSeconds();
		}
	}

	private void setNewFlag() {
		currentFlag = Flag.values()[random.nextInt(Flag.values().length)];
		nextActionInSeconds = 20;
		onNewFlag();
	}

	private void onNewFlag() {

		String[] flagMessage = ImageUtils.getImageLines(currentFlag.getFlagURL());

		for (String currentMessageLine : flagMessage) {
			Bukkit.broadcastMessage(currentMessageLine);
		}

		String time = nextActionInSeconds + " " + Translation.SECONDS.get();
		Bukkit.broadcastMessage(" \n" + Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_NEW.get().replace("%time%", time));

		new AnimationSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1.2F).broadcast();

	}

	private void handleInput(AsyncPlayerChatEvent event) {

		boolean isOK = currentFlag.isOK(event.getMessage());

		if (isOK) {
			onCorrect(event);
		} else {
			onWrong(event);
		}

	}

	private void onCorrect(AsyncPlayerChatEvent chatEvent) {
		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_RIGHT.get()
				.replace("%flag%", currentFlag.getName())
				.replace("%player%", chatEvent.getPlayer().getName()));
		setNextSeconds();
	}

	private void onWrong(AsyncPlayerChatEvent chatEvent) {
		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_WRONG.get()
				.replace("%player%", chatEvent.getPlayer().getName())
				.replace("%flag%", chatEvent.getMessage()));
		chatEvent.getPlayer().sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_FLAG_WAS.get().replace("%flag%", currentFlag.getName()));

		// Running the player damage synced because this is called async
		Bukkit.getScheduler().runTask(Challenges.getInstance(), () -> {
			chatEvent.getPlayer().damage(chatEvent.getPlayer().getHealth() + 1);
		});
	}

	private void onTimeIsUp() {
		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_TIME_IS_UP);
		Bukkit.broadcastMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_FLAG_WAS.get().replace("%flag%", currentFlag.getName()));
		currentFlag = null;
		ServerManager.simulateChallengeEnd(null, ChallengeEndCause.PLAYER_CHALLENGE_FAIL);
	}

	@Override
	public ItemStack getItem() {
		return new ItemBuilder(Material.BLUE_BANNER, ItemTranslation.GUESS_THE_FLAG).build();
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!enabled || currentFlag == null) return;
		event.setCancelled(true);
		handleInput(event);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

		if (label.equalsIgnoreCase("currentflag")) {
			if (currentFlag == null) {
				sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_NOT_STARTED);
			} else {
				sender.sendMessage(Challenges.getInstance().getStringManager().CHALLENGE_PREFIX + Translation.GUESS_THE_FLAG_CURRENTFLAG.get().replace("%flag%", Utils.getEnumName(currentFlag.name())));
			}
			return true;
		}

		// the label can only be "skipflag" here
		setNewFlag();
		return true;

	}

	public enum Flag {

		ANDORRA("ad"),
		UNITED_ARAB_EMIRATES("ae"),
		AFGHANISTAN("af"),
		ANTIGUA_AND_BARBUDA("ag", a("antigua", "barbuada")),
		ANGUILLA("ag"),
		ARMENIA("am"),
		NETHERLANDS_ANTILLES("an"),
		ANGOLA("ao"),
		ANTARCTICA("aq"),
		ARGENTINA("ar"),
		AMERICAN_SAMOA("as"),
		AUSTRIA("at", a("Österreich")),
		AUSTRALIA("au", a("Australien")),
		ARUBA("aw"),
		ALAND_ISLANDS("ax"),
		BOSNIA_AND_HERZEGOVINA("ba"),
		BARBADOS("bb"),
		BANGLADESH("bd"),
		BELGIUM("be", a("Belgien")),
		BURKINA_FASO("bf"),
		BULGARIA("bg", a("Bulgarien")),
		BAHRAIN("bh"),
		BURUNDI("bi"),
		BENIN("bj"),
		SAINT_BARTHELEMY("bl"),
		BERMUDA("bm"),
		BRUNEI_DARUSSALAM("bn"),
		BOLVIA("bo"),
		BRAZIL("br", a("Brazilien")),
		BAHAMAS("bs"),
		BHUTAN("bt"),
		BOUVET_ISLAND("bv"),
		BOTSWANA("bw"),
		BELARUS("by"),
		BELIZE("bz"),
		CANADA("ca", a("Kanada")),
		CACOS_ISLANDS("cc"),
		CENTRAL_AFRICAN_REPUBLIC("cf"),
		CONGO("cg"),
		SWITZERLAND("ch", a("Schweiz")),
		COOK_ISLANDS("ck"),
		CHILE("cl"),
		CAMEROON("cm"),
		CHINA("cn"),
		COLOMBIA("co"),
		COSTA_RICA("cr"),
		CUBA("cu", a("Kuba")),
		CAPE_VERDE("cv"),
		CURACAO("cw"),
		CYPRUS("cy"),
		CZECH_REPUBLIC("cz"),
		GERMANY("de", a("Deutschland")),
		DJIBOUTI("dj"),
		DENMARK("dk", a("Dänemark")),
		DOMINICA("dm"),
		DOMINICAN_REPUBLIC("do"),
		ALGERIA("dz"),
		ECUADOR("ec"),
		ESTONIA("ee"),
		EGYPT("eg"),
		WESTERN_SAHARA("eh"),
		ERITREA("er"),
		SPAIN("es", a("Spanien")),
		ETHIOPIA("et"),
		FINLAND("fi", a("Finnland")),
		FALKLAND_ISLANDS("fk"),
		FAROE_ISLANDS("fo"),
		FRANCE("fr", a("Frankreich")),
		GABON("ga"),
		UNITED_KINGDOM("gb", a("Britanien", "UK", "Groß Britanien")),
		GEORGIA("ge"),
		FRENCH_GUIANA("gf"),
		GUERNSEY("gg"),
		GHANA("gh"),
		GREENLAND("gl"),
		GAMBIA("gm"),
		GUINEA("gn"),
		GUADELOUPE("gp"),
		GREECE("gr"),
		GUATEMALA("gt"),
		GUAM("gu"),
		GUYANA("gy"),
		HONG_KONG("hk"),
		HUNGARY("hu"),
		INDONESIA("id"),
		IRELAND("ie"),
		ISRAEL("il"),
		ISLE_OF_MAN("im"),
		INDIA("in"),
		ICELAND("is"),
		ITALY("it"),
		JERSEY("je"),
		JAMAICA("jm"),
		JORDAN("jo"),
		JAPAN("jp"),
		KYRGYZSTAN("kg"),
		CAMBODIA("kh"),
		KIRIBATI("ki"),
		SAINT_KITTS_AND_NEVIS("kn"),
		KOREA("kr"),
		KUWAIT("kw"),
		SAINT_LUCIA("lc"),
		LIECHTENSTEIN("li"),
		LIBERIA("lr"),
		LESOTHO("ls"),
		LUXEMBOURG("lu"),
		LIBYA("ly"),
		MOROCCO("ma"),
		MONACO("mc"),
		MOLDOVA("md"),
		MONTENEGRO("me"),
		SAINT_MARTIN("mf"),
		MADAGASCAR("mg"),
		MARSHALL_ISLANDS("mh"),
		MACEDONIA("mk"),
		MALI("ml"),
		MYANMAR("mm"),
		MONGOLIA("mn"),
		MONTSERRAT("ms"),
		MALTA("mt"),
		MAURITIUS("mu"),
		MALDIVES("mv"),
		MALAWI("mw"),
		NAMIBIA("na"),
		NEW_CALEDONIA("nc"),
		NIGERIA("ng"),
		NORWAY("no", a("Norwegen")),
		NEW_ZEALAND("nz", a("Neuseeland")),
		OMAN("om"),
		PERU("pe"),
		FRENCH_POLYNESIA("pf"),
		PAPUA_NEW_GUINEA("pg", a("Papa neu guena")),
		PAKISTAN("pk"),
		POLAND("pl", a("Polen")),
		PUERTO_RICO("pr"),
		PALESTINE("ps"),
		PALAU("pw"),
		PARAGUAY("py"),
		ROMANIA("ro"),
		RUSSIAN_FEDERATION("fr", a("Russland")),
		SEYCHELLES("sc"),
		SUDAN("sd"),
		SINGAPORE("sg"),
		SLOVAKIA("sk"),
		SIERRA_LEONE("sl"),
		SOMALIA("so"),
		SURINAME("sr"),
		SOUTH_SUDAN("ss"),
		EL_SALVADOR("sv"),
		CHAD("td"),
		THAILAND("th"),
		TOKELAU("tk"),
		TIMOR_LESTE("tl"),
		TUNISIA("tn", a("Tunesien")),
		TONGA("to"),
		TURKEY("tr", a("Türkei")),
		TRINIDAD_AND_TOBAGO("tt", a("Trinida", "Tobago")),
		TAIWAN("tw"),
		TANZANIA("tz"),
		UKRAINE("ua", a("Ukraine")),
		UGANDA("ug"),
		UNITED_STATES("us", a("USA", "US", "America", "Amerika", "Vereinigte Staaten")),
		VIETNAM("vn"),
		VANUATU("vu"),
		YEMEN("ye");


		private final String flagCode;
		private final String[] names;

		Flag(String flagCode) {
			this.flagCode = flagCode;
			names = new String[] { this.name().toLowerCase().replace("_", " ") };
		}

		Flag(String flagCode, String[] names) {
			this.flagCode = flagCode;
			this.names = new String[names.length + 1];
			for (int i = 0; i < names.length; i++) {
				this.names[i] = names[i].toLowerCase();
			}
			this.names[names.length] = this.name().toLowerCase().replace("_", " ");
		}

		public String getFlagCode() {
			return flagCode;
		}

		public String getFlagURL() {
			return getFlagURL(flagCode);
		}

		public String getName() {
			return Utils.getEnumName(this.name());
		}

		public boolean isOK(String comparision) {

			double best = 0;
			for (String currentName : names) {
				double percentage = StringUtils.compare(currentName, comparision, false, false);
				if (percentage > best) best = percentage;
			}

			return best > 80;

		}

		public static String getFlagURL(String flagCode) {
			return "https://www.countryflags.io/" + flagCode + "/flat/64.png";
		}

	}

}
