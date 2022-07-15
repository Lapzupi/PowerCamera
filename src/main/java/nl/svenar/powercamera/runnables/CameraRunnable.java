package nl.svenar.powercamera.runnables;

import nl.svenar.powercamera.CameraRunnableView;
import nl.svenar.powercamera.Permissions;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.model.PreviousState;
import nl.svenar.powercamera.model.ViewingMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sarhatabaot
 */
public abstract class CameraRunnable extends BukkitRunnable {
    private int ticks = 0;
    private final PowerCamera plugin;
    private final Player player;

    //Camera Stuff
    private final Camera camera;
    private ArrayList<Location> locationsPaths;

    private int currentCameraPointPosition = 0;


    private PreviousState previousState;

    public CameraRunnable(final PowerCamera plugin, final Player player, final @NotNull Camera camera) {
        this.plugin = plugin;
        this.player = player;
        this.camera = camera;


        this.locationsPaths = generatePath(camera.getPoints());
    }

    //Calculate the maximum amount of points between 2 points in duration
    private int calcMaxPoints(int duration, int singleFrameDurationMs) {
        return (duration * 1000) / singleFrameDurationMs;
    }

    //Calculate the next sub point location
    private @NotNull Location translateLinearNext(@NotNull Location point, @NotNull Location pointNext, int progress, int progressMax) {
        if (!point.getWorld().getUID().toString().equals(pointNext.getWorld().getUID().toString())) {
            return pointNext;
        }

        Location newPoint = new Location(pointNext.getWorld(), point.getX(), point.getY(), point.getZ());

        newPoint.setX(calculateProgress(point.getX(), pointNext.getX(), progress, progressMax));
        newPoint.setY(calculateProgress(point.getY(), pointNext.getY(), progress, progressMax));
        newPoint.setZ(calculateProgress(point.getZ(), pointNext.getZ(), progress, progressMax));
        newPoint.setYaw((float) calculateProgress(point.getYaw(), pointNext.getYaw(), progress, progressMax));
        newPoint.setPitch((float) calculateProgress(point.getPitch(), pointNext.getPitch(), progress, progressMax));

        return newPoint;
    }

    private double calculateProgress(double firstPoint, double lastPoint, int subPointPosition, int maxSubPoints) {
        return firstPoint + ((double) subPointPosition / (double) maxSubPoints) * (lastPoint - firstPoint);
    }

    public void stop() {
        this.plugin.getPlayerManager().setViewingMode(this.player.getUniqueId(), ViewingMode.VIEW);
        try {
            this.cancel();
        } catch (Exception e) {
            //ignored
        }

        player.teleport(previousState.location());
        if (this.plugin.getConfigPlugin().getCameraEffects().isSpectator())
            player.setGameMode(previousState.gameMode());
        if (this.plugin.getConfigPlugin().getCameraEffects().isInvisible())
            player.setInvisible(previousState.invisible());

        if (this.player.hasPermission(Permissions.SHOW_START_MESSAGES))
            player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + this.camera.getId() + "' has ended!");

        currentCameraPointPosition = 0;
    }

    @Contract("_, _ -> new")
    private @NotNull Vector calculateVelocity(@NotNull Location start, @NotNull Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }

    @Override
    public void run() {
        ViewingMode playerViewingMode = plugin.getPlayerManager().getViewingMode(player.getUniqueId());
        switch (playerViewingMode) {
            case VIEW -> {
                if (this.ticks > locationsPaths.size() - 2 || currentCameraPointPosition == camera.getPoints().size()) {
                    this.stop();
                    return;
                }

                Location currentPos = locationsPaths.get(this.ticks);
                Location nextPoint = locationsPaths.get(this.ticks + 1);

                player.teleport(locationsPaths.get(this.ticks));

                final CameraPoint currentPoint = camera.getPoints().get(currentCameraPointPosition);
                if(currentPoint.getLocation().equals(currentPos)) {
                    currentCameraPointPosition++;
                    if (!currentPoint.getCommandsStart().isEmpty()) {
                        for (String command : currentPoint.getCommandsStart()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        }
                    }

                    if (!currentPoint.getCommandsEnd().isEmpty()) {
                        for (String command : currentPoint.getCommandsStart()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        }
                    }
                }

                player.setVelocity(calculateVelocity(currentPos, nextPoint));

                this.ticks += 1;
            }
            case PREVIEW -> {
                player.teleport(previousState.location());
                if (this.plugin.getConfigPlugin().getCameraEffects().isSpectator())
                    player.setGameMode(previousState.gameMode());
                if (this.plugin.getConfigPlugin().getCameraEffects().isInvisible())
                    player.setInvisible(previousState.invisible());
                plugin.getPlayerManager().setViewingMode(player.getUniqueId(), ViewingMode.NONE);
                player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
            }
            case NONE -> {
                //nothing
            }
        }

    }
    private double calcPointPortionOfTotalDuration(double duration, double total) {
        return duration / total;
    }

    public Camera getCamera() {
        return camera;
    }

    private @NotNull ArrayList<Location> generatePath(@NotNull List<CameraPoint> points) {
        final ArrayList<Location> list = new ArrayList<>();
        final int singleFrameDuration = plugin.getConfigPlugin().getSingleFrameDuration();
        if (points.size() == 1)
            list.add(points.get(0).getLocation());

        for (int i = 0; i < points.size() - 1; i++) {
            CameraPoint currentPoint = points.get(i);
            CameraPoint nextPoint = points.get(i + 1);

            int maxSubPoints = calcMaxPoints(nextPoint.getDuration().intValue(), singleFrameDuration);
            for (int cursor = 0; cursor <= maxSubPoints; cursor++) {
                if (nextPoint.getEasing() == CameraPoint.Easing.LINEAR) {
                    list.add(translateLinearNext(currentPoint.getLocation(), nextPoint.getLocation(), cursor, maxSubPoints));
                } else {
                    list.add(nextPoint.getLocation()); //maybe not needed
                }
            }
        }

        return list;
    }
}
