package nl.svenar.powercamera.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.objects.Camera;
import nl.svenar.powercamera.objects.CameraPoint;
import nl.svenar.powercamera.utils.PaginationManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@CommandAlias("powercam|pcam|pc|cam")
@Description("Main command.")
public class BasePowerCommand extends BaseCommand {
    private final PowerCamera plugin;

    public BasePowerCommand(final PowerCamera plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("powercamera.command.reload")
    @Description("Reload the configuration files")
    public void onReload(final CommandSender sender) {
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloading the plugin configuration file...");
        this.plugin.getCoreConfig().load();
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloaded the plugin configuration file!");

        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloading the camera configuration file...");
        this.plugin.getCameraConfig().load();
        this.plugin.setCameras(this.plugin.getCameraConfig().getCameras());
        sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.GREEN + "Reloaded the camera configuration file!");
    }

    @Subcommand("help")
    @CommandPermission("powercamera.command.help")
    @Description("PowerCamera command list")
    @HelpCommand
    public void onHelp(final CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("list")
    @CommandPermission("powercamera.command.camera.list")
    @Description("List all available camera's")
    public void onCameraList(final CommandSender sender, final Integer page) {
        ArrayList<String> pageData = new ArrayList<>();

        for (Camera camera : plugin.getCameras().values()) {
            pageData.add(ChatColor.BLACK + "[" + ChatColor.DARK_GREEN + camera.getName() + ChatColor.BLACK + "] "
                    + ChatColor.GREEN + camera.getPoints().size() + " point(s)");
        }

        if (page < 0) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + " page must be 0 or higher.");
            return;
        }

        PaginationManager paginationManager = new PaginationManager(pageData, "list cameras",
                "pc list", page, 5);

        paginationManager.setHeaderMessage(ChatColor.DARK_GREEN + "/pc info <camera_name>" + ChatColor.GREEN + " for more info");
        paginationManager.send(sender);
    }

    @Subcommand("info")
    @CommandPermission("powercamera.command.camera.info")
    @Description("Show information about a specific camera")
    public void onCameraInfo(final CommandSender sender, final String cameraName,@Optional final Integer page) {
        if (this.plugin.getCamera(cameraName) == null) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + "A camera with that name does not exist.");
            return;
        }

        ArrayList<String> pageData = new ArrayList<>();
        Camera camera = this.plugin.getCamera(cameraName);

        int pointIndex = 1;
        for (CameraPoint point : camera.getPoints()) {
            pageData.add(ChatColor.DARK_GREEN + "Point: #" + pointIndex);
            pageData.add(ChatColor.DARK_GREEN + "- Type: " + ChatColor.GREEN + point.getType());
            pageData.add(ChatColor.DARK_GREEN + "- Easing: " + ChatColor.GREEN + point.getEasing());
            pageData.add(ChatColor.DARK_GREEN + "- Duration: " + ChatColor.GREEN + point.getDuration());
            pageData.add(ChatColor.DARK_GREEN + "- Location: ");
            pageData.add(ChatColor.DARK_GREEN + "  - World: " + ChatColor.GREEN + point.getLocation().getWorld().getName());
            pageData.add(ChatColor.DARK_GREEN + "  - Position: " + ChatColor.GREEN + "X:" + point.getLocation().getX() + ", " + "Y:" + point.getLocation().getY() + ", " + "Z:" + point.getLocation().getZ());
            pageData.add(ChatColor.DARK_GREEN + "  - Rotation: " + ChatColor.GREEN + "Yaw:" + point.getLocation().getYaw() + ", " + "Pitch:" + point.getLocation().getPitch());
            pageData.add(ChatColor.DARK_GREEN + "- Start commands: " + ChatColor.GREEN + (point.getStartCommands().size() > 0 ? "" : "None"));
            for (String command : point.getStartCommands()) {
                pageData.add(ChatColor.DARK_GREEN + "  - " + ChatColor.GREEN + command);
            }
            pageData.add(ChatColor.DARK_GREEN + "- End commands: " + ChatColor.GREEN + (point.getEndCommands().size() > 0 ? "" : "None"));
            for (String command : point.getEndCommands()) {
                pageData.add(ChatColor.DARK_GREEN + "  - " + ChatColor.GREEN + command);
            }
            pointIndex++;
        }

        if (page < 0) {
            sender.sendMessage(plugin.pluginChatPrefix() + ChatColor.RED + " page must be 0 or higher.");
            return;
        }

        PaginationManager paginationManager = new PaginationManager(pageData, cameraName,
                "pc info " + cameraName, page, 10);

        paginationManager.send(sender);


    }
}
