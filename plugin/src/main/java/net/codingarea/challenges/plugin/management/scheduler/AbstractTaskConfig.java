package net.codingarea.challenges.plugin.management.scheduler;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public abstract class AbstractTaskConfig {

	protected final boolean async;

	public AbstractTaskConfig(boolean async) {
		this.async = async;
	}

	public boolean isAsync() {
		return async;
	}

}
