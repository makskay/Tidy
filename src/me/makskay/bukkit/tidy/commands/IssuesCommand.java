package me.makskay.bukkit.tidy.commands;

import me.makskay.bukkit.tidy.IssueManager;
import me.makskay.bukkit.tidy.TidyPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IssuesCommand implements CommandExecutor {
	private IssueManager issueManager;
	
	public IssuesCommand(TidyPlugin plugin) {
		issueManager = plugin.getIssueManager();
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args) {
		int pageNumber = 0;
		boolean searchAllIssues = false;
		
		if (args.length == 0) {
			pageNumber = 0;
		}
		
		else if (args.length == 1) {
			try {
				pageNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				if (!args[0].equals("-all")) {
					sender.sendMessage(ChatColor.RED + "\"" +  args[0] + "\" isn't a valid page number");
					return true;
				}
				
				searchAllIssues = true;
			}
		}
		
		else if (args.length == 2) {
			try {
				pageNumber = Integer.parseInt(args[0]);
			} catch (NumberFormatException ex) {
				sender.sendMessage(ChatColor.RED + "\"" +  args[0] + "\" isn't a valid page number");
				return true;
			}
			
			if (!args[1].equals("-all")) {
				sender.sendMessage(ChatColor.RED + "\"" +  args[1] + "\" isn't a valid flag");
				return true;
			}
		}
		
		else {
			return false;
		}
		
		if (searchAllIssues) {
			// TODO generate a list of ALL issues (resolved included) at the specified page
		}
		
		else {
			// TODO generate a list of OPEN issues (iterate over the cache and check for !resolved) at the specified page
		}
		
		return false;
	}
}
