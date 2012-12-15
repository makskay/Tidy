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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.makskay.tidy.IssueManager;
import me.makskay.tidy.IssueReport;
import me.makskay.tidy.TidyPlugin;

public class NotifyServerStaffTask implements Runnable {
	private IssueManager issueManager;
	
	public NotifyServerStaffTask(TidyPlugin plugin) {
		this.issueManager = plugin.getIssueManager();
	}
	
	public void run() {
		int count = 0;
		for (IssueReport issue : issueManager.getCachedIssues()) {
			if (issue.isOpen()) {
				count++;
			}
		}
		
		if (count < 1) {
			return;
		}
		
		else if (count == 1) {
			Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "There is currently " + ChatColor.GREEN + count + 
					TidyPlugin.NEUTRAL_COLOR + " unresolved issue.", "tidy.staff");
		}
		
		else {
			Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "There are currently " + ChatColor.GREEN + count + 
					TidyPlugin.NEUTRAL_COLOR + " unresolved issues.", "tidy.staff");
		}
		
		Bukkit.broadcast(TidyPlugin.NEUTRAL_COLOR + "Type /issues for details.", "tidy.staff");
	}
}
