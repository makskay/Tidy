package me.makskay.tidy.commands;

import java.util.ArrayList;
import java.util.List;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IssuesCommand implements CommandExecutor {
	private IssueManager issueManager;
	private final int ISSUES_PER_PAGE = 8;
	
	public IssuesCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		int pageNumber = 0;
		boolean searchAllIssues = false;
		
		/*
		 * Parse the input (including page number and flags if applicable.)
		 */
		
		if (args.length == 0) {
			// Do nothing
		}
		
		else if (args.length == 1) {
			try {
				pageNumber = Integer.parseInt(args[0]) - 1;
			} 
			
			catch (NumberFormatException ex) {
				if (args[0].equalsIgnoreCase("-all")) {
					searchAllIssues = true;
				}
				
				else {
					sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid page number!");
					return true;
				}
			}
		}
		
		else if (args.length == 2) {
			try {
				pageNumber = Integer.parseInt(args[0]) - 1;
			} 
			
			catch (NumberFormatException ex) {
				sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid page number!");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("-all")) {
				searchAllIssues = true;
			}
			
			else {
				sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[1] + "\" isn't a valid flag!");
				return true;
			}
		}
		
		else {
			return false;
		}
		
		/*
		 * Generate a list of issues matching the search query.
		 */
		
		int startIndex = pageNumber * ISSUES_PER_PAGE;
		pageNumber++; // Label "page 0" (array indices 0-7) as page 1 and so forth
		
		List<IssueReport> allIssues = issueManager.getCachedIssues();
		List<IssueReport> searchResults = new ArrayList<IssueReport>();
		
		if (searchAllIssues) {
			if (sender.hasPermission("tidy.staff")) {
				searchResults.addAll(allIssues);
			}
			
			else {
				for (IssueReport issue : allIssues) {
					if (issue.canBeEditedBy(sender)) {
						searchResults.add(issue);
					}
				}
			}
		}
		
		else {
			for (IssueReport issue : allIssues) {
				if (issue.isOpen() && issue.canBeEditedBy(sender)) {
					searchResults.add(issue);
				}
			}
		}
		
		/*
		 * Appropriately edit down the list of matching issues.
		 */
		
		int countIssuesRetrieved = searchResults.size();
		int countPagesRetrieved = (countIssuesRetrieved % ISSUES_PER_PAGE == 0) ? 
				(countIssuesRetrieved / ISSUES_PER_PAGE) : ((countIssuesRetrieved / ISSUES_PER_PAGE) + 1);
		
		if (countIssuesRetrieved == 0) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "No issues matching the search criteria could be found.");
			
			if (!searchAllIssues) {
				sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + 
						"Consider broadening the search to include resolved issues: /issues [page number] -all");
			}
			
			return true;
		}
		
		try {
			searchResults = searchResults.subList(startIndex, startIndex + ISSUES_PER_PAGE);
		}
		
		catch (Exception ex1) {
			try {
				searchResults = searchResults.subList(startIndex, countIssuesRetrieved);
			}
			
			catch (Exception ex2) {
				searchResults = searchResults.subList(0, countIssuesRetrieved);
			}
		}
		
		/*
		 * Print the edited-down list of search results.
		 */
		
		sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Issues (page " + TidyPlugin.HIGHLIGHT_COLOR + pageNumber + TidyPlugin.NEUTRAL_COLOR + 
				"/" + TidyPlugin.HIGHLIGHT_COLOR + countPagesRetrieved + TidyPlugin.NEUTRAL_COLOR + ")");
		
		for (IssueReport issue : searchResults) {
			sender.sendMessage(issue.shortSummary());
		}
		
		return true;
	}
}
