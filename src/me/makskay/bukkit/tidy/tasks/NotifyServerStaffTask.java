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
				count++;
			}
		}
		
		if (count != 0) {
			Bukkit.broadcast(ChatColor.GRAY + "There are currently " + ChatColor.GREEN + count + 
					ChatColor.GRAY + " unresolved issues", "tidy.staff");
		}
	}

}
