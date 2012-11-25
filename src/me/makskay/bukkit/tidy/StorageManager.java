/*
 * A StorageManager should be used for any operation that touches actual files on disk,
 * with the exception of the plugin's config.yml. The plugin will automatically decide what
 * sort of storage manager to use on startup, based on the config settings and/or the
 * presence or absence of certain types of storage (i.e. an issues.yml file or SQL database.)
 */

package me.makskay.bukkit.tidy;

import java.util.HashMap;

public interface StorageManager {
	public void saveIssue(IssueReport issue);
	public void deleteIssue(IssueReport issue);
	public void setNextUid(int nextUid); // called whenever a new issue is created, to ensure that every UID is unique
	public int getNextUid(); // called at startup, so that the IssueManager can cache the next UID
	public HashMap<Integer, IssueReport> loadIssues(); // maps UIDs to actual issue objects
}
