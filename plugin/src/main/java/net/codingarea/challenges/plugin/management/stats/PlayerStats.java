package net.codingarea.challenges.plugin.management.stats;

import net.anweisen.utilities.commons.config.Document;
import net.anweisen.utilities.commons.config.document.GsonDocument;
import net.codingarea.challenges.plugin.utils.logging.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class PlayerStats {

	private final Map<Statistic, Double> values = new HashMap<>();

	public PlayerStats(@Nonnull Document document) {
		for (Statistic statistic : Statistic.values()) {
			values.put(statistic, document.getDouble(statistic.name()));
		}
	}

	public PlayerStats() {
	}

	public void incrementStatistic(@Nonnull Statistic statistic, double amount) {
		Logger.debug("Incrementing statistic " + statistic + " by " + amount);
		double value = values.getOrDefault(statistic, 0d);
		values.put(statistic, value + amount);
	}

	@Nonnull
	public Document asDocument() {
		Document document = new GsonDocument();
		for (Entry<Statistic, Double> entry : values.entrySet()) {
			document.set(entry.getKey().name(), entry.getValue());
		}
		return document;
	}

	public double getStatisticValue(@Nonnull Statistic statistic) {
		return values.getOrDefault(statistic, 0d);
	}

	@Override
	public String toString() {
		return "PlayerStats" + values;
	}

}
