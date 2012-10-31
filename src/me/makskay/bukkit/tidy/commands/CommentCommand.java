package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommentCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public CommentCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length < 2) {
			return false; // you need at least an issue ID and one word of comment material
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
		
		Player player = (Player) sender;
		if ((player != null) && (!issue.canBeEditedBy(player))) {
			sender.sendMessage(ChatColor.RED + "You're not permitted to comment on issue #" + uid);
			return true;
		}
		
		String comment = "";
		for (int i = 1; i < args.length; i++) {
			comment = comment + args[i] + " ";
		}
		
		issue.addComment(sender.getName(), comment.trim());
		sender.sendMessage(ChatColor.GRAY + "Added your comment to issue #" + uid);
		return true;
	}
}
