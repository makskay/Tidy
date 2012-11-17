package me.makskay.bukkit.tidy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private TidyPlugin plugin;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin) {
		FileConfiguration issues = plugin.getIssuesFile();
		this.plugin  = plugin;
		this.cachedIssues = new HashMap<Integer, IssueReport>();
		this.nextUid = issues.getInt("NextIssueUID");
		if (nextUid == 0) {
			plugin.getLogger().warning("Your issues.yml file is corrupted. Deleting it and reloading the server is recommended.");
		}
		
		Set<String> keys = issues.getConfigurationSection("issues").getKeys(false);
		for (String key : keys) { // pull all issues on disk into cache
			try {
				getIssue(Integer.parseInt(key));
			} catch (NumberFormatException e) {
				continue;
			}
		}
	}
	
	public IssueReport registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.put(nextUid, issue); // pull this issue into cached memory
		incrementNextUid(); // the next issue will be assigned the next greatest integer as a UID
		return issue;
	}
	
	public void purge() {
		// TODO Kill every closed non-sticky issue
		// TODO Rebase UIDs
	}
	
	public IssueReport getIssue(int uid) {
		FileConfiguration issues = plugin.getIssuesFile();
		IssueReport issue = cachedIssues.get(uid);
		
		if (issue == null) {
			try {
				String path           = "issues." + uid + ".";
				String ownerName      = issues.getString(path + "owner");
				String description    = issues.getString(path + "description");
				String[] locAsString  = issues.getString(path + "location").split(",");
				Location loc          = new Location(Bukkit.getWorld(locAsString[0]), Integer.parseInt(locAsString[1]), 
						Integer.parseInt(locAsString[2]), Integer.parseInt(locAsString[3]));
				List<String> comments = issues.getStringList(path + "comments");
				boolean isOpen        = issues.getBoolean(path + "open");
				boolean isSticky      = issues.getBoolean(path + "sticky");
				long timestamp        = issues.getLong(path + "timestamp");
				
				issue = new IssueReport(ownerName, description, uid, loc, comments, isOpen, isSticky, timestamp);
				if (issue.isIntact()) { 
					cachedIssues.put(uid, issue);
				}
			}
		
			catch (Exception e) {
				return null; // no issue with that UID exists
			}
		}
		
		return issue;
	}
	
	private void incrementNextUid() {
		nextUid++;
		ConfigAccessor issuesYml = plugin.getIssuesYml();
		issuesYml.getConfig().set("NextIssueUID", nextUid);
		issuesYml.saveConfig();
	}

	public List<IssueReport> getCachedIssues() { // TODO this will return numerical order in some cases, but not all
		ArrayList<IssueReport> issues = new ArrayList<IssueReport>();
		for (IssueReport issue : cachedIssues.values()) {
			issues.add(issue);
		}
		return issues;
	}
}
