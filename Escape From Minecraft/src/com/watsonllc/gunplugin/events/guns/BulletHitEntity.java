package com.watsonllc.gunplugin.events.guns;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.watsonllc.gunplugin.config.Config;

public class BulletHitEntity implements Listener {
	private LivingEntity target;
	private Player player;
	private Location hitEntityLoc;
	private Sound hitMarker = Sound.valueOf(Config.getString("hitMarker.sound"));
	private Sound entityHitSound = Sound.valueOf(Config.getString("entityHit.sound"));
	private Particle entityHitParticle = Particle.valueOf(Config.getString("entityHitParticle.particle"));
	private double DAMAGE = Config.getDouble("gun.damage");
	private int entityHitParticleAmount = Config.getInt("entityHitParticle.amount");
	private int hitEntitySoundVolume = Config.getInt("entityHit.volume");
	private int hitEntitySoundPitch = Config.getInt("entityHit.pitch");
	private int hitMarkerVolume = Config.getInt("hitMarker.volume");
	private int hitMarkerPitch = Config.getInt("hitMarker.pitch");

	@EventHandler
	public void onProjectileHitEntity(ProjectileHitEvent event) {
		// Null check
		if (event.getHitEntity() == null)
			return;

		// Check if the entity is living
		if (!(event.getHitEntity() instanceof LivingEntity))
			return;

		// Check to see if the shooter was a player
		if (!(event.getEntity().getShooter() instanceof Player))
			return;
		
		this.target = (LivingEntity) event.getHitEntity();
		this.hitEntityLoc = event.getHitEntity().getLocation();
		this.player = (Player) event.getEntity().getShooter();

		// deal damage
		// wont return a value lower than 0
		target.setHealth(damage(DAMAGE));

		soundHandler(hitEntitySoundVolume, hitEntitySoundPitch);
		particleHandler(entityHitParticle, entityHitParticleAmount);
		
		return;
	}

	private void soundHandler(int volume, int pitch) {
		hitEntityLoc.getWorld().playSound(hitEntityLoc, entityHitSound, volume, pitch);
		player.playSound(player, hitMarker, hitMarkerVolume, hitMarkerPitch);
	}
	
	private void particleHandler(Particle particle, int amount) {
		hitEntityLoc.getWorld().spawnParticle(particle, hitEntityLoc, amount, 0.5, 0.6, .5, 0.1);
	}

	private double damage(double damage) {
		return target.getHealth() - damage > 0 ? target.getHealth() - damage : 0;
	}
}