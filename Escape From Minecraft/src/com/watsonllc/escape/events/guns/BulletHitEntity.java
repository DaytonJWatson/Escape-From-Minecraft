package com.watsonllc.escape.events.guns;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class BulletHitEntity implements Listener {
	private LivingEntity target;
	private Location hitEntityLoc;
	private Sound entityHitSound = Sound.BLOCK_BAMBOO_BREAK;
	private Particle entityHitParticle = Particle.FALLING_LAVA;
	private double DAMAGE = 5.0;
	private int particleAmount = 33;
	private int soundVolume = 1;
	private int soundPitch = 100;

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

		// deal damage
		// wont return a value lower than 0
		target.setHealth(damageMath(DAMAGE));

		soundHandler(soundVolume, soundPitch);
		particleHandler(entityHitParticle, particleAmount);
		
		return;
	}

	private void soundHandler(int volume, int pitch) {
		hitEntityLoc.getWorld().playSound(hitEntityLoc, entityHitSound, volume, pitch);
	}
	
	private void particleHandler(Particle particle, int amount) {
		hitEntityLoc.getWorld().spawnParticle(particle, hitEntityLoc, amount, 0.5, 0.6, .5, 0.1);
	}

	private double damageMath(double damage) {
		return target.getHealth() - damage > 0 ? target.getHealth() - damage : 0;
	}
}