package nl.svenar.powercamera.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.svenar.powercamera.CameraRunnableView;
import nl.svenar.powercamera.Permissions;
import nl.svenar.powercamera.managers.PlayerManager;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.model.ViewingMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

@CommandAlias("powercamera|pc|camera|pcam")
public class PowerCameraCommand extends BaseCommand {
    private final PowerCamera plugin;

    public PowerCameraCommand(final PowerCamera plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @CommandPermission(Permissions.Command.HELP)
    public void onHelp(final @NotNull CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission(Permissions.Command.RELOAD)
    public void onReload() {
        for (Map.Entry<UUID, CameraRunnableView> entry : plugin.getPlayerManager().getRunningTasks().entrySet()) {
            entry.getValue().stop();
        }
        this.plugin.getConfigPlugin().reloadConfig();
        //check for storage changes
    }

    @Subcommand("create")
    @CommandPermission(Permissions.Command.CREATE)
    public void onCreate(final Player sender, final String cameraId) {
        if (plugin.getCameraManager().hasCamera(cameraId)) {
            sender.sendMessage("%sA camera with the id '%s' already exists.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, cameraId));
            return;
        }

        plugin.getCameraManager().createCamera(cameraId);

        final Camera camera = plugin.getCameraManager().getCamera(cameraId);
        plugin.getCameraManager().addPoint(new CameraPoint(camera.getId(),CameraPoint.Type.MOVE, CameraPoint.Easing.LINEAR, 60D, sender.getLocation()));
        sender.sendMessage("%sCamera '%s' created!".formatted(plugin.getPluginChatPrefix() + ChatColor.GREEN, cameraId));
        onSelect(sender, cameraId);
    }

    @Subcommand("remove")
    @CommandCompletion("@cameras")
    @CommandPermission(Permissions.Command.REMOVE)
    public void onRemove(final Player sender, final String cameraId) {
        if (!plugin.getCameraManager().hasCamera(cameraId)) {
            sender.sendMessage("%sA camera with the id '%s' doesn't exist.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, cameraId));
            return;
        }

        plugin.getCameraStorage().deleteCamera(cameraId);
        sender.sendMessage("%sCamera '%s' removed.".formatted(plugin.getPluginChatPrefix() + ChatColor.GREEN, cameraId));
    }

    @Subcommand("point")
    public class PointSubCommand extends BaseCommand {
        @Subcommand("tp")
        @CommandPermission(Permissions.Command.POINT_TP)
        public void onTeleport(final @NotNull Player player, final int num) {
            if (!plugin.getPlayerManager().hasSelectedCamera(player.getUniqueId())) {
                player.sendMessage("%sPlayer %s does not have a selected camera.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, player.getName()));
                return;
            }

            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(player.getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                //mismatched ids, please restart your server
                plugin.getLogger().severe("Mismatched ids. Something went wrong. Please restart your server.");
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            final CameraPoint cameraPoint = camera.getPoints().get(num);
            player.teleport(cameraPoint.getLocation());
            player.sendMessage("Teleported to point %d @ %s".formatted(num,cameraPoint.getLocation().toString()));
        }

        @Subcommand("add")
        @CommandPermission(Permissions.Command.POINT_ADD)
        public void onAdd(final @NotNull Player player, final CameraPoint.Type type, final CameraPoint.Easing easing, final Double duration) {
            if (!plugin.getPlayerManager().hasSelectedCamera(player.getUniqueId())) {
                player.sendMessage("%sPlayer %s does not have a selected camera.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, player.getName()));
                return;
            }

            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(player.getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                //mismatched ids, please restart your server
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            final CameraPoint cameraPoint = new CameraPoint(camera.getId(),type, easing, duration, player.getLocation());
            plugin.getCameraManager().addPoint(cameraPoint);
            player.sendMessage("%sAdded point '%s' to camera: '%s'".formatted(plugin.getPluginChatPrefix() + ChatColor.GREEN, cameraPoint.toString(), camera.getId()));
        }

        @Subcommand("delete")
        @CommandCompletion("@points")
        @CommandPermission(Permissions.Command.POINT_DELETE)
        public void onDelete(final @NotNull Player player, final int num) {
            if (!plugin.getPlayerManager().hasSelectedCamera(player.getUniqueId())) {
                player.sendMessage("%sPlayer %s does not have a selected camera.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, player.getName()));
                return;
            }

            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(player.getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                //mismatched ids, please restart your server
                plugin.getLogger().severe("Mismatched ids. Something went wrong. Please restart your server.");
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            plugin.getCameraManager().removePoint(camera.getId(), num);
            player.sendMessage("%sRemoved point %d from camera '%s'.".formatted(plugin.getPluginChatPrefix() + ChatColor.GREEN, num, selectedCameraId));
        }

        @Subcommand("duration|setduration")
        @CommandCompletion("@points")
        @CommandPermission(Permissions.Command.POINT_DURATION)
        public void onSetDuration(final @NotNull Player player, final int num, final Double duration) {
            if (!plugin.getPlayerManager().hasSelectedCamera(player.getUniqueId())) {
                player.sendMessage("%sPlayer %s does not have a selected camera.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, player.getName()));
                return;
            }

            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(player.getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                //mismatched ids, please restart your server
                plugin.getLogger().severe("Mismatched ids. Something went wrong. Please restart your server.");
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            if (num >= camera.getPoints().size()) {
                player.sendMessage("%sPlease specify a number between 0-%d".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, camera.getPoints().size() - 1));
                return;
            }

            final CameraPoint cameraPoint = camera.getPoints().get(num);
            cameraPoint.setDuration(duration);
            camera.getPoints().set(num, cameraPoint);
            player.sendMessage("Set duration of camera %s @ point %d to %.2f".formatted(camera.getId(), num, duration));
        }
    }

    @Subcommand("select")
    @CommandCompletion("@cameras")
    @CommandPermission(Permissions.Command.SELECT)
    public void onSelect(final Player sender, final String cameraId) {
        if (!plugin.getCameraManager().hasCamera(cameraId)) {
            sender.sendMessage("%sCamera '%s' does not exist.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, cameraId));
            return;
        }

        plugin.getPlayerManager().setSelectedCameraId(sender.getUniqueId(), cameraId);
        sender.sendMessage(plugin.getPluginChatPrefix() + ChatColor.GREEN + "Camera '" + cameraId + "' selected!");
    }

    @Subcommand("preview")
    @CommandCompletion("@points")
    @CommandPermission(Permissions.Command.PREVIEW)
    public void onPreview(final @NotNull Player sender, final int point, final int previewTime) {
        if (!plugin.getPlayerManager().hasSelectedCamera(sender.getUniqueId())) {
            return;
        }
        final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(sender.getUniqueId());

        ViewingMode viewingMode = plugin.getPlayerManager().getViewingMode(sender.getUniqueId());
        if (viewingMode != ViewingMode.NONE) {
            sender.sendMessage("Cannot start preview, current mode=" + viewingMode);
            return;
        }

        final PlayerManager playerManager = plugin.getPlayerManager();
        if (playerManager.hasRunningTask(sender.getUniqueId())) {
            sender.sendMessage("You already have a running task.");
            return;
        }

        final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
        playerManager.setRunningTask(sender.getUniqueId(), new CameraRunnableView(plugin, sender, camera).preview(sender, point, previewTime));
    }

    @Subcommand("info")
    @CommandCompletion("@cameras")
    @Description("Display information on a selected camera, optionally specify a specific camera.")
    public void onInfo(final Player sender, @Optional final String cameraId) {
        if (cameraId == null) {
            if (!plugin.getPlayerManager().hasSelectedCamera(sender.getUniqueId())) {
                return;
            }
            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(sender.getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            sendInfoMessage(sender, camera);
            return;
        }

        if (!plugin.getCameraManager().hasCamera(cameraId)) {
            sender.sendMessage("%sA camera with the id '%s' doesn't exist.".formatted(plugin.getPluginChatPrefix() + ChatColor.RED, cameraId));
            return;
        }

        final Camera camera = plugin.getCameraManager().getCamera(cameraId);
        sendInfoMessage(sender, camera);
    }

    private void sendInfoMessage(final @NotNull Player sender, final @NotNull Camera camera) {
        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
        sender.sendMessage(ChatColor.DARK_GREEN + "Camera name: " + ChatColor.GREEN + camera.getId());
        sender.sendMessage(ChatColor.DARK_GREEN + "Path duration: " + ChatColor.GREEN + camera.getTotalDuration() + " seconds");
        sender.sendMessage(ChatColor.DARK_GREEN + "Camera points (" + ChatColor.GREEN + camera.getPoints().size() + ChatColor.DARK_GREEN + "):");

        for (int i = 0; i < camera.getPoints().size() - 1; i++) {
            final String pointInfo = camera.getPoints().get(i).toString();
            sender.sendMessage(ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + pointInfo);
        }
    }

    @Subcommand("start")
    @CommandCompletion("@players @cameras")
    @CommandPermission(Permissions.Command.START)
    public void onStart(final Player sender, final OnlinePlayer target, @Optional final String cameraId) {
        if (cameraId == null) {
            if (!plugin.getPlayerManager().hasSelectedCamera(target.getPlayer().getUniqueId())) {
                return;
            }

            final String selectedCameraId = plugin.getPlayerManager().getSelectedCameraId(target.getPlayer().getUniqueId());
            if (!plugin.getCameraManager().hasCamera(selectedCameraId)) {
                return;
            }

            final Camera camera = plugin.getCameraManager().getCamera(selectedCameraId);
            CameraRunnableView runnable = new CameraRunnableView(plugin, target.getPlayer(), camera);
            plugin.getPlayerManager().setRunningTask(target.getPlayer().getUniqueId(), runnable.start());
            return;
        }

        if (!plugin.getCameraManager().hasCamera(cameraId)) {
            return;
        }

        final Camera camera = plugin.getCameraManager().getCamera(cameraId);
        CameraRunnableView runnable = new CameraRunnableView(plugin, target.getPlayer(), camera);
        plugin.getPlayerManager().setRunningTask(target.getPlayer().getUniqueId(), runnable.start());
    }


    @Subcommand("stop")
    @CommandPermission(Permissions.Command.STOP)
    public void onStop(final Player sender, final @NotNull OnlinePlayer target) {
        if (!plugin.getPlayerManager().hasRunningTask(target.getPlayer().getUniqueId())) {
            sender.sendMessage("%sPlayer %s does not have an active camera", plugin.getPluginChatPrefix() + ChatColor.RED, target.getPlayer().getName());
            return;
        }

        CameraRunnableView task = plugin.getPlayerManager().getCurrentRunningCameraTask(target.getPlayer().getUniqueId());
        if (task != null && !task.isCancelled()) {
            task.stop();
        }
    }

    @Subcommand("stats")
    @CommandPermission(Permissions.Command.STATS)
    public void onStats(final @NotNull CommandSender sender) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Instant currentTime = Instant.now();

        final String invisibilityMode = getInvisibilityMode();

        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "----------" + ChatColor.AQUA + plugin.getDescription().getName() + ChatColor.DARK_AQUA + "----------" + ChatColor.BLUE + "===");
        sender.sendMessage(ChatColor.DARK_GREEN + "Server version: " + ChatColor.GREEN + Bukkit.getVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Bukkit version: " + ChatColor.GREEN + Bukkit.getServer().getBukkitVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Java version: " + ChatColor.GREEN + System.getProperty("java.version"));
        sender.sendMessage(ChatColor.DARK_GREEN + plugin.getDescription().getName() + " Version: " + ChatColor.GREEN + plugin.getDescription().getVersion());
        sender.sendMessage(ChatColor.DARK_GREEN + "Plugin uptime: " + ChatColor.GREEN + format.format(Duration.between(plugin.getStartTime(), currentTime).toMillis()));
        sender.sendMessage(ChatColor.DARK_GREEN + "Registered cameras: " + ChatColor.GREEN + plugin.getCameraStorage().getTotalAmountCameras());
        sender.sendMessage(ChatColor.DARK_GREEN + "Invisibility mode: " + ChatColor.GREEN + invisibilityMode);
        sender.sendMessage(ChatColor.BLUE + "===" + ChatColor.DARK_AQUA + "-------------------------------" + ChatColor.BLUE + "===");
    }

    private @NotNull String getInvisibilityMode() {
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode") && this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
            return "spectator & invisible";
        }
        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.spectator-mode")) {
            return "spectator";
        }

        if (this.plugin.getConfigPlugin().getConfig().getBoolean("camera-effects.invisible")) {
            return "invisible";
        }

        return "none";
    }


    @Subcommand("invisible")
    @CommandPermission(Permissions.Command.INVISIBLE)
    public void onInvisible(final @NotNull Player player, final boolean invisible) {
        player.setInvisible(invisible);
    }

}
