package com.watsonllc.escape.config;

import com.watsonllc.escape.Escape;

public class Config {
	public void setup() {
		create();
	}
	
	public void create() {
		Escape.instance.getConfig().options().copyDefaults(true);
		Escape.instance.saveDefaultConfig();
	}
	
	public void save() {
		Escape.instance.saveConfig();
	}
}
