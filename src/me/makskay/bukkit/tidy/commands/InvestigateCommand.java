package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.IssueReport;
import me.makskay.bukkit.tidy.PlayerManager;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvestigateCommand implements CommandExecutor {
	private IssueManager issueManager;
	private PlayerManager playerManager;
	
	public InvestigateCommand(TidyPlugin plugin) {
		issueManager  = plugin.getIssueManager();
		playerManager = plugin.getPlayerManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		if (args.length != 1) {
			return false; // needs exactly one argument: issue ID
		}
		
		Player player = (Player) sender;
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Only a player may investigate issues");
			return true;
		}
		
		int uid;
		try {
			uid = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			if (!args[0].equalsIgnoreCase("-end")) {
				sender.sendMessage(ChatColor.RED + "\"" +  args[0] + "\" isn't a valid issue ID number");
				return true;
			}
			
			// handle a request to end the issue investigation
			
			Location location = playerManager.getSavedLocationOf(player);
			if (location == null) {
				player.sendMessage(ChatColor.RED + "You aren't currently investigating any issues");
				return true;
			}
			
			player.teleport(location);
			playerManager.clearSavedLocationOf(player);
			player.sendMessage(ChatColor.GRAY + "Issue investigation ended.");
			return true;
		}
		
		IssueReport issue = issueManager.getIssue(uid);
		if (issue == null) {
			player.sendMessage(ChatColor.RED + "Couldn't find issue #" + uid);
			return true;
		}
		
		playerManager.saveLocationOf(player);
		player.teleport(issue.getLocation());
		player.sendMessage(ChatColor.GRAY + "Now investigating issue #" + uid + ".");
		player.sendMessage(ChatColor.GRAY + "Type /investigate -end when you're done to return to where you left off.");
		return true;
	}
}
