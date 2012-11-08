package me.makskay.bukkit.tidy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class IssueReport {
	private String ownerName, description;
	private int uid; // the ID number by which the issue is uniquely identifiable
	private Location location; // the location from which this issue was filed
	private ArrayList<String> comments;
	private boolean isOpen, isSticky, hasChanged;
	private long timestamp;
	
	public IssueReport(String ownerName, String description, int uid, Location location) {
		this.ownerName   = ownerName;
		this.description = description;
		this.uid         = uid;
		this.location    = location;
		this.comments    = new ArrayList<String>();
		this.isOpen      = true;
		this.isSticky    = false;
		this.hasChanged  = false;
		this.timestamp   = System.currentTimeMillis();
	}
	
	public IssueReport(String ownerName, String description, int uid, Location location, 
			List<String> comments, boolean isOpen, boolean isSticky, long timestamp) {
		this.ownerName   = ownerName;
		this.description = description;
		this.uid         = uid;
		this.location    = location;
		this.comments    = new ArrayList<String>();
		this.isOpen      = isOpen;
		this.isSticky    = isSticky;
		this.hasChanged  = false;
		this.timestamp   = timestamp;
		
		this.comments.addAll(comments);
	}
	
	public void addComment(String authorName, String comment) {
		comments.add(authorName + ": " + comment);
	}
	
	public boolean markAsOpen(String authorName, String comment) { //returns false if issue was already open
		if (isOpen) {
			return false;
		}
		
		if (isSticky) {
			markAsNonSticky(authorName, comment);
			comment = "see preceding de-sticky message for details";
		}
		
		addComment(authorName, "Marked issue as " + statusColor() + "open " + ChatColor.WHITE + "(" + comment + ")");
		return true;
	}
	
	public boolean markAsClosed(String authorName, String comment) { //returns false if issue was already closed
		if (isOpen) {
			return false;
		}
		
		addComment(authorName, "Marked issue as " + statusColor() + "resolved " + ChatColor.WHITE + "(" + comment + ")");
		return true;
	}
	
	public boolean markAsSticky(String authorName, String comment) {
		if (isSticky) {
			return false;
		}
		
		if (isOpen) {
			markAsClosed(authorName, comment);
			comment = "see preceding close message for details";
		}
		
		addComment(authorName, "Marked issue as " + statusColor() + "sticky " + ChatColor.WHITE + "(" + comment + ")");
		return true;
	}
	
	public boolean markAsNonSticky(String authorName, String comment) {
		if (!isSticky) {
			return false;
		}
		
		addComment(authorName, statusColor() + "De-stickied" + ChatColor.WHITE + " issue (" + comment + ")");
		return true;
	}
	
	public String getLocationString() {
		return location.getWorld().getName() + "," + location.getBlockX() + "," 
				+ location.getBlockY() + "," + location.getBlockZ();
	}
	
	public boolean canBeEditedBy(CommandSender sender) {
		return (sender.getName().equals(ownerName)) || (sender.hasPermission("tidy.staff"));
	}
	
	public boolean isIntact() { // used to verify that this issue is valid on disk (and not producing corrupted cache objects)
		return (ownerName != null) && (description != null) && (uid > 0) && 
				(location != null) && (comments != null) && (timestamp > 0);
	}
	
	public boolean shouldBeDeleted() {
		return ((!isOpen) && (System.currentTimeMillis() - timestamp > 259200000)); // delete after 3 days closed without change
																					// TODO make time configurable
	}
	
	public String stringSummary() {
		int charactersPerLine = 80; // TODO this is a placeholder, not a guaranteed value
		String truncatedDescription = description;
		if (description.length() > charactersPerLine) {
			truncatedDescription = description.substring(0, charactersPerLine - 4) + "...";
		}
		return statusColor() + "#" + uid + ChatColor.GRAY + " (" + ownerName + "): " + ChatColor.WHITE + truncatedDescription; 
	}
	
	private ChatColor statusColor() {
		if (isOpen) {
			return ChatColor.GREEN;
		}
		
		if (isSticky) {
			return ChatColor.DARK_RED;
		}
		
		return ChatColor.RED;
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean isSticky() {
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
	
	public boolean hasChanged() {
		return hasChanged;
	}
}
