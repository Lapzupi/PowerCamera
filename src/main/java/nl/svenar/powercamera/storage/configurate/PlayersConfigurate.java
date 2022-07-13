package nl.svenar.powercamera.storage.configurate;

import com.github.sarhatabaot.kraken.core.config.JsonConfigurateFile;
import com.github.sarhatabaot.kraken.core.config.Transformation;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.storage.PlayerStorage;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author sarhatabaot TODO
 */
public class PlayersConfigurate extends JsonConfigurateFile<PowerCamera> implements PlayerStorage {
    private Map<String, List<String>> cameraPlayedMap;

    public PlayersConfigurate(@NotNull final PowerCamera plugin) throws ConfigurateException {
        super(plugin, "", "players.json", "");
    }

    @Override
    protected void initValues() throws ConfigurateException {
        this.cameraPlayedMap = new HashMap<>();
        for(Map.Entry<Object, BasicConfigurationNode> entry: rootNode.childrenMap().entrySet()) {
            final String cameraId = entry.getKey().toString();
            final BasicConfigurationNode node = entry.getValue();
            this.cameraPlayedMap.putIfAbsent(cameraId, node.getList(String.class));
        }
    }

    @Override
    protected void builderOptions() {
        //nothing
    }

    @Override
    protected Transformation getTransformation() {
        return null;
    }

    public CompletableFuture<Boolean> hasPlayed(final String uuid, final String cameraId){
        return CompletableFuture.completedFuture(cameraPlayedMap.get(cameraId).contains(uuid));
    }

    public CompletableFuture<Void> addPlayer(final String uuid, final String cameraId) {
        if(!this.cameraPlayedMap.get(cameraId).contains(uuid)) {
            this.cameraPlayedMap.get(cameraId).add(uuid);
        }

        return null;
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid, final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid) {
        return null;
    }
}
