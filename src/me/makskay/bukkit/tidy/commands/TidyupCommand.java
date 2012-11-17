package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TidyupCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public TidyupCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length != 0) {
			return false; // you need an issue ID and at least one word of reasoning
		}
		
		issueManager.purge();
		sender.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Marked all resolved issues for deletion");
		return true;
	}
}
