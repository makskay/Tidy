package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpmeCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public HelpmeCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (player == null) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "Only a player may file a help request");
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(TidyPlugin.ERROR_COLOR + "Please fill out a description of your problem.");
			player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Example usage: /helpme I've been griefed");
			return true;
		}
		
		String description = "";
		for (String word : args) {
			description = description + word + " ";
		}
		
		issueManager.registerIssue(player.getName(), description.trim(), player.getLocation());
		player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Your help request has been filed and will be investigated as soon as possible");
		return true;
	}
}
