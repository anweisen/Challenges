package net.codingarea.challenges.plugin.utils.animation;

import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class AnimatedInventory {

	private final List<AnimationFrame> frames = new ArrayList<>();
	private final InventoryHolder holder;
	private final int size;
	private final String title;
	private SoundSample frameSound = SoundSample.CLICK, endSound = SoundSample.OPEN;
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
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(plugin, () -> open(player, plugin));
			return;
		}

		Inventory inventory = createInventory();
		player.openInventory(inventory);

		AtomicInteger index = new AtomicInteger();
		Bukkit.getScheduler().runTaskTimerAsynchronously(Challenges.getInstance(), task -> {

			if (index.get() >= frames.size() || player.getOpenInventory().getTopInventory() != inventory) {
				task.cancel();
				return;
			}

			AnimationFrame frame = frames.get(index.get());
			inventory.setContents(frame.getContent());

			if (index.get() == frames.size() - 1 && endSound != null) endSound.play(player);
			else if (frameSound != null && frame.shouldPlaySound()) frameSound.play(player);

			index.incrementAndGet();

		}, 0, frameDelay);

	}

	public void openNotAnimated(@Nonnull Player player, boolean playSound, @Nonnull JavaPlugin plugin) {
		if (!Bukkit.isPrimaryThread()) {
			Bukkit.getScheduler().runTask(plugin, () -> openNotAnimated(player, playSound, plugin));
			return;
		}

		Inventory inventory = createInventory();
		player.openInventory(inventory);

		if (!frames.isEmpty()) {
			AnimationFrame frame = getLastFrame();
			inventory.setContents(frame.getContent());
		}

		if (playSound && endSound != null) endSound.play(player);

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
	public AnimationFrame getOrCreateFrame(int index) {
		while (frames.size() <= index) {
			cloneLastAndAdd();
		}
		return getFrame(index);
	}

	@Nonnull
	public AnimationFrame cloneAndAdd(int index) {
		AnimationFrame frame = getFrame(index).clone();
		addFrame(frame);
		return frame;
	}

	@Nonnull
	public AnimationFrame getLastFrame() {
		if (frames.isEmpty()) throw new IllegalStateException("Frames are empty");
		return getFrame(frames.size() - 1);
	}

	@Nonnull
	public AnimationFrame cloneLastAndAdd() {
		AnimationFrame frame = getLastFrame().clone();
		addFrame(frame);
		return frame;
	}

	@Nonnull
	public AnimatedInventory setEndSound(@Nullable SoundSample endSound) {
		this.endSound = endSound;
		return this;
	}

	@Nonnull
	public AnimatedInventory setFrameSound(@Nullable SoundSample frameSound) {
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
