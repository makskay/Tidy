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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.makskay.tidy.storage.StorageManager;

import org.bukkit.Location;

public class IssueManager {
	private HashMap<Integer, IssueReport> cachedIssues;
	private StorageManager storageManager;
	private int nextUid;
	
	public IssueManager(TidyPlugin plugin) {
		this.storageManager = plugin.getStorageManager();
		this.cachedIssues = storageManager.loadIssues();
		this.nextUid = storageManager.getNextUid();
		if (nextUid == 0) {
			plugin.getLogger().warning("Your issues.yml file is corrupted. Deleting it and reloading the server is recommended.");
		}
	}
	
	public IssueReport registerIssue(String ownerName, String description, Location loc) {
		IssueReport issue = new IssueReport(ownerName, description, nextUid, loc);
		cachedIssues.put(nextUid, issue); // pull this issue into cached memory
		incrementNextUid(); // the next issue will be assigned the next greatest integer as a UID
		return issue;
	}
	
	public void purge() { // mark all resolved, non-sticky issues for deletion
		for (IssueReport issue : cachedIssues.values()) {
			if (!issue.isOpen() && !issue.isSticky()) {
				issue.setShouldBeDeleted(true);
				issue.setHasChanged(true);
			}
		}
		
		// TODO Rebase UIDs
	}
	
	public IssueReport getIssue(int uid) {
		return cachedIssues.get(uid);
	}
	
	private void incrementNextUid() {
		nextUid++;
		storageManager.setNextUid(nextUid);
	}

	public List<IssueReport> getCachedIssues() { // TODO this will return numerical order in some cases, but not all
		ArrayList<IssueReport> issues = new ArrayList<IssueReport>();
		for (IssueReport issue : cachedIssues.values()) {
			issues.add(issue);
		}
		return issues;
	}
}
