package com.watsonllc.escape.events.guns;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class BulletHitBlock implements Listener {
	private Location hitBlockLoc;
	private Sound blockHit = Sound.BLOCK_ANVIL_HIT;
	private Particle blockHitParticle = Particle.ASH;
	private int particleAmount = 100;
	private int soundVolume = 1;
	private int soundPitch = 100;

	@EventHandler
	public void onProjectileHitEntity(ProjectileHitEvent event) {
		// null check
		if (event.getHitBlock() == null)
			return;

		// Check to see if the shooter was a player
		if (!(event.getEntity().getShooter() instanceof Player))
			return;
		
		this.hitBlockLoc = event.getHitBlock().getLocation();

		soundHandler(soundVolume, soundPitch);
		particleHandler(blockHitParticle, particleAmount);
	}

	// Hit sounds
	// Block hit - Sound.BLOCK_ANVIL_HIT, 1, 100
	private void soundHandler(int volume, int pitch) {
		hitBlockLoc.getWorld().playSound(hitBlockLoc, blockHit, volume, pitch);
	}

	private void particleHandler(Particle particle, int amount) {
		hitBlockLoc.getWorld().spawnParticle(particle, hitBlockLoc, amount, 0.5, 0.5, 0.5, 0.1);
	}
}