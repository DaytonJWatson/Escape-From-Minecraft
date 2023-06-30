package com.watsonllc.escape.events.guns;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.watsonllc.escape.Escape;

public class GunController implements Listener {
	private Player player;
	private Location loc;
	private Material handItem;
	private Material gunItem = Material.IRON_HOE;
	private Material ammoItem = Material.ARROW;
	private Particle bulletParticle = Particle.ELECTRIC_SPARK;
	private Sound noAmmo = Sound.UI_BUTTON_CLICK;
	private Sound gunshot = Sound.ENTITY_ARROW_SHOOT;
	private Sound reload = Sound.ITEM_SHIELD_BLOCK;
	private Snowball snowballProjectile;
	private boolean projectileVisible = false;
	private boolean bulletDrop = false;
	private boolean cancelEvent = true;
	private double bulletVelocity = 3.5;
	private double recoilStanding = 0.05;
	private double recoilCrouching = 0.01;
	private int shootVolume = 3;
	private int shootPitch = 100;
	private int reloadVolume = 1;
	private int reloadPitch = 100;
	private int noAmmoVolume = 1;
	private int noAmmoPitch = 100;
	private int removeAmmoAmount = 1;
	private int bulletParticleAmount = 1;
	private int particleStartDelay = 2;
	private int particleFollowTicks = 1;
	
	private HashMap<Player, Integer> currentShots = new HashMap<>();
	private HashMap<Player, Boolean> reloading = new HashMap<>();

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		this.player = event.getPlayer();
		this.loc = player.getLocation();

		// null check
		if (event.getAction() == null)
			return;

		// right click check
		if (!event.getAction().toString().contains("RIGHT_CLICK"))
			return;

		this.handItem = player.getInventory().getItemInMainHand().getType();
		// correct item check
		if (handItem != gunItem)
			return;

		// currently reloading check
		if(isReloading()) {
			player.sendMessage("you're reloading");
			event.setCancelled(true);
			return;
		}
		
		// reload check
		if (forceReload()) {
			event.setCancelled(true);
			playSound(noAmmo, noAmmoVolume, noAmmoPitch);
			player.sendMessage("reload required");
			return;
		}

		// ammo handler
		if (!hasAmmo()) {
			playSound(noAmmo, noAmmoVolume, noAmmoPitch);
			event.setCancelled(cancelEvent);
			return;
		} else
			removeAmmo();

		// shoot the projectile
		this.snowballProjectile = player.launchProjectile(Snowball.class);
		shootProjectile(snowballProjectile);
		createProjectile(snowballProjectile);
		playSound(gunshot, shootVolume, shootPitch);
		event.setCancelled(cancelEvent);
		addShot();

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
	}

	private void createProjectile(Projectile proj) {
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

	private void removeAmmo() {
		if (!hasAmmo())
			return;
		ItemStack ammo = new ItemStack(ammoItem, removeAmmoAmount);
		player.getInventory().removeItem(ammo);
	}

	private boolean hasAmmo() {
		for (ItemStack item : player.getInventory()) {
			if (item != null && item.getType() == ammoItem)
				return true;
		}
		return false;
	}

	// 					\\
	// 					\\
	// 	Reload Section 	\\
	// 					\\
	// 					\\

	@EventHandler
	public void onLeftClick(PlayerInteractEvent event) {
		this.player = event.getPlayer();
		player.sendMessage("currentShot: " + getShots());

		// left click check
		if (!event.getAction().toString().contains("LEFT_CLICK"))
			return;	

		// check if they have enough ammo to reload
		if(!hasAmmo()) {
			player.sendMessage("no ammo to reload");
			return;
		}
		
		// check if ammo is already full
		if(ammoFull()) {
			player.sendMessage("ammo already full");
			return;
		}
		
		startReloading();
		resetShots();
		player.sendMessage("reloading");
		event.setCancelled(cancelEvent);

	}

	public void startReloading() {
        reloading.put(player, true);

        new BukkitRunnable() {
            @Override
            public void run() {
            	player.sendMessage("finished reloading");
                stopReloading();
                playSound(reload, reloadVolume, reloadPitch);
                reloading.remove(player);
            }
        }.runTaskLater(Escape.instance, 60); // 60 ticks = 3 seconds
    }

    private void stopReloading() {
    	reloading.remove(player);
    }
    
    private boolean isReloading() {
    	if(reloading.containsKey(player))
    		return true;
    	else
    		return false;
    }
    
    private boolean ammoFull() {
    	if(getShots() == 0) 
    		return true;
    	else
    		return false;
    }
	
	private boolean forceReload() {
		if (getShots() >= 30)
			return true;
		else
			return false;
	}

	private void resetShots() {
		currentShots.put(player, 0);
	}

	private int getShots() {
		if (currentShots.get(player) == null)
			return 0;
		else
			return currentShots.get(player);
	}

	private void addShot() {
		currentShots.put(player, getShots() + 1);
	}
}