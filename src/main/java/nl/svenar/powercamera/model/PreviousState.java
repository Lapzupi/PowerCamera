package nl.svenar.powercamera.model;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public record PreviousState (GameMode gameMode, Location location, boolean invisible){

    @Contract("_ -> new")
    public static @NotNull PreviousState fromPlayer(final @NotNull Player player) {
        return new PreviousState(player.getGameMode(), player.getLocation(), isPlayerInvisible(player));
    }

    public static boolean isPlayerInvisible(@NotNull Player player) {
        try {
            return player.isInvisible();
        } catch (NoSuchMethodError e) {
            return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
        }
    }
}
