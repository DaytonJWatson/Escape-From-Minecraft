package com.watsonllc.escape;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static void removeAmmo(Player player, int amount) {
	    ItemStack snowball = new ItemStack(Material.ARROW, amount);
	    player.getInventory().removeItem(snowball);
	}
	
	public static boolean hasAmmo(Player player) {
	    for (ItemStack item : player.getInventory().getContents()) {
	    	if(item == null) 
	    		break;
	    	if(item.getType() == Material.ARROW)
	    		return true;
	    }
	    // default false
	    return false;
	}
}