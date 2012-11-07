package me.makskay.bukkit.tidy.tasks;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import me.makskay.bukkit.tidy.ConfigAccessor;
import me.makskay.bukkit.tidy.TidyPlugin;

public class KillExpiredIssuesTask implements Runnable { // TODO this needs to be tested, it might hang if there's a bunch of issues
	private ConfigAccessor issuesYml;
	
	public KillExpiredIssuesTask(TidyPlugin plugin) {
		this.issuesYml = plugin.getIssuesYml();
	}

	public void run() {
		FileConfiguration issues = issuesYml.getConfig();
		boolean isOpen   = true;
		boolean isSticky = false;
		String key       = "";
		Set<String> keys = issues.getConfigurationSection("issues").getKeys(false);
		while (isOpen || isSticky) {
			if (!keys.iterator().hasNext()) {
				return;
			}
			
			key      = keys.iterator().next();
			isOpen   = issues.getBoolean("issues." + key + ".open");
			isSticky = issues.getBoolean("issues." + key + ".sticky");
		}
		
		long currentTime = System.currentTimeMillis();
		long deltaTime = currentTime - issues.getLong("issues." + key + ".timestamp");
		if (deltaTime > 259200000) { // 259200000 == 3 days (TODO make the number of days configurable)
			issuesYml.getConfig().set("issues." + key, null);
		}
	}

}
