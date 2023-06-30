package com.watsonllc.escape.events.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickInventory implements Listener {
	private InventoryClickEvent event;
	private int startSlot = 9;
    private int endSlot = 35;
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		this.event = event;
		
		if(!clickedBlockedSlot()) 
			return;
		
		event.setCancelled(true);
	}
	
	private boolean clickedBlockedSlot() {
		int slotClicked = event.getSlot();
		
		if (slotClicked >= startSlot && slotClicked <= endSlot) 
			return true;
		else
			return false;
	}
}
