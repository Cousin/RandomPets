package xyz.betanyan.randompets;

import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.ChatColor;

public class PathfinderGoalFollowPetOwner extends PathfinderGoal {

    private double speed;

    private CustomPet pet;

    private Navigation navigation;

    public PathfinderGoalFollowPetOwner(CustomPet pet, double speed) {
        this.pet = pet;
        this.navigation = (Navigation) this.pet.getNavigation();
        this.speed = speed;
    }

    /* Method called when goal becomes active */
    @Override
    public void c() {
        PathEntity pathEntity = this.navigation.a(
                pet.getOwner().getLocation().getX() + 0.5,
                pet.getOwner().getLocation().getY() + 0.5,
                pet.getOwner().getLocation().getZ() + 0.5);

        this.navigation.a(pathEntity, speed);

        // Randomly set owner on fire
        // Seems like a small chance, yet this method is called multiple times a second so you get set on fire a lot.
        if (Math.random() < 0.003) {
            pet.getOwner().setFireTicks(40);
            pet.getOwner().sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Your pet just set you on fire!");
        }
    }

    /* This method seems to be called continuously as long as the entity is alive */
    @Override
    public void e() {
        c();
    }

    /* Returns if the pathfindergoal is 'active' */
    @Override
    public boolean a() {
        return true;
    }
}
