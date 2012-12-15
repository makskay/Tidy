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

package me.makskay.tidy.tasks;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;
import me.makskay.tidy.storage.StorageManager;

/** 
 ** Smooth, backgrounded saving of changed issues.
 ** Every time this task runs, one issue will have changes saved to disk.
 ** The frequency with which this task runs can be changed to improve performance.
 */

public class SaveChangedIssuesTask implements Runnable {
	private IssueManager issueManager;
	private StorageManager storageManager;
	
	public SaveChangedIssuesTask(TidyPlugin plugin) {
		this.issueManager   = plugin.getIssueManager();
		this.storageManager = plugin.getStorageManager();
	}
	
	public void run() {
		IssueReport issue = null;
		boolean delete = false;
		for (IssueReport iss : issueManager.getCachedIssues()) {
			if (iss.hasChanged()) {
				issue = iss;
				issue.setHasChanged(false);
				break;
			}
			
			delete = iss.shouldBeDeleted();
			if (delete) {
				issue = iss;
				issueManager.getCachedIssues().remove(issue);
				break;
			}
		}
		
		if (issue == null) {
			return; // there were no issues with changes waiting to be saved
		}
		
		if (delete) {
			storageManager.deleteIssue(issue);
		}
		
		else {
			storageManager.saveIssue(issue);
		}
	}
}
