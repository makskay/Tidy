/*
 * Copyright (c) 2012 Max Kreminski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */

package me.makskay.tidy.commands;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.PlayerManager;
import me.makskay.tidy.TidyPlugin;

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
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(TidyPlugin.ERROR_COLOR + "Only a player may investigate issues!");
			return true;
		}
		
		Player player = (Player) sender;
		
		int uid;
		try {
			uid = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {
			if (!args[0].equalsIgnoreCase("-end")) {
				sender.sendMessage(TidyPlugin.ERROR_COLOR + "\"" +  args[0] + "\" isn't a valid issue ID number!");
				return true;
			}
			
			// handle a request to end the issue investigation
			
			Location location = playerManager.getSavedLocationOf(player);
			if (location == null) {
				player.sendMessage(TidyPlugin.ERROR_COLOR + "You aren't currently investigating any issues!");
				return true;
			}
			
			player.teleport(location);
			playerManager.clearSavedLocationOf(player);
			player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Issue investigation ended.");
			return true;
		}
		
		IssueReport issue = issueManager.getIssue(uid);
		if (issue == null) {
			player.sendMessage(TidyPlugin.ERROR_COLOR + "Couldn't find issue #" + uid);
			return true;
		}
		
		playerManager.saveLocationOf(player);
		player.teleport(issue.getLocation());
		player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Now investigating issue #" + uid);
		player.sendMessage(TidyPlugin.NEUTRAL_COLOR + "Type /investigate -end when you're done to return to where you left off.");
		return true;
	}
}
