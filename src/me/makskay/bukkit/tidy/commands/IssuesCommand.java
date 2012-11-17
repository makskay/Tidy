package me.makskay.bukkit.tidy.commands;

import java.util.ArrayList;
import java.util.List;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IssuesCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public IssuesCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		int pageNumber = 0;
		int issuesPerPage = 8; // TODO this should be configurable
		boolean searchAllIssues = false;
		
		if (args.length == 0) {
			// do nothing
		}
		
		else if (args.length == 1) {
			try {
				pageNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				if (!args[0].equals("-all")) {
					sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid page number");
					return true;
				}
				
				searchAllIssues = true;
			}
		}
		
		else if (args.length == 2) {
			try {
				pageNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid page number");
				return true;
			}
			
			if (!args[1].equals("-all")) {
				sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[1] + "\" isn't a valid flag");
				return true;
			}
		}
		
		else {
			return false;
		}
		
		int startIndex = pageNumber * issuesPerPage;
		List<IssueReport> issues = issueManager.getCachedIssues();
		
		if (searchAllIssues) { // generate a list of ALL issues (resolved included) at the specified page
			if (sender.hasPermission("tidy.staff")) {
				try {
					issues = issues.subList(startIndex, startIndex + issuesPerPage - 1);
				}
				
				catch (IndexOutOfBoundsException ex) {
					issues = issues.subList(0, issues.size());
				}
			}
			
			else {
				issues = new ArrayList<IssueReport>();
				for (IssueReport issue : issueManager.getCachedIssues()) {
					if (issues.size() > issuesPerPage) {
						break;
					}
					
					if (issue.canBeEditedBy(sender)) {
						issues.add(issue);
					}
				}
			}
		}
		
		else { // generate a list of OPEN issues (iterate over the cache and check for !resolved) at the specified page
			issues = new ArrayList<IssueReport>();
			List<IssueReport> cachedIssues = issueManager.getCachedIssues();
			int cacheSize = cachedIssues.size();
			
			for (IssueReport issue : cachedIssues) {
				int size = issues.size();
				if (size > issuesPerPage || size == cacheSize) {
					break;
				}
				
				if (issue.isOpen() && issue.canBeEditedBy(sender)) {
					issues.add(issue);
				}
			}
		}
		
		if (issues.size() == 0) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "No issues matching the search criteria could be found");
			if (!searchAllIssues) {
				sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + 
						"Consider broadening the search to include resolved issues: /issues -all");
			}
			return true;
		}
		
		sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + "-- Issues --");
		for (IssueReport issue : issues) {
			sender.sendMessage(issue.shortSummary());
		}
		return true;
	}
}
