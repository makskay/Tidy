package me.makskay.bukkit.tidy;

import java.util.HashSet;

import org.bukkit.Location;

public class IssueManager {
	private HashSet<IssueReport> cachedIssues;
	private int nextUid;
	private TidyPlugin plugin;
	
	public IssueManager(TidyPlugin plugin) {
		this.plugin = plugin;
	}
	
	public void registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.add(issue); // pull this issue into cached memory
		nextUid++; // the next issue will be assigned the next greatest integer as a UID
	}
	
	public void purge() {
		// TODO Kill every closed non-sticky issue
		// TODO Rebase UIDs
	}
}
