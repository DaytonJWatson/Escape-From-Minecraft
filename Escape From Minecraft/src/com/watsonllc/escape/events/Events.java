package com.watsonllc.escape.events;

import org.bukkit.plugin.PluginManager;

import com.watsonllc.escape.Escape;
import com.watsonllc.escape.events.guns.BulletHitBlock;
import com.watsonllc.escape.events.guns.BulletHitEntity;
import com.watsonllc.escape.events.guns.EntityDamage;
import com.watsonllc.escape.events.guns.GunController;
import com.watsonllc.escape.events.inventory.BlockInventory;
import com.watsonllc.escape.events.inventory.ClickInventory;

public class Events {
	
	public void setup() {
		PluginManager pm = Escape.instance.getServer().getPluginManager();
		
		// guns
		Escape.instance.getLogger().warning("Loading 'guns' listeners");
		pm.registerEvents(new BulletHitBlock(), Escape.instance);
		pm.registerEvents(new BulletHitEntity(), Escape.instance);
		pm.registerEvents(new EntityDamage(), Escape.instance);
		pm.registerEvents(new GunController(), Escape.instance);
		
		// inventory
		Escape.instance.getLogger().warning("Loading 'inventory' listeners");
		pm.registerEvents(new BlockInventory(), Escape.instance);
		pm.registerEvents(new ClickInventory(), Escape.instance);
	}	
}