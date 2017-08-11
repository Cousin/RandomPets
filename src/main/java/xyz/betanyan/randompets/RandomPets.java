package xyz.betanyan.randompets;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPets extends JavaPlugin implements Listener {

    private Map<UUID, CustomPet> pets;

    private List<EntityType> petTypes;

    @Override
    public void onEnable() {
        this.pets = new HashMap<>();
        this.petTypes = Arrays.asList(
                EntityType.ZOMBIE,
                EntityType.SKELETON,
                EntityType.ENDERMAN,
                EntityType.CHICKEN,
                EntityType.PIG
        );

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        EntityType petType = petTypes.get(ThreadLocalRandom.current().nextInt(petTypes.size()));

        spawnPet(new CustomPet(player, petType));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        // Goodbye old friend :(
        CustomPet pet = pets.get(event.getPlayer().getUniqueId());

        pet.getBukkitEntity().getPassenger().remove();
        pet.die();

    }

    private void spawnPet(CustomPet pet) {

        Location loc = pet.getOwner().getLocation();

        pet.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(pet);

        // Armor stand that rides the pet so that a nametag is always visible
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(ChatColor.AQUA.toString() + ChatColor.BOLD + pet.getOwner().getName() + "'s Pet");

        pet.getBukkitEntity().setPassenger(armorStand);

        pets.put(pet.getOwner().getUniqueId(), pet);

    }

}
