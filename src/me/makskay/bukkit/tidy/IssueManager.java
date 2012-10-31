package me.makskay.bukkit.tidy;

import java.util.HashMap;

import org.bukkit.Location;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private TidyPlugin plugin;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin, int nextUid) {
		this.plugin  = plugin;
		this.nextUid = nextUid;
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
