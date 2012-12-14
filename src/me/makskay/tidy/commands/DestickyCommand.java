package me.makskay.tidy.commands;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DestickyCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public DestickyCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length < 2) {
			return false; // you need an issue ID and at least one word of reasoning
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
		
		String comment = "";
		for (int i = 1; i < args.length; i++) {
			comment = comment + args[i] + " ";
		}
		
		if (issue.markAsNonSticky(sender.getName(), comment)) {
			sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + "De-stickied issue #" + uid);
			return true;
		}
		
		sender.sendMessage(TidyPlugin.ERROR_COLOR + "Issue #" + uid + " isn't stickied!");
		return true;
	}
}
