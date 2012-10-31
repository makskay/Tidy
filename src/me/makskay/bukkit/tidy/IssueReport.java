package me.makskay.bukkit.tidy;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class IssueReport {
	private String ownerName, description;
	private int uid; // the ID number by which the issue is uniquely identifiable
	private Location location; // the location from which this issue was filed
	private ArrayList<String> comments;
	private boolean isOpen, isSticky;
	
	public IssueReport(String ownerName, String description, int uid, Location location) {
		this.ownerName   = ownerName;
		this.description = description;
		this.uid         = uid;
		this.location    = location;
		this.comments    = new ArrayList<String>();
		this.isOpen      = true;
		this.isSticky    = false;
	}
	
	public void addComment(String authorName, String comment) {
		comments.add(authorName + ": " + comment);
	}
	
	public boolean markAsOpen(String authorName, String comment) { //returns false if issue was already open
		if (isOpen) {
			return false;
		}
		
		addComment(authorName, "Marked issue as " + ChatColor.GREEN + "open " + ChatColor.WHITE + "(" + comment + ")");
		return true;
	}
	
	public boolean markAsClosed(String authorName, String comment) { //returns false if issue was already closed
		if (isOpen) {
			return false;
		}
		
		addComment(authorName, "Marked issue as " + ChatColor.RED + "resolved " + ChatColor.WHITE + "(" + comment + ")");
		return true;
	}
	
	public boolean setSticky(boolean sticky) {
		isSticky = !isSticky;
		return isSticky;
	}
	
	public String getLocationString() {
		return location.getWorld().getName() + "," + location.getBlockX() + "," 
				+ location.getBlockY() + "," + location.getBlockZ();
	}
	
	public boolean canBeEditedBy(Player player) {
		return (player.getName().equals(ownerName)) || (player.hasPermission("tidy.staff"));
	}
	
	public boolean getOpen() {
		return isOpen;
	}
	
	public boolean getSticky() {
		return isSticky;
	}
	
	public ArrayList<String> getComments() {
		return comments;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getUid() {
		return uid;
	}
	
	public Location getLocation() {
		return location;
	}
}