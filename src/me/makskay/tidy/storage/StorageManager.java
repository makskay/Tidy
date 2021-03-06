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

import me.makskay.tidy.IssueReport;

/**
 ** A StorageManager should be used for any operation that touches actual files on disk,
 ** with the exception of the plugin's config.yml. The plugin will automatically decide what
 ** sort of storage manager to use on startup, based on the config settings and/or the
 ** presence or absence of certain types of storage (i.e. an issues.yml file or SQL database.)
 */

public interface StorageManager {
	public void saveIssue(IssueReport issue);
	public void deleteIssue(IssueReport issue);
	public void setNextUid(int nextUid); // called whenever a new issue is created, to ensure that every UID is unique
	public int getNextUid(); // called at startup, so that the IssueManager can cache the next UID
	public HashMap<Integer, IssueReport> loadIssues(); // maps UIDs to actual issue objects
}
