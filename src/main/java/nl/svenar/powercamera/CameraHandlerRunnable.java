package nl.svenar.powercamera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.PreviousState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import nl.svenar.powercamera.model.ViewingMode;

public class CameraHandlerRunnable extends BukkitRunnable {

    private int ticks = 0;

    private final PowerCamera plugin;
    private final Player player;

    //Camera Stuff
    private Camera camera;

    private String camera_name;
    private ArrayList<Location> cameraPathPoints = new ArrayList<>();
    private HashMap<Integer, ArrayList<String>> cameraPathCommands = new HashMap<>();

    private PreviousState previousState;

    public CameraHandlerRunnable(final PowerCamera plugin,final Player player, String cameraName) {
        this.plugin = plugin;
        this.player = player;
        this.camera_name = cameraName;
    }

    public CameraHandlerRunnable(final PowerCamera plugin, final Player player, final Camera camera) {
        this.plugin = plugin;
        this.player = player;
        this.camera = camera;

        this.camera_name = camera.getId();
    }

    private int calcMaxPoints(int duration, int singleFrameDurationMs) {
        return (duration * 1000) / singleFrameDurationMs;
    }

    public CameraHandlerRunnable generatePath() {
        int singleFrameDurationMs = 50;
        int maxPoints = calcMaxPoints(this.plugin.getConfigCameras().getDuration(this.camera_name), singleFrameDurationMs);

        List<String> rawCameraPoints = this.plugin.getConfigCameras().getPoints(this.camera_name);
        List<String> rawCameraMovePoints = getMovementPoints(rawCameraPoints);

        if (rawCameraMovePoints.size() - 1 == 0) {
            for (int j = 0; j < maxPoints - 1; j++) {
                this.cameraPathPoints.add(Util.deserializeLocation(rawCameraMovePoints.get(0).split(":", 2)[1]));
            }
        } else {
            for (int i = 0; i < rawCameraMovePoints.size() - 1; i++) {
                String raw_point = rawCameraMovePoints.get(i).split(":", 2)[1];
                String raw_point_next = rawCameraMovePoints.get(i + 1).split(":", 2)[1];
                String easing = rawCameraMovePoints.get(i + 1).split(":", 2)[0];

                Location point = Util.deserializeLocation(raw_point);
                Location point_next = Util.deserializeLocation(raw_point_next);

                this.cameraPathPoints.add(point);
                for (int j = 0; j < maxPoints / (rawCameraMovePoints.size() - 1) - 1; j++) {
                    if (easing.equalsIgnoreCase("linear")) {
                        this.cameraPathPoints.add(translateLinear(point, point_next, j, maxPoints / (rawCameraMovePoints.size() - 1) - 1));
                    }
                    if (easing.equalsIgnoreCase("teleport")) {
                        this.cameraPathPoints.add(point_next);
                    }
                }
            }
        }

        int commandIndex = 0;
        for (String raw_point : rawCameraPoints) {
            String type = raw_point.split(":", 3)[0];
            //String easing = raw_point.split(":", 3)[1];
            String data = raw_point.split(":", (Objects.equals(type, "location") ? 3 : 2))["location".equals(type) ? 2 : 1];

            if (type.equalsIgnoreCase("location")) {
                commandIndex += 1;
            }

            if (type.equalsIgnoreCase("command")) {
                int index = ((commandIndex) * maxPoints / (rawCameraMovePoints.size()) - 1);
                index = commandIndex == 0 ? 0 : index - 1;
                index = Math.max(index, 0);
                if (!this.cameraPathCommands.containsKey(index))
                    this.cameraPathCommands.put(index, new ArrayList<>());
                this.cameraPathCommands.get(index).add(data);
            //this.camera_path_commands.put(index, raw_camera_points.get(0));
            }
        }

        return this;
    }

    private List<String> getMovementPoints(List<String> rawCameraPoints) {
        List<String> output = new ArrayList<>();
        for (String raw_point : rawCameraPoints) {
            String[] pointData = raw_point.split(":", 2);
            if (pointData[0].equalsIgnoreCase("location")) {
                output.add(pointData[1]);
            }
        }
        return output;
    }

    private Location translateLinear(Location point, Location pointNext, int progress, int progressMax) {
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

    public CameraHandlerRunnable start() {
        this.previousState = PreviousState.fromPlayer(player);

        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
            player.setGameMode(GameMode.SPECTATOR);
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
            player.setInvisible(true);

        this.plugin.player_camera_mode.put(this.player.getUniqueId(), ViewingMode.VIEW);
        runTaskTimer(this.plugin, 1L, 1L);
        if (!cameraPathPoints.isEmpty()) {
            player.teleport(cameraPathPoints.get(0));
        }

        if (!this.player.hasPermission("powercamera.hidestartmessages"))
            this.player.sendMessage(this.plugin.getPluginChatPrefix() + ChatColor.GREEN + "Viewing the path of camera '" + this.camera_name + "'!");
        return this;
    }

    public CameraHandlerRunnable stop() {
        plugin.player_camera_mode.put(player.getUniqueId(), ViewingMode.NONE);
        try {
            this.cancel();
        } catch (Exception e) {
            //ignored
        }

        player.teleport(previousState.location());
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
            player.setGameMode(previousState.gameMode());
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
            player.setInvisible(previousState.invisible());

        if (!this.player.hasPermission("powercamera.hidestartmessages"))
            player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "The path of camera '" + camera_name + "' has ended!");
        return this;
    }

    private Vector calculateVelocity(Location start, Location end) {
        return new Vector(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }

    @Override
    public void run() {
        ViewingMode playerViewingMode = plugin.player_camera_mode.get(player.getUniqueId());
        switch (playerViewingMode) {
            case VIEW -> {
                if (this.ticks > cameraPathPoints.size() - 2) {
                    this.stop();
                    return;
                }

                Location currentPos = cameraPathPoints.get(this.ticks);
                Location nextPoint = cameraPathPoints.get(this.ticks + 1);

                player.teleport(cameraPathPoints.get(this.ticks));

                if (cameraPathCommands.containsKey(this.ticks)) {
                    for (String cmd : cameraPathCommands.get(this.ticks)) {
                        String command = cmd.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }

                player.setVelocity(calculateVelocity(currentPos, nextPoint));

                this.ticks += 1;
            }
            case PREVIEW -> {
                player.teleport(previousState.location());
                if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
                    player.setGameMode(previousState.gameMode());
                if (plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
                    player.setInvisible(previousState.invisible());
                plugin.player_camera_mode.put(player.getUniqueId(), ViewingMode.NONE);
                player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview ended!");
            }
            case NONE -> {
                //nothing
            }
        }

    }

    public CameraHandlerRunnable preview(Player player, int num, int previewTime) {
        List<String> cameraPoints = plugin.getConfigCameras().getPoints(camera_name);

        if (num < 0)
            num = 0;

        if (num > cameraPoints.size() - 1)
            num = cameraPoints.size() - 1;

        if (!cameraPoints.get(num).split(":", 2)[0].equalsIgnoreCase("location")) {
            player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.RED + "Point " + (num + 1) + " is not a location!");
            return this;
        }

        player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Preview started of point " + (num + 1) + "!");
        player.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Ending in " + previewTime + " seconds.");

        this.previousState = PreviousState.fromPlayer(player);
        Location point = Util.deserializeLocation(cameraPoints.get(num).split(":", 3)[2]);

        plugin.player_camera_mode.put(player.getUniqueId(), ViewingMode.PREVIEW);
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode"))
            player.setGameMode(GameMode.SPECTATOR);
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible"))
            player.setInvisible(true);
        player.teleport(point);

        runTaskLater(this.plugin, previewTime * 20L);
        return this;
    }

}
