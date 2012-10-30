package me.makskay.bukkit.tidy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TidyPlugin extends JavaPlugin {
	private ConfigAccessor configYml, issuesYml;
	private IssueManager issueManager;
	
	public void onEnable() {
		configYml = new ConfigAccessor(this, "config.yml");
		issuesYml = new ConfigAccessor(this, "issues.yml");
		issueManager = new IssueManager(this);
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
	}
	
	public IssueManager getIssueManager() {
		return issueManager;
	}
}
