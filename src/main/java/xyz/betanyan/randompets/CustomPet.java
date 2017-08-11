package xyz.betanyan.randompets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/*  A little trick I figured out on my own.. you can extend EntityZombie (or probably most entities) and just set the entity id
   to make it be any entity. When extending EntityInsentient or EntityMonster, it either doesn't work or Pathfinders go crazy.
 */
public class CustomPet extends EntityZombie {

    private Player owner;

    public CustomPet(Player owner, EntityType entityType) {
        super(((CraftWorld) owner.getWorld()).getHandle());

        this.owner = owner;

        // Clear its current pathfinder goals, so all it does is follow it's owner.
        List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        // Our path finder
        this.targetSelector.a(1, new PathfinderGoalFollowPetOwner(this, 1.5));

        // Register entity
        addToMaps(getClass(), "Pet" + entityType.getName(), entityType.getTypeId());

    }

    private Object getPrivateField(String fieldName, Class clazz, Object object) {
        try {
            Field field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            return field.get(object);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Pet is now invincible */
    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    /* Since it's technically a zombie it get's caught on fire in the day, make this not happen. */
    @Override
    public void setOnFire(int i) {
        super.setOnFire(0);
    }

    private void addToMaps(Class clazz, String name, int id) {
        ((Map)getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
    }

    public Player getOwner() {
        return owner;
    }

}
