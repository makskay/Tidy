package me.makskay.bukkit.tidy;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private TidyPlugin plugin;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin) {
		this.plugin  = plugin;
		this.nextUid = plugin.issuesYml.getConfig().getInt("NextIssueUID");
		if (nextUid == 0) {
			plugin.getLogger().warning("Your issues.yml file is corrupted. Deleting it and reloading the server is recommended.");
		}
		
		for (int uid : plugin.issuesYml.getConfig().getIntegerList("OpenIssueIDs")) {
			try {
				String path = "issues." + uid + ".";
				String ownerName      = plugin.issuesYml.getConfig().getString(path + "owner");
				String description    = plugin.issuesYml.getConfig().getString(path + "description");
				String[] locAsString  = plugin.issuesYml.getConfig().getString(path + "location").split(",");
				Location loc          = new Location(Bukkit.getWorld(locAsString[0]), Integer.parseInt(locAsString[1]), 
						Integer.parseInt(locAsString[2]), Integer.parseInt(locAsString[3]));
				List<String> comments = plugin.issuesYml.getConfig().getStringList(path + "comments");
				boolean isSticky      = plugin.issuesYml.getConfig().getBoolean(path + "sticky");
				IssueReport issue = new IssueReport(ownerName, description, uid, loc, comments, true, isSticky);
				cachedIssues.put(uid, issue);
			} catch (Exception e) {
				continue; // the issue entry is corrupt and can't be deciphered -- move on to the next one
			}
		}
	}
	
	public void registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.put(nextUid, issue); // pull this issue into cached memory
		incrementNextUid(); // the next issue will be assigned the next greatest integer as a UID
	}
	
	public void purge() {
		// TODO Kill every closed non-sticky issue
		// TODO Rebase UIDs
	}
	
	public IssueReport getIssue(int uid) {
		IssueReport issue = cachedIssues.get(uid);
		if (issue != null) {
			return issue;
		}
		
		// TODO Try to create an issue object from data in issues.yml
		
		return null; // if no issue with that UID exists
	}
	
	private void incrementNextUid() {
		nextUid++;
		plugin.issuesYml.getConfig().set("NextIssueUID", nextUid);
		plugin.issuesYml.saveConfig();
	}
}
