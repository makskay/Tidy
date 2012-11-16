package me.makskay.bukkit.tidy.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.TidyPlugin;

public class NotifyServerStaffTask implements Runnable {
	private IssueManager issueManager;
	
	public NotifyServerStaffTask(TidyPlugin plugin) {
		this.issueManager = plugin.getIssueManager();
	}
	
	public void run() {
		int count = 0;
		for (IssueReport issue : issueManager.getCachedIssues()) {
			if (issue.isOpen()) {
				count++; // TODO this count is EFFING WRONG 
			}
		}
		
		if (count != 0) {
			Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "There are currently " + ChatColor.GREEN + count + 
					TidyPlugin.NEUTRAL_COLOR + " unresolved issues", "tidy.staff");
		}
	}

}
