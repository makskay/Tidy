package me.makskay.tidy;

import java.util.HashMap;

public class SqlStorageManager implements StorageManager {
	public SqlStorageManager(TidyPlugin plugin) {
		
	}
	
	public void saveIssue(IssueReport issue) {
		// TODO Auto-generated method stub
	}

	public void deleteIssue(IssueReport issue) {
		// TODO Auto-generated method stub
	}

	public void setNextUid(int nextUid) {
		// TODO Auto-generated method stub
	}

	public int getNextUid() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<Integer, IssueReport> loadIssues() {
		// TODO Auto-generated method stub
		return null;
	}

}
