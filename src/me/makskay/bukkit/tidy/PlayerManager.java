package me.makskay.bukkit.tidy;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerManager {
	private HashMap<String, Location> preInvestigateLocations;
	// private HashMap<String, List<Integer>> changedIssues;
	
	public PlayerManager(TidyPlugin plugin) {
		preInvestigateLocations = new HashMap<String, Location>();
	}
	
	public void saveLocationOf(Player player) {
		preInvestigateLocations.put(player.getName(), player.getLocation());
	}
	
	public Location getSavedLocationOf(Player player) {
		return preInvestigateLocations.get(player.getName());
	}
	
	public void clearSavedLocationOf(Player player) {
		preInvestigateLocations.remove(player.getName());
	}
	
	public void addChangedIssue(String playerName, int uid) {
		// TODO method stub -- add implementation
	}
	
	public List<Integer> getChangedIssues(String playerName) {
		// TODO method stub -- add implementation
		return null; // dummy return so that Eclipse doesn't get mad at me
	}
}
