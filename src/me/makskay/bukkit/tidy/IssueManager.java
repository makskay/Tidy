package me.makskay.bukkit.tidy;

import java.util.HashMap;

import org.bukkit.Location;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin) {
	}
	
	public void registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.put(nextUid, issue); // pull this issue into cached memory
		nextUid++; // the next issue will be assigned the next greatest integer as a UID
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
}
