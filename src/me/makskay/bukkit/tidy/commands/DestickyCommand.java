package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.ChatColor;
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
			sender.sendMessage(ChatColor.RED + "\"" +  args[0] + "\" isn't a valid issue ID number");
			return true;
		}
		
		IssueReport issue = issueManager.getIssue(uid);
		if (issue == null) {
			sender.sendMessage(ChatColor.RED + "Couldn't find issue #" + uid);
			return true;
		}
		
		String comment = "";
		for (int i = 1; i < args.length; i++) {
			comment = comment + args[i] + " ";
		}
		
		if (issue.markAsNonSticky(sender.getName(), comment)) {
			sender.sendMessage(ChatColor.GRAY + "De-stickied issue #" + uid);
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Issue #" + uid + " is not stickied");
		return true;
	}
}
