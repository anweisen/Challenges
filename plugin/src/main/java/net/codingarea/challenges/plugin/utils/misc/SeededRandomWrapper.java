package net.codingarea.challenges.plugin.utils.misc;

import java.util.Random;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public class SeededRandomWrapper extends Random {

	protected long seed;

	public SeededRandomWrapper() {
		super();
	}

	public SeededRandomWrapper(long seed) {
		super(seed);
		this.seed = seed;
	}

	@Override
	public void setSeed(long seed) {
		super.setSeed(seed);
		this.seed = seed;
	}

	public long getSeed() {
		return seed;
	}

}
