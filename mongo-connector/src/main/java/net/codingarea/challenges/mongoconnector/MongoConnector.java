package net.codingarea.challenges.mongoconnector;

import net.anweisen.utilities.database.internal.mongodb.MongoDBDatabase;
import net.codingarea.challenges.plugin.Challenges;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.0
 */
public final class MongoConnector extends JavaPlugin {

	@Override
	public void onLoad() {
		Challenges.getInstance().getDatabaseManager().registerDatabase("mongodb", MongoDBDatabase.class, this);
	}

}
