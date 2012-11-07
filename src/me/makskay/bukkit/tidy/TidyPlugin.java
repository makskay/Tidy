package me.makskay.bukkit.tidy;

import me.makskay.bukkit.tidy.commands.CommentCommand;
import me.makskay.bukkit.tidy.commands.DestickyCommand;
import me.makskay.bukkit.tidy.commands.HelpmeCommand;
import me.makskay.bukkit.tidy.commands.InvestigateCommand;
import me.makskay.bukkit.tidy.commands.IssueCommand;
import me.makskay.bukkit.tidy.commands.IssuesCommand;
import me.makskay.bukkit.tidy.commands.ReopenCommand;
import me.makskay.bukkit.tidy.commands.ResolveCommand;
import me.makskay.bukkit.tidy.commands.StickyCommand;
import me.makskay.bukkit.tidy.tasks.KillExpiredIssuesTask;
import me.makskay.bukkit.tidy.tasks.NotifyServerStaffTask;
import me.makskay.bukkit.tidy.tasks.SaveChangedIssuesTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TidyPlugin extends JavaPlugin {
	public ConfigAccessor configYml, issuesYml;
	private IssueManager issueManager;
	private PlayerManager playerManager;
	private final long TICKS_PER_MINUTE = 1200L;
	private long killExpiredIssuesDelay, notifyServerStaffDelay, saveChangedIssuesDelay;
	
	public void onEnable() {
		configYml = new ConfigAccessor(this, "config.yml");
		issuesYml = new ConfigAccessor(this, "issues.yml");
		
		configYml.saveDefaultConfig();
		configYml.reloadConfig();
		issuesYml.saveDefaultConfig();
		issuesYml.reloadConfig();
		
		killExpiredIssuesDelay = configYml.getConfig().getLong("MinutesBetweenExpiredIssuePurges") * TICKS_PER_MINUTE;
		notifyServerStaffDelay = configYml.getConfig().getLong("MinutesBetweenUnresolvedIssueNotifications") * TICKS_PER_MINUTE;
		saveChangedIssuesDelay = configYml.getConfig().getLong("MinutesBetweenChangedIssueSaves") * TICKS_PER_MINUTE;
		
		issueManager  = new IssueManager(this);
		playerManager = new PlayerManager(this);
		
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
		
		if (killExpiredIssuesDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new KillExpiredIssuesTask(this), 0, killExpiredIssuesDelay);
		}
		
		if (notifyServerStaffDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new NotifyServerStaffTask(this), 0, notifyServerStaffDelay);
		}
		
		if (saveChangedIssuesDelay > 0) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new SaveChangedIssuesTask(this), 0, saveChangedIssuesDelay);
		}
	}
	
	public IssueManager getIssueManager() {
		return issueManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}
