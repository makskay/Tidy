package me.makskay.bukkit.tidy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private StorageManager storageManager;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin) {
		this.storageManager = plugin.getStorageManager();
		this.cachedIssues = storageManager.loadIssues();
		this.nextUid = storageManager.getNextUid();
		if (nextUid == 0) {
			plugin.getLogger().warning("Your issues.yml file is corrupted. Deleting it and reloading the server is recommended.");
		}
	}
	
	public IssueReport registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.put(nextUid, issue); // pull this issue into cached memory
		incrementNextUid(); // the next issue will be assigned the next greatest integer as a UID
		return issue;
	}
	
	public void purge() { // mark all resolved, non-sticky issues for deletion
		for (IssueReport issue : cachedIssues.values()) {
			if (!issue.isOpen() && !issue.isSticky()) {
				issue.setShouldBeDeleted(true);
				issue.setHasChanged(true);
			}
		}
		
		// TODO Rebase UIDs
	}
	
	public IssueReport getIssue(int uid) {
		return cachedIssues.get(uid);
	}
	
	private void incrementNextUid() {
		nextUid++;
		storageManager.setNextUid(nextUid);
	}

	public List<IssueReport> getCachedIssues() { // TODO this will return numerical order in some cases, but not all
		ArrayList<IssueReport> issues = new ArrayList<IssueReport>();
		for (IssueReport issue : cachedIssues.values()) {
			issues.add(issue);
		}
		return issues;
	}
}
