package me.makskay.tidy.commands;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IssueCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public IssueCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length != 1) {
			return false;
		}
		
		int uid;
		try {
			uid = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid issue ID number!");
			return true;
		}
		
		IssueReport issue = issueManager.getIssue(uid);
		if (issue == null) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "Couldn't find issue #" + uid);
			return true;
		}
		
		if (!issue.canBeEditedBy(sender)) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "You're not permitted to view details of that issue!");
			return true;
		}
		
		for (String line : issue.fullSummary()) {
			sender.sendMessage(line);
		}
		
		return true;
	}
}
