package me.makskay.tidy.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

public class NotifyServerStaffTask implements Runnable {
	private IssueManager issueManager;
	
	public NotifyServerStaffTask(TidyPlugin plugin) {
		this.issueManager = plugin.getIssueManager();
	}
	
	public void run() {
		int count = 0;
		for (IssueReport issue : issueManager.getCachedIssues()) {
			if (issue.isOpen()) {
				count++;
			}
		}
		
		if (count == 1) {
			Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "There is currently " + ChatColor.GREEN + count + 
					TidyPlugin.NEUTRAL_COLOR + " unresolved issue", "tidy.staff");
		}
		
		else if (count > 0) {
			Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "There are currently " + ChatColor.GREEN + count + 
					TidyPlugin.NEUTRAL_COLOR + " unresolved issues", "tidy.staff");
		}
	}

}
