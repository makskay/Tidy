package me.makskay.tidy.commands;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
		if (!(sender instanceof Player)) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "Only a player may file an issue report");
			return true;
		}
		
		Player player = (Player) sender;
		if (args.length == 0) {
			player.sendMessage(TidyPlugin.ERROR_COLOR + "Please repeat the command, including a description your problem.");
			player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Example usage: /helpme I've been griefed");
			return true;
		}
		
		String description = "";
		for (String word : args) {
			description = description + word + " ";
		}
		
		String playername = player.getName();
		IssueReport issue = issueManager.registerIssue(playername, description.trim(), player.getLocation());
		player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Your issue report (" + TidyPlugin.UNRESOLVED_COLOR + issue.getUid() + 
				TidyPlugin.NEUTRAL_COLOR + ") has been filed and will be investigated as soon as possible.");
		
		for (Player staffer : Bukkit.getOnlinePlayers()) {
			if (!staffer.hasPermission("tidy.staff") || staffer.equals(player)) {
				continue;
			}
			
			staffer.playEffect(staffer.getLocation(), Effect.CLICK2, 0);
			staffer.sendMessage(TidyPlugin.PLAYERNAME_COLOR + playername + TidyPlugin.NEUTRAL_COLOR + " just filed a new issue report:");
			staffer.sendMessage(issue.shortSummary());
		}
		
		issue.setHasChanged(true);
		return true;
	}
}
