package com.watsonllc.gunplugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.watsonllc.gunplugin.config.Config;
import com.watsonllc.gunplugin.events.Events;

public class GunPlugin extends JavaPlugin {
	
	public static GunPlugin instance;
	
	Events events = new Events();
	Config config = new Config();
	
	@Override
	public void onEnable() {
		instance = this;
		
		config.setup();
		events.setup();
	}
}
