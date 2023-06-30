package com.watsonllc.escape.events;

import org.bukkit.plugin.PluginManager;

import com.watsonllc.escape.Escape;
import com.watsonllc.escape.events.guns.BulletHitBlock;
import com.watsonllc.escape.events.guns.BulletHitEntity;
import com.watsonllc.escape.events.guns.EntityDamage;
import com.watsonllc.escape.events.guns.ShootGun;
import com.watsonllc.escape.events.inventory.InvJoin;

public class Events {
	
	public void setup() {
		PluginManager pm = Escape.instance.getServer().getPluginManager();
		
		pm.registerEvents(new BulletHitBlock(), Escape.instance);
		pm.registerEvents(new BulletHitEntity(), Escape.instance);
		pm.registerEvents(new EntityDamage(), Escape.instance);
		pm.registerEvents(new ShootGun(), Escape.instance);
		pm.registerEvents(new InvJoin(), Escape.instance);
	}	
}