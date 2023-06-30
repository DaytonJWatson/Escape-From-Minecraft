package com.watsonllc.escape;

import org.bukkit.plugin.java.JavaPlugin;

import com.watsonllc.escape.commands.Commands;
import com.watsonllc.escape.config.Config;
import com.watsonllc.escape.events.Events;

public class Escape extends JavaPlugin {
	
	public static Escape instance;
	
	Events events = new Events();
	Commands commands = new Commands();
	Config config = new Config();
	
	@Override
	public void onEnable() {
		instance = this;
		
		config.setup();
		commands.setup();
		events.setup();
	}
}
