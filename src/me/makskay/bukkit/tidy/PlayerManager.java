package me.makskay.bukkit.tidy;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerManager {
	private HashMap<String, Location> preInvestigateLocations;
	
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
}
