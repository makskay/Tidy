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

package me.makskay.tidy.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class YamlStorageManager implements StorageManager {
	private ConfigAccessor issuesYml;
	private TidyPlugin plugin;
	
	public YamlStorageManager(TidyPlugin plugin, String issuesFileName) {
		issuesYml = new ConfigAccessor(plugin, issuesFileName);
		issuesYml.reloadConfig();
		issuesYml.saveDefaultConfig();
	}
	
	public void saveIssue(IssueReport issue) {
		String path = "issues." + issue.getUid();
		
		issuesYml.getConfig().set(path + ".owner", issue.getOwnerName());
		issuesYml.getConfig().set(path + ".description", issue.getDescription());
		issuesYml.getConfig().set(path + ".location", issue.getLocationString());
		issuesYml.getConfig().set(path + ".open", issue.isOpen());
		issuesYml.getConfig().set(path + ".sticky", issue.isSticky());
		issuesYml.getConfig().set(path + ".comments", issue.getComments());
		issuesYml.getConfig().set(path + ".timestamp", System.currentTimeMillis());
		
		issuesYml.saveConfig();
		issuesYml.reloadConfig();

	}

	public void deleteIssue(IssueReport issue) {
		String path = "issues." + issue.getUid();
		issuesYml.getConfig().set(path, null);
		issuesYml.saveConfig();
		issuesYml.reloadConfig();

	}
	
	public HashMap<Integer, IssueReport> loadIssues() {
		HashMap<Integer, IssueReport> cachedIssues = new HashMap<Integer, IssueReport>();
		FileConfiguration issuesFile = issuesYml.getConfig();
		int nextUid = issuesFile.getInt("NextIssueUID");
		if (nextUid < 1) {
			plugin.getLogger().warning("Your issues.yml file is corrupted. Deleting it and reloading the server is recommended.");
		}
		
		Set<String> keys = issuesFile.getConfigurationSection("issues").getKeys(false);
		for (String key : keys) { // pull all issues on disk into cache
			try {
				int uid     = Integer.parseInt(key);
				String path = "issues." + uid + ".";
				
				String ownerName      = issuesFile.getString(path + "owner");
				String description    = issuesFile.getString(path + "description");
				String[] locAsString  = issuesFile.getString(path + "location").split(",");
				Location loc          = new Location(Bukkit.getWorld(locAsString[0]), Integer.parseInt(locAsString[1]), 
						Integer.parseInt(locAsString[2]), Integer.parseInt(locAsString[3]));
				List<String> comments = issuesFile.getStringList(path + "comments");
				boolean isOpen        = issuesFile.getBoolean(path + "open");
				boolean isSticky      = issuesFile.getBoolean(path + "sticky");
				long timestamp        = issuesFile.getLong(path + "timestamp");
				
				IssueReport issue = new IssueReport(ownerName, description, uid, loc, comments, isOpen, isSticky, timestamp);
				if (issue.isIntact()) { 
					cachedIssues.put(uid, issue);
				}
			} 
			
			catch (NumberFormatException e) {
				continue;
			}
		}
		
		return cachedIssues;
	}
	
	public void setNextUid(int nextUid) {
		issuesYml.getConfig().set("NextIssueUID", nextUid);
		issuesYml.saveConfig();
	}
	
	public int getNextUid() {
		return issuesYml.getConfig().getInt("NextIssueUID");
	}
}
