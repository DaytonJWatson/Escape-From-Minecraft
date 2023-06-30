package com.watsonllc.escape.events.guns;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.watsonllc.escape.Escape;
import com.watsonllc.escape.Utils;

public class ShootGun implements Listener {
	private Player player;
	private Location loc;
	private Material handItem;
	private Material gunItem = Material.IRON_HOE;
	private Particle bulletParticle = Particle.ELECTRIC_SPARK;
	private Sound noAmmo = Sound.UI_BUTTON_CLICK;
	private Sound gunshot = Sound.ENTITY_ARROW_SHOOT;
	private boolean projectileVisible = false;
	private boolean bulletDrop = false;
	private double bulletVelocity = 3.5;
	private double recoilStanding = 0.05;
	private double recoilCrouching = 0.01;
	private int shootVolume = 3;
	private int noAmmoVolume = 1;
	private int shootPitch = 100;
	private int noAmmoPitch = 100;
	private int removeAmmoAmount = 1;
	private int bulletParticleAmount = 1;
	private int particleStartDelay = 2;
	private int particleFollowTicks = 1;

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		this.player = event.getPlayer();
		this.loc = player.getLocation();
		
		// null check
		if (event.getAction() == null)
			return;

		// right click check
		if (event.getAction() != Action.RIGHT_CLICK_AIR) {
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;
		}
		
		this.handItem = player.getInventory().getItemInMainHand().getType();
		// correct item check
		if (handItem != gunItem)
			return;
		
		// no ammo
		if (!Utils.hasAmmo(player)) {
			playSound(noAmmo, noAmmoVolume, noAmmoPitch);
			event.setCancelled(true);
			return;
		}

		// create the projectile
		Snowball snowballProjectile = player.launchProjectile(Snowball.class);
		// shoot the projectile
		shootProjectile(snowballProjectile);
		followProjectile(snowballProjectile);
		playSound(gunshot, shootVolume, shootPitch);
		event.setCancelled(true);

		// recoil handler
		if (player.isSneaking())
			recoilHandler(recoilCrouching);
		else
			recoilHandler(recoilStanding);

		return;
	}

	private void playSound(Sound sound, int volume, int pitch) {
		loc.getWorld().playSound(player.getLocation(), sound, volume, pitch);
	}

	private void recoilHandler(double strength) {
		Vector recoilVector = player.getLocation().getDirection().multiply(-strength);
		player.setVelocity(player.getVelocity().add(recoilVector));
	}

	private void shootProjectile(Projectile proj) {
		Vector velocity = proj.getVelocity();
		proj.setShooter(player);
		proj.setGravity(bulletDrop);
		proj.setVisibleByDefault(projectileVisible);
		proj.setVelocity(velocity.multiply(bulletVelocity));
		Utils.removeAmmo(player, removeAmmoAmount);
	}

	private void followProjectile(Projectile proj) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (proj.isDead())
					this.cancel();
				else
					player.getWorld().spawnParticle(bulletParticle, proj.getLocation(), bulletParticleAmount, 0, 0, 0, 0.1);
			}
		}.runTaskTimer(Escape.instance, particleStartDelay, particleFollowTicks);
	}
}