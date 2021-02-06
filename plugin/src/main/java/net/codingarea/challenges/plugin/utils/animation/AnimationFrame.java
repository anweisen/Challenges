package net.codingarea.challenges.plugin.utils.animation;

import net.codingarea.challenges.plugin.utils.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class AnimationFrame implements Cloneable {

	private final ItemStack[] content;
	private boolean sound = true;

	public AnimationFrame(@Nonnull ItemStack[] content) {
		this.content = Arrays.copyOf(content, content.length);
	}

	public AnimationFrame(int size) {
		this.content = new ItemStack[size];
	}

	@Nonnull
	public AnimationFrame fill(@Nonnull ItemStack item) {
		Arrays.fill(content, item);
		return this;
	}

	@Nonnull
	public AnimationFrame setItem(int slot, @Nonnull ItemBuilder item) {
		return setItem(slot, item.build());
	}

	@Nonnull
	public AnimationFrame setItem(int slot, @Nonnull ItemStack item) {
		content[slot] = item;
		return this;
	}

	@Nonnull
	public AnimationFrame setSound(boolean play) {
		this.sound = play;
		return this;
	}

	@Nonnull
	public ItemStack[] getContent() {
		return content;
	}

	public boolean shouldPlaySound() {
		return sound;
	}

	public int getSize() {
		return content.length;
	}

	@Nonnull
	@Override
	public AnimationFrame clone() {
		return new AnimationFrame(content);
	}

}
