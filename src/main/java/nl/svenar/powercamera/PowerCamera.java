package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.kraken.core.db.ConnectionFactory;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.storage.configurate.CameraConfigurate;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.listeners.OnJoin;
import nl.svenar.powercamera.listeners.OnMove;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public class PowerCamera extends JavaPlugin {
    private final String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + getDescription().getName() + ChatColor.BLACK + "] ";

    private PluginConfig pluginConfig;
    private CameraConfigurate cameraConfigurate;

    //These Should be in managers/caches
    private PlayerManager playerManager;
    private CameraManager cameraManager;

    private ConnectionFactory<PowerCamera> connectionFactory;

    private final Instant startTime = Instant.now();

    @Override
    public void onEnable() {
        initConfig();
        try {
            this.cameraConfigurate = new CameraConfigurate(this);
        } catch (ConfigurateException e) {
            getLogger().severe(() -> "Could not initialize cameras.conf");
            getLogger().warning(() -> "Please fix any errors and reload the plugin.");
            e.printStackTrace();
        }

        initManagers();
        Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);

        initCommands();

        getLogger().info(() -> "Enabled %s v%s".formatted(getDescription().getName(), getDescription().getVersion()));

        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, 9107);
    }

    private void initConfig() {
        try {
            this.pluginConfig = new PluginConfig(this);
        } catch (ConfigurateException e) {
            getLogger().severe(() -> "Could not initialize config.conf");
            getLogger().warning(() -> "Please fix any errors and reload the plugin.");
            e.printStackTrace();
        }
    }

    private void initCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new PowerCameraCommand(this));
        commandManager.getCommandCompletions().registerCompletion("points", context ->
                generateIntsInRange(cameraManager.getCamera(playerManager.getSelectedCameraId(context.getPlayer().getUniqueId())).getPoints().size()).stream().map(Object::toString).toList()
        );
        commandManager.getCommandCompletions().registerCompletion("cameras", context -> cameraManager.getCameraIds());
    }

    private void initManagers() {
        this.playerManager = new PlayerManager();
        this.cameraManager = new CameraManager(this);
    }

    private void initStorage() {
        String databaseType = pluginConfig.getDatabase().getType();
        switch (databaseType){
            case "hocon": {

            }
            case "sql": {

            }
            case "sqlite": {

            }
        }

    }

    @Override
    public void onDisable() {
        getLogger().info(() -> "Disabled %s v%s".formatted(getDescription().getName(), getDescription().getVersion()));
    }

    public String getPluginChatPrefix() {
        return pluginChatPrefix;
    }


    public PluginConfig getConfigPlugin() {
        return pluginConfig;
    }


    public Instant getStartTime() {
        return startTime;
    }

    public CameraConfigurate getCameraStorage() {
        return cameraConfigurate;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }


    private @NotNull List<Integer> generateIntsInRange(int max) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < max; i++){
			list.add(i);
		}
		return list;
    }
}
