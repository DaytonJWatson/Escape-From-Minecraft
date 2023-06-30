package com.watsonllc.escape.events.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.escape.Escape;
import com.watsonllc.escape.Utils;

public class BlockInventory implements Listener {
	private Player player;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.player = event.getPlayer();
		
		blockInventory(player, 9, 35);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		this.player = event.getPlayer();
		
		blockInventory(player, 9, 35);
	}
	
	private void blockInventory(Player player, int x, int y) {
		BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
            	Inventory inv = player.getInventory();
            	ItemStack blocked = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            	ItemMeta blockedMeta = blocked.getItemMeta();
            	blockedMeta.setDisplayName(Utils.color("&8BLOCKED"));
            	blocked.setItemMeta(blockedMeta);
            	
            	for(int xF = x; xF<=y; xF++) {
            		inv.setItem(xF, blocked);
            	}
            }
        };
        runnable.runTaskLater(Escape.instance, 10L);
	}
}