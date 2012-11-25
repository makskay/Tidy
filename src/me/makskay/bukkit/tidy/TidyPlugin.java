package me.makskay.bukkit.tidy;

import java.util.List;

import me.makskay.bukkit.tidy.commands.CommentCommand;
import me.makskay.bukkit.tidy.commands.DestickyCommand;
import me.makskay.bukkit.tidy.commands.HelpmeCommand;
import me.makskay.bukkit.tidy.commands.InvestigateCommand;
import me.makskay.bukkit.tidy.commands.IssueCommand;
import me.makskay.bukkit.tidy.commands.IssuesCommand;
import me.makskay.bukkit.tidy.commands.ReopenCommand;
import me.makskay.bukkit.tidy.commands.ResolveCommand;
import me.makskay.bukkit.tidy.commands.StickyCommand;
import me.makskay.bukkit.tidy.commands.TidyupCommand;
import me.makskay.bukkit.tidy.tasks.NotifyServerStaffTask;
import me.makskay.bukkit.tidy.tasks.SaveChangedIssuesTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TidyPlugin extends JavaPlugin {
	private ConfigAccessor configYml, issuesYml;
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
		issuesYml = new ConfigAccessor(this, "issues.yml");
		
		configYml.reloadConfig();
		configYml.saveDefaultConfig();
		issuesYml.reloadConfig();
		issuesYml.saveDefaultConfig();
		
		FileConfiguration config = configYml.getConfig();
		issueLifetime            = config.getLong("IssueLifetimeInDays") * MILLISECONDS_PER_DAY;
		notifyServerStaffDelay   = config.getLong("MinutesBetweenUnresolvedIssueNotifications") * TICKS_PER_MINUTE;
		saveChangedIssuesDelay   = config.getLong("SecondsBetweenChangedIssueSaves") * TICKS_PER_SECOND;
		panicWords               = config.getStringList("PanicWords");
		panicTolerance           = config.getInt("PanicTolerance");
		
		storageManager = new YamlStorageManager(this); // must be initialized before issueManager and playerManager
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
		getCommand("tidyup").setExecutor(new TidyupCommand(this));
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		if (notifyServerStaffDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new NotifyServerStaffTask(this), 0, notifyServerStaffDelay);
		}
		
		if (saveChangedIssuesDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveChangedIssuesTask(this), 0, saveChangedIssuesDelay);
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
		
		issuesYml.saveConfig();
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
	
	public ConfigAccessor getIssuesYml() { // TODO this probably shouldn't need to exist -- it's only called by YamlStorageManager, and only once
		return issuesYml;
	}
}
