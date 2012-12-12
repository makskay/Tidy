package me.makskay.tidy;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	private IssueManager issueManager;
	private PlayerManager playerManager;
	
	public PlayerListener(TidyPlugin plugin) {
		this.issueManager  = plugin.getIssueManager();
		this.playerManager = plugin.getPlayerManager();
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (player.hasPermission("tidy.staff")) {
			int count = 0;
			for (IssueReport issue : issueManager.getCachedIssues()) {
				if (issue.isOpen()) {
					count++;
				}
			}
		
			if (count == 1) {
				event.getPlayer().sendMessage(TidyPlugin.NEUTRAL_COLOR + "There is currently " + ChatColor.GREEN + count + 
						TidyPlugin.NEUTRAL_COLOR + " unresolved issue");
			}
		
			else if (count > 0) {
				event.getPlayer().sendMessage(TidyPlugin.NEUTRAL_COLOR + "There are currently " + ChatColor.GREEN + count + 
						TidyPlugin.NEUTRAL_COLOR + " unresolved issues");
			}
		}
		
		if (player.hasPermission("tidy.user")) {
			List<Integer> changedIssues = playerManager.getChangedIssues(player.getName());
			if (changedIssues == null) {
				return;
			}
			
			player.sendMessage(""); // TODO header for list of changed issues
			for (Integer uid : changedIssues) {
				IssueReport issue = issueManager.getIssue(uid);
				player.sendMessage(issue.shortSummary());
			}
		}
	}

	@EventHandler (ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		Player player = event.getPlayer();
		int panicLevel = 0;
		
		for (String word : message.split(" ")) {
			if (TidyPlugin.panicWords.contains(word)) {
				panicLevel++;
			}
		}
		
		if (message.endsWith("!")) {
			panicLevel *= 2;
		}
		
		if (panicLevel > TidyPlugin.panicTolerance) {
			player.sendMessage(ChatColor.GRAY + "It looks like you might be having a problem.");
			player.sendMessage(ChatColor.GRAY + "Consider using /helpme to alert server staff.");
			//issueManager.registerIssue(player.getName(), message, player.getLocation());
		}
	}
}
