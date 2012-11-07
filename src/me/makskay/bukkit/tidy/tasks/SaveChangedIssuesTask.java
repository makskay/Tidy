/* Smooth, backgrounded saving of changed issues.
 * Every time this task runs, one issue will have changes saved to disk.
 * The frequency with which this task runs can be changed to improve performance.
 */

package me.makskay.bukkit.tidy.tasks;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.TidyPlugin;

public class SaveChangedIssuesTask implements Runnable {
	private IssueManager issueManager;
	private TidyPlugin plugin;
	
	public SaveChangedIssuesTask(TidyPlugin plugin) {
		this.issueManager = plugin.getIssueManager();
		this.plugin       = plugin;
	}
	
	public void run() {
		IssueReport issue = null;
		for (IssueReport iss : issueManager.getCachedIssues()) {
			if (iss.hasChanged()) {
				issue = iss;
				break;
			}
		}
		
		if (issue == null) {
			return; // there were no issues with changes waiting to be saved
		}
		
		String path = "issues." + issue.getUid() + ".";
		
		plugin.issuesYml.getConfig().set(path + "open", issue.isOpen());
		plugin.issuesYml.getConfig().set(path + "sticky", issue.isSticky());
		plugin.issuesYml.getConfig().set(path + "comments", issue.getComments());
		plugin.issuesYml.getConfig().set(path + "timestamp", System.currentTimeMillis());
		plugin.issuesYml.saveConfig();
		plugin.issuesYml.reloadConfig();
	}
}
