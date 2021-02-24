package net.codingarea.challenges.plugin.utils.animation;

import net.codingarea.challenges.plugin.management.player.ChallengePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class SoundSample {

	public static final SoundSample
			CLICK           = new SoundSample().addSound(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.5f),
			BASS_OFF        = new SoundSample().addSound(Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F),
			BASS_ON         = new SoundSample().addSound(Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F),
			PLING           = new SoundSample().addSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1),
			KLING           = new SoundSample().addSound(Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 2),
			PLOP            = new SoundSample().addSound(Sound.ENTITY_CHICKEN_EGG, 1, 2),
			DEATH           = new SoundSample().addSound(Sound.ENTITY_BAT_DEATH, 0.7F),
			TELEPORT        = new SoundSample().addSound(Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.9F),
			OPEN            = new SoundSample().addSound(KLING).addSound(PLOP),
			DRAGON_BREATH   = new SoundSample().addSound(Sound.ENTITY_ENDER_DRAGON_GROWL, 0.8F);

	private final class SoundFrame {

		private final float pitch, volume;
		private final Sound sound;

		public SoundFrame(@Nonnull Sound sound, float volume, float pitch) {
			this.volume = volume;
			this.pitch = pitch;
			this.sound = sound;
		}

		public SoundFrame(@Nonnull Sound sound, float volume) {
			this(sound, volume, 1);
		}

		public void play(@Nonnull Player player, @Nonnull Location location) {
			player.playSound(location, sound, volume, pitch);
		}

		public float getPitch() {
			return pitch;
		}

		public float getVolume() {
			return volume;
		}

		@Nonnull
		public Sound getSound() {
			return sound;
		}

	}

	private final List<SoundFrame> frames = new ArrayList<>();

	@Nonnull
	public SoundSample addSound(@Nonnull Sound sound, float volume, float pitch) {
		frames.add(new SoundFrame(sound, volume, pitch));
		return this;
	}

	@Nonnull
	public SoundSample addSound(@Nonnull Sound sound, float volume) {
		frames.add(new SoundFrame(sound, volume));
		return this;
	}

	@Nonnull
	public SoundSample addSound(@Nonnull SoundSample sound) {
		frames.addAll(sound.frames);
		return this;
	}

	public void play(@Nonnull ChallengePlayer player) {
		play(player.getPlayer());
	}

	public void play(@Nonnull ChallengePlayer player, @Nonnull Location location) {
		play(player.getPlayer(), location);
	}

	public void play(@Nonnull Player player) {
		play(player, player.getLocation());
	}

	public void play(@Nonnull Player player, @Nonnull Location location) {
		for (SoundFrame frame : frames) {
			frame.play(player, location);
		}
	}

	public void broadcast() {
		Bukkit.getOnlinePlayers().forEach(this::play);
	}

}