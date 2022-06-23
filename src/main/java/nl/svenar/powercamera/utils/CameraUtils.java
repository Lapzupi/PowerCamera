package nl.svenar.powercamera.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class CameraUtils {

    public static Location getLocation(String rawWorld, double x, double y, double z, float yaw, float pitch) {
        World world = null;
        try {
            UUID worldUid = UUID.fromString(rawWorld);

            world = Bukkit.getServer().getWorld(worldUid);
        } catch (Exception e) {
            //ignored
        }

        if (world == null) {
            world = Bukkit.getServer().getWorld(rawWorld);
        }

        if (world == null) {
            world = Bukkit.getServer().getWorlds().get(0);
        }

        return new Location(world, x, y, z, yaw, pitch);
    }

    /*
     *
     * Vector pos = armorstand.getLocation().toVector(); Vector target =
     * Bukkit.getPlayer("svenar").getLocation().toVector(); Vector velocity =
     * target.subtract(pos);
     * armorstand.setVelocity(velocity.normalize().multiply(0.07));
     */
}
