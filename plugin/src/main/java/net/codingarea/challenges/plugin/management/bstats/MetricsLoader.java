package net.codingarea.challenges.plugin.management.bstats;

import net.anweisen.utilities.commons.misc.StringUtils;
import net.codingarea.challenges.plugin.Challenges;
import net.codingarea.challenges.plugin.language.loader.LanguageLoader;
import net.codingarea.challenges.plugin.management.bstats.Metrics.AdvancedPie;
import net.codingarea.challenges.plugin.management.bstats.Metrics.SimplePie;
import net.codingarea.challenges.plugin.management.bstats.Metrics.SingleLineChart;

import java.util.HashMap;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 2.0
 */
public class MetricsLoader {

	public void load() {
		Challenges plugin = Challenges.getInstance();

		Metrics metrics = new Metrics(plugin, 11494);
		metrics.addCustomChart(new SimplePie("language", () -> Challenges.getInstance().getLoaderRegistry().getFirstRegistryLoaderByClass(LanguageLoader.class).getLanguage()));
		metrics.addCustomChart(new SimplePie("cloudType", () -> StringUtils.getEnumName(plugin.getCloudSupportManager().getType())));
		metrics.addCustomChart(new SimplePie("databaseType", () -> StringUtils.getEnumName(plugin.getDatabaseManager().getType())));
		metrics.addCustomChart(new SingleLineChart("totalMemory", this::getMemory));
		metrics.addCustomChart(new SingleLineChart("totalCores", () -> Runtime.getRuntime().availableProcessors()));
		metrics.addCustomChart(new AdvancedPie("maxMemory", () -> {
			HashMap<String, Integer> map = new HashMap<>();
			if (Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) return map;
			map.put(getMemory() + "", 1);
			return map;
		}));

	}

	private int getMemory() {
		float maxMemory = (float) Runtime.getRuntime().maxMemory() / 100000000;
		return (int) Math.ceil(maxMemory);
	}

}