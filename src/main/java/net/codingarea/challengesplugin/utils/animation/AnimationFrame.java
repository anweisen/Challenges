package net.codingarea.challengesplugin.utils.animation;

import org.bukkit.inventory.ItemStack;

/**
 * @author anweisen
 * Challenges developed on 07-29-2020
 * https://github.com/anweisen
 */

public class AnimationFrame {

	private boolean sound = true;
	private int size;
	private ItemStack[] contents;

	public AnimationFrame(ItemStack[] contents) {
		setContents(contents);
	}

	public AnimationFrame(int size) {
		this.size = size;
		setContents(null);
	}

	public AnimationFrame setPlaySound(boolean play) {
		this.sound = play;
		return this;
	}

	public AnimationFrame setItem(int slot, ItemStack item) {
		contents[slot] = item;
		return this;
	}

	public AnimationFrame setContents(ItemStack[] contents) {

		if (contents == null) {
			this.contents = new ItemStack[size];
		} else {
			this.size = contents.length;
			this.contents = contents;
		}

		return this;

	}

	public AnimationFrame fill(ItemStack item) {
		for (int i = 0; i < size && i < contents.length; i++) {
			contents[i] = item;
		}
		return this;
	}

	public ItemStack[] getContents() {
		return contents;
	}

	public boolean shouldPlaySound() {
		return sound;
	}

	@Override
	public AnimationFrame clone() {
		return new AnimationFrame(size).setContents(contents.clone());
	}

}
