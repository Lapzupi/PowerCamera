package nl.svenar.powercamera;

import java.util.*;

import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.model.PreviousState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import nl.svenar.powercamera.model.ViewingMode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CameraRunnable extends BukkitRunnable {

    private int ticks = 0;

    private final PowerCamera plugin;
    private final Player player;

    //Camera Stuff
    private final Camera camera;
    private final ArrayList<Location> locationsPaths;


    private PreviousState previousState;

    public CameraRunnable(final PowerCamera plugin, final Player player, final Camera camera) {
        this.plugin = plugin;
        this.player = player;
        this.camera = camera;


        this.locationsPaths = generatePath();
    }

    private int calcMaxPoints(int duration, int singleFrameDurationMs) {
        return (duration * 1000) / singleFrameDurationMs;
    }

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

    private double calculateProgress(double start, double end, int progress, int progressMax) {
        return start + ((double) progress / (double) progressMax) * (end - start);
    }

    public CameraRunnable start() {
        this.previousState = PreviousState.fromPlayer(player);

        if (this.plugin.getConfigPlugin().getCameraEffects().isSpectator())
            player.setGameMode(GameMode.SPECTATOR);
        if (this.plugin.getConfigPlugin().getCameraEffects().isInvisible())
            player.setInvisible(true);

        this.plugin.getPlayerManager().setViewingMode(this.player.getUniqueId(), ViewingMode.VIEW);
        runTaskTimer(this.plugin, 1L, 1L);
        if (!locationsPaths.isEmpty()) {
            player.teleport(locationsPaths.get(0));
        }

        if (!this.player.hasPermission("powercamera.hidestartmessages"))
            this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera.getId() + "'!");
        return this;
    }

    public CameraRunnable stop() {
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

        if (!this.player.hasPermission("powercamera.hidestartmessages"))
            player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + this.camera.getId() + "' has ended!");
        return this;
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
                if (this.ticks > locationsPaths.size() - 2) {
                    this.stop();
                    return;
                }

                Location currentPos = locationsPaths.get(this.ticks);
                Location nextPoint = locationsPaths.get(this.ticks + 1);

                player.teleport(locationsPaths.get(this.ticks));

                final CameraPoint currentPoint = camera.getPoints().get(this.ticks);
                if(!currentPoint.getCommandsStart().isEmpty()) {
                    for(String command: currentPoint.getCommandsStart()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                    }
                }

                if(!currentPoint.getCommandsEnd().isEmpty()) {
                    for(String command: currentPoint.getCommandsStart()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
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
                plugin.getPlayerManager().setViewingMode(player.getUniqueId(),ViewingMode.NONE);
                player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
            }
            case NONE -> {
                //nothing
            }
        }

    }

    public CameraRunnable preview(Player player, int num, int previewTime) {
        List<CameraPoint> cameraPoints = camera.getPoints();

        if (num < 0)
            num = 0;

        if (num > cameraPoints.size() - 1)
            num = cameraPoints.size() - 1;


        player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview started of point " + (num + 1) + "!");
        player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Ending in " + previewTime + " seconds.");


        this.previousState = PreviousState.fromPlayer(player);

        final Location startingPoint = cameraPoints.get(0).getLocation();

        plugin.getPlayerManager().setViewingMode(player.getUniqueId(),ViewingMode.PREVIEW);
        if (this.plugin.getConfigPlugin().getCameraEffects().isSpectator())
            player.setGameMode(GameMode.SPECTATOR);
        if (this.plugin.getConfigPlugin().getCameraEffects().isInvisible())
            player.setInvisible(true);
        player.teleport(startingPoint);

        runTaskLater(this.plugin, previewTime * 20L);
        return this;
    }

    public Camera getCamera() {
        return camera;
    }

    private @NotNull ArrayList<Location> generatePath() {
        final ArrayList<Location> list = new ArrayList<>();
        if(camera.getPoints().size() == 1)
            list.add(camera.getPoints().get(0).getLocation());


        int maxPoints = calcMaxPoints(camera.getTotalDuration().intValue(), 60);
        for (int j = 0; j < maxPoints / (camera.getPoints().size() - 1) - 1; j++) {
            CameraPoint currentPoint = camera.getPoints().get(j);
            CameraPoint nextPoint = null;
            if(j+1 != camera.getPoints().size())
                nextPoint = camera.getPoints().get(j+1);

            if (nextPoint == null) {
                break;
            }

            if (nextPoint.getEasing() == CameraPoint.Easing.LINEAR) {
                list.add(translateLinearNext(currentPoint.getLocation(),nextPoint.getLocation(),j,maxPoints / (camera.getPoints().size() - 1) - 1));
            } else {
                list.add(nextPoint.getLocation());
            }
        }

        return list;
    }
}
