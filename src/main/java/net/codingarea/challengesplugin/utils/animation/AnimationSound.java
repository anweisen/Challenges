package net.codingarea.challengesplugin.utils.animation;

import net.codingarea.challengesplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 07-29-2020
 * https://github.com/anweisen
 */

public class AnimationSound {

	public static final AnimationSound STANDARD_SOUND = new AnimationSound(Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 0.5F, 1);
	public static final AnimationSound ON_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 1);
	public static final AnimationSound OFF_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 1);
	public static final AnimationSound PLING_SOUND = new AnimationSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
	public static final AnimationSound OPEN_SOUND = new AnimationSound(Sound.ENTITY_PLAYER_LEVELUP, 0.6F, 2F).addSound(Sound.ENTITY_CHICKEN_EGG, 1F, 2F);
	public static final AnimationSound DEATH_SOUND = new AnimationSound(Sound.ENTITY_BAT_DEATH, 0.7F, 1F);
	public static final AnimationSound TELEPORT_SOUND = new AnimationSound(Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.9F, 1F);
	public static final AnimationSound PLOP_SOUND = new AnimationSound(Sound.ENTITY_CHICKEN_EGG, 1F, 2F);

	private final List<Sound> sound;
	private final List<Float> pitch;
	private final List<Float> volume;

	public AnimationSound(Sound sound, float volume, float pitch) {
		this.sound = new ArrayList<>(); this.sound.add(sound);
		this.volume = new ArrayList<>(); this.volume.add(volume);
		this.pitch = new ArrayList<>(); this.pitch.add(pitch);
	}

	public AnimationSound addSound(Sound sound, float volume, float pitch) {
		this.sound.add(sound);
		this.volume.add(volume);
		this.pitch.add(pitch);
		return this;
	}

	public void play(Player player) {
		if (player == null) return;
		play(player, player.getLocation());
	}

	public void playDelayed(JavaPlugin plugin, Player player, int delay) {
		if (player == null) return;
		Bukkit.getScheduler().runTaskLater(plugin, () -> play(player, player.getLocation()), delay);
	}

	public void broadcast() {
		Utils.forEachPlayerOnline(this::play);
	}

	public void play(Player player, Location location) {
		if (player == null) return;
		for (int i = 0; i < sound.size() && i < volume.size() && i < pitch.size(); i++) {
			player.playSound(location, sound.get(i), volume.get(i), pitch.get(i));
		}
	}

}
