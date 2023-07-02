package com.watsonllc.gunplugin.events.guns;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.watsonllc.gunplugin.config.Config;

public class EntityDamage implements Listener {
	private Projectile bullet;
	private boolean cancelEvent = Config.getBoolean("cancelPlayerDamage");
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		this.bullet = (Projectile) event.getDamager();
		
		if(!(bullet instanceof Snowball))
			return;
		
		if(!(bullet.getShooter() instanceof Player))
			return;
		
		// stops vanilla damage
		event.setCancelled(cancelEvent);
	}
}