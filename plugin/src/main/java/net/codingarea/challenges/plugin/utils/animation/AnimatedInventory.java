package net.codingarea.challenges.plugin.utils.animation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class AnimatedInventory {

	private InventoryHolder holder;
	private final int size;
	private final String title;
	private List<AnimationFrame> frames = new ArrayList<>();
	private SoundSample frameSound, endSound;
	private int frameDelay = 1;

	public AnimatedInventory(@Nonnull String title, int size) {
		this(title, size, null);
	}

	public AnimatedInventory(@Nonnull String title, int size, @Nullable InventoryHolder holder) {
		this.title = title;
		this.size = size;
		this.holder = holder;
	}

	public void open(@Nonnull Player player, @Nonnull JavaPlugin plugin) {

		Inventory inventory = createInventory();
		player.openInventory(inventory);

		for (int i = 0; i < frames.size(); i++) {

			AnimationFrame frame = frames.get(i);

			int I = i; // effective final clone
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {

				if (!player.getOpenInventory().getTitle().equals(title)) return;

				inventory.setContents(frame.getContent());

				if (frameSound != null && frame.shouldPlaySound()) frameSound.play(player);
				if (I == frames.size() - 1 && endSound != null) endSound.play(player);

			}, (long) frameDelay * i);

		}

	}

	public void openNotAnimated(@Nonnull Player player, boolean playSound) {

		Inventory inventory = createInventory();
		if (!frames.isEmpty()) {
			AnimationFrame frame = frames.get(frames.size() - 1);
			inventory.setContents(frame.getContent());
		}

		if (playSound && endSound != null)
			endSound.play(player);

		player.openInventory(inventory);

	}

	@Nonnull
	public AnimatedInventory addFrame(@Nonnull AnimationFrame frame) {
		if (size != frame.getSize()) throw new IllegalArgumentException("AnimationFrame must have the same size (Expected " + size + "; Got " + frame.getSize() + ")");
		frames.add(frame);
		return this;
	}

	@Nonnull
	public AnimationFrame createAndAdd() {
		AnimationFrame frame = new AnimationFrame(size);
		addFrame(frame);
		return frame;
	}

	@Nonnull
	public AnimationFrame getFrame(int index) {
		return frames.get(index);
	}

	@Nonnull
	public AnimationFrame cloneAndAdd(int index) {
		AnimationFrame frame = getFrame(index).clone();
		addFrame(frame);
		return frame;
	}

	@Nonnull
	public AnimationFrame cloneLastAndAdd() {
		AnimationFrame frame = getFrame(frames.size() - 1).clone();
		addFrame(frame);
		return frame;
	}

	@Nonnull
	public AnimatedInventory setEndSound(SoundSample endSound) {
		this.endSound = endSound;
		return this;
	}

	@Nonnull
	public AnimatedInventory setFrameSound(SoundSample frameSound) {
		this.frameSound = frameSound;
		return this;
	}

	@Nonnull
	public AnimatedInventory setFrameDelay(int delay) {
		if (delay < 1) throw new IllegalArgumentException("Delay cannot be smaller than 1");
		this.frameDelay = delay;
		return this;
	}

	@Nonnull
	private Inventory createInventory() {
		return Bukkit.createInventory(holder, size, title);
	}

}
