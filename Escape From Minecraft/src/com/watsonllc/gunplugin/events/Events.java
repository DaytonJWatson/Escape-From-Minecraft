package com.watsonllc.gunplugin.events;

import org.bukkit.plugin.PluginManager;

import com.watsonllc.gunplugin.GunPlugin;
import com.watsonllc.gunplugin.events.guns.BulletHitBlock;
import com.watsonllc.gunplugin.events.guns.BulletHitEntity;
import com.watsonllc.gunplugin.events.guns.EntityDamage;
import com.watsonllc.gunplugin.events.guns.GunController;

public class Events {
	
	public void setup() {
		PluginManager pm = GunPlugin.instance.getServer().getPluginManager();
		
		// guns
		GunPlugin.instance.getLogger().warning("Loading 'guns' listeners");
		pm.registerEvents(new BulletHitBlock(), GunPlugin.instance);
		pm.registerEvents(new BulletHitEntity(), GunPlugin.instance);
		pm.registerEvents(new EntityDamage(), GunPlugin.instance);
		pm.registerEvents(new GunController(), GunPlugin.instance);
	}	
}