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
