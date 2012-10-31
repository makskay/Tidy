package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IssueCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public IssueCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		return false; // TODO Fill out with actual command implementation
	}
}
