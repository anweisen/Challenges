package net.codingarea.challengesplugin.utils.animation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anweisen
 * Challenges developed on 07-29-2020
 * https://github.com/anweisen
 */

public class InventoryAnimation {

	private List<AnimationFrame> frames;

	public InventoryAnimation() {
		frames = new ArrayList<>();
	}

	public InventoryAnimation(List<AnimationFrame> frames) {
		if (frames == null) {
			this.frames = new ArrayList<>();
		} else {
			this.frames = frames;
		}
	}

	public List<AnimationFrame> getFrames() {
		return frames;
	}

	public InventoryAnimation setFrames(List<AnimationFrame> frames) {

		if (frames == null) {
			this.frames = new ArrayList<>();
		} else {
			this.frames = frames;
		}

		return this;

	}

	public AnimationFrame getFrame(int frame) {
		return frames.get(frame);
	}

}
