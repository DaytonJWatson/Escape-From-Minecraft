package com.watsonllc.gunplugin.events.guns;

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

import com.watsonllc.gunplugin.GunPlugin;
import com.watsonllc.gunplugin.config.Config;

public class GunController implements Listener {
	private Player player;
	private Location loc;
	private Material handItem;
	private Material gunItem = Material.valueOf(Config.getString("gunItem"));
	private Material ammoItem = Material.valueOf(Config.getString("ammoItem"));
	private Particle bulletParticle = Particle.valueOf(Config.getString("bulletParticle"));
	private Sound noAmmo = Sound.valueOf(Config.getString("noAmmo"));
	private Sound gunshot = Sound.valueOf(Config.getString("gunshot"));
	private Sound reload = Sound.valueOf(Config.getString("reload"));
	private String shootAction = Config.getString("shootAction");
	private String reloadAction = Config.getString("reloadAction");
	private Snowball snowballProjectile;
	private boolean projectileVisible = Config.getBoolean("projectileVisible");
	private boolean bulletDrop = Config.getBoolean("bulletDrop");
	private boolean cancelEvent = Config.getBoolean("cancelEvent");;
	private double bulletVelocity = Config.getDouble("bulletVelocity");
	private double recoilStanding = Config.getDouble("recoilStanding");
	private double recoilCrouching = Config.getDouble("recoilCrouching");
	private int shootVolume = Config.getInt("shootVolume");
	private int shootPitch = Config.getInt("shootPitch");
	private int reloadVolume = Config.getInt("reloadVolume");
	private int reloadPitch = Config.getInt("reloadPitch");
	private int noAmmoVolume = Config.getInt("noAmmoVolume");
	private int noAmmoPitch = Config.getInt("noAmmoPitch");
	private int removeAmmoAmount = Config.getInt("removeAmmoAmount");
	private int bulletParticleAmount = Config.getInt("bulletParticleAmount");
	private int particleStartDelay = Config.getInt("particleStartDelay");
	private int particleFollowTicks = Config.getInt("particleFollowTicks");
	
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
		if (!event.getAction().toString().contains(shootAction))
			return;

		this.handItem = player.getInventory().getItemInMainHand().getType();
		// correct item check
		if (handItem != gunItem)
			return;

		// currently reloading check
		if(isReloading()) {
			event.setCancelled(true);
			return;
		}
		
		// reload check
		if (forceReload()) {
			event.setCancelled(true);
			playSound(noAmmo, noAmmoVolume, noAmmoPitch);
			return;
		}

		// ammo handler
		if (!hasAmmo()) {
			playSound(noAmmo, noAmmoVolume, noAmmoPitch);
			event.setCancelled(cancelEvent);
			return;
		} else
			removeAmmo();
		
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
		}.runTaskTimer(GunPlugin.instance, particleStartDelay, particleFollowTicks);
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

		// left click check
		if (!event.getAction().toString().contains(reloadAction))
			return;	

		// check if they have enough ammo to reload
		if(!hasAmmo()) {
			return;
		}
		
		// check if ammo is already full
		if(ammoFull()) {
			return;
		}
		
		startReloading();
		resetShots();
		event.setCancelled(cancelEvent);

	}

	public void startReloading() {
        reloading.put(player, true);

        new BukkitRunnable() {
            @Override
            public void run() {
                stopReloading();
                playSound(reload, reloadVolume, reloadPitch);
                reloading.remove(player);
            }
        }.runTaskLater(GunPlugin.instance, 60); // 60 ticks = 3 seconds
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