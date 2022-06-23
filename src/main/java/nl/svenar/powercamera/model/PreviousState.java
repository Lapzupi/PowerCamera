package nl.svenar.powercamera.model;

import org.bukkit.GameMode;
import org.bukkit.Location;

/**
 * @author sarhatabaot
 */
public record PreviousState (GameMode gameMode, Location location, boolean invisible){
}
