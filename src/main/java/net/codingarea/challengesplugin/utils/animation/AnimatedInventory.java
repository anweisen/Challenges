package net.codingarea.challengesplugin.utils.animation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author anweisen
 * Challenges developed on 07-29-2020
 * https://github.com/anweisen
 */

public class AnimatedInventory {

	private final InventoryAnimation animation;
	private String title;
	private final int delay;
	private final int size;
	private AnimationSound sound;
	private AnimationSound endSound;

	public AnimatedInventory(@NotNull String title, int delay, int size, AnimationSound sound) {
		this.title = title;
		this.delay = delay;
		this.size = size;
		this.sound = sound;
		this.animation = new InventoryAnimation();
	}

	public void trigger(final Player player, JavaPlugin plugin) {

		if (player == null) throw new IllegalArgumentException("Player cannot be null!");
		if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null!");

		Inventory inventory = Bukkit.createInventory(null, size, title);
		player.openInventory(inventory);

		for (int i = 0; i < animation.getFrames().size(); i++) {

			final AnimationFrame currentFrame = animation.getFrames().get(i);
			final int I = i;

			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {

				if (!player.getOpenInventory().getTitle().equals(title)) return;

				if (currentFrame == null) {
					inventory.setContents(new ItemStack[size]);
				} else {
					inventory.setContents(currentFrame.getContents());
				}

				if (sound != null && (currentFrame == null || currentFrame.shouldPlaySound())) {
					sound.play(player);
				}
				if (endSound != null && I == (animation.getFrames().size() - 1)) {
					endSound.play(player);
				}

			}, getDelayForFrame(i));

		}

	}

	public void openLastFrame(Player player, boolean sound) {

		Inventory inventory = Bukkit.createInventory(null, size, title);
		AnimationFrame lastFrame = animation.getFrames().get(animation.getFrames().size() - 1);
		inventory.setContents(lastFrame.getContents());
		player.openInventory(inventory);

		if (sound) {
			if (endSound != null) {
				endSound.play(player);
			} else if (this.sound != null) {
				this.sound.play(player);
			}
		}

	}

	public AnimatedInventory addFrame(AnimationFrame frame) {
		this.animation.getFrames().add(frame);
		return this;
	}

	public AnimationFrame getFrame(int frame) {
		return this.animation.getFrame(frame);
	}

	public final int getDelayForFrame(int frame) {
		return delay * frame;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {

		if (title == null) throw new IllegalArgumentException("Title cannot be null!");

		this.title = title;

	}

	public InventoryAnimation getAnimation() {
		return animation;
	}

	public void setEndSound(AnimationSound endSound) {
		this.endSound = endSound;
	}
}
