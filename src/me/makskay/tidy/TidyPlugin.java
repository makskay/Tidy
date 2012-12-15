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

package me.makskay.tidy;

import java.util.List;

import me.makskay.tidy.commands.CommentCommand;
import me.makskay.tidy.commands.DestickyCommand;
import me.makskay.tidy.commands.HelpmeCommand;
import me.makskay.tidy.commands.InvestigateCommand;
import me.makskay.tidy.commands.IssueCommand;
import me.makskay.tidy.commands.IssuesCommand;
import me.makskay.tidy.commands.ReopenCommand;
import me.makskay.tidy.commands.ResolveCommand;
import me.makskay.tidy.commands.StickyCommand;
import me.makskay.tidy.storage.ConfigAccessor;
import me.makskay.tidy.storage.StorageManager;
import me.makskay.tidy.storage.YamlStorageManager;
import me.makskay.tidy.tasks.NotifyServerStaffTask;
import me.makskay.tidy.tasks.SaveChangedIssuesTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TidyPlugin extends JavaPlugin {
	private ConfigAccessor configYml;
	private IssueManager issueManager;
	private PlayerManager playerManager;
	private StorageManager storageManager;
	private final long MILLISECONDS_PER_DAY = 86400000L, TICKS_PER_MINUTE = 1200L, TICKS_PER_SECOND = 20L;
	private long notifyServerStaffDelay, saveChangedIssuesDelay;
	static long issueLifetime;
	static int panicTolerance;
	static List<String> panicWords;
	public static final ChatColor ERROR_COLOR      = ChatColor.RED,
			                      NEUTRAL_COLOR    = ChatColor.GRAY, 
			                      UNRESOLVED_COLOR = ChatColor.GREEN,
			                      RESOLVED_COLOR   = ChatColor.RED,
			                      STICKY_COLOR     = ChatColor.DARK_RED,
			                      PLAYERNAME_COLOR = ChatColor.YELLOW,
			                      HIGHLIGHT_COLOR  = ChatColor.LIGHT_PURPLE;
	
	public void onEnable() {
		configYml = new ConfigAccessor(this, "config.yml");
		configYml.reloadConfig();
		configYml.saveDefaultConfig();
		
		FileConfiguration config = configYml.getConfig();
		issueLifetime            = config.getLong("IssueLifetimeInDays") * MILLISECONDS_PER_DAY;
		notifyServerStaffDelay   = config.getLong("MinutesBetweenUnresolvedIssueNotifications") * TICKS_PER_MINUTE;
		saveChangedIssuesDelay   = config.getLong("SecondsBetweenChangedIssueSaves") * TICKS_PER_SECOND;
		panicWords               = config.getStringList("PanicWords");
		panicTolerance           = config.getInt("PanicTolerance");
		
		storageManager = new YamlStorageManager(this, "issues.yml"); // must be initialized before issueManager and playerManager
		issueManager   = new IssueManager(this);
		playerManager  = new PlayerManager(this);
		
		getCommand("comment").setExecutor(new CommentCommand(this));
		getCommand("desticky").setExecutor(new DestickyCommand(this));
		getCommand("helpme").setExecutor(new HelpmeCommand(this));
		getCommand("investigate").setExecutor(new InvestigateCommand(this));
		getCommand("issue").setExecutor(new IssueCommand(this));
		getCommand("issues").setExecutor(new IssuesCommand(this));
		getCommand("reopen").setExecutor(new ReopenCommand(this));
		getCommand("resolve").setExecutor(new ResolveCommand(this));
		getCommand("sticky").setExecutor(new StickyCommand(this));
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		if (notifyServerStaffDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new NotifyServerStaffTask(this), 0, notifyServerStaffDelay);
		}
		
		if (saveChangedIssuesDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveChangedIssuesTask(this), 0, saveChangedIssuesDelay);
		}
		
		if (configYml.getConfig().getBoolean("AutoUpdate")) { // auto-updater stuff
			try {
				@SuppressWarnings("unused")
				Updater updater = new Updater(this, "tidy", this.getFile(), Updater.UpdateType.DEFAULT, false);
			} 
			
			catch (Exception e) {
				getLogger().info("Couldn't connect to BukkitDev to check for updates.");
			}
		}
	}
	
	public void onDisable() {
		for (IssueReport issue : issueManager.getCachedIssues()) {
			if (issue.hasChanged()) {
				if (issue.shouldBeDeleted()) {
					storageManager.deleteIssue(issue);
				}
				
				else {
					storageManager.saveIssue(issue);
					playerManager.addChangedIssue(issue.getOwnerName(), issue.getUid());
				}
			}
		}
	}
	
	public IssueManager getIssueManager() {
		return issueManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public StorageManager getStorageManager() {
		return storageManager;
	}
}
