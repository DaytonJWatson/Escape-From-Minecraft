package com.watsonllc.gunplugin.config;

import com.watsonllc.gunplugin.GunPlugin;

public class Config {
	public void setup() {
		create();
	}
	
	public void create() {
		GunPlugin.instance.getConfig().options().copyDefaults(true);
		GunPlugin.instance.saveDefaultConfig();
	}
	
	public void save() {
		GunPlugin.instance.saveConfig();
	}
	
	public static boolean getBoolean(String path) {
		return GunPlugin.instance.getConfig().getBoolean(path);
	}
	
	public static String getString(String path) {
		return GunPlugin.instance.getConfig().getString(path);
	}
	
	public static int getInt(String path) {
		return GunPlugin.instance.getConfig().getInt(path);
	}
	
	public static double getDouble(String path) {
		return GunPlugin.instance.getConfig().getDouble(path);
	}
}
