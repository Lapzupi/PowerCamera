package nl.svenar.powercamera.config;

import com.github.sarhatabaot.kraken.core.config.JsonConfigurateFile;
import com.github.sarhatabaot.kraken.core.config.Transformation;
import nl.svenar.powercamera.PowerCamera;
import org.jetbrains.annotations.NotNull;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sarhatabaot TODO
 */
public class PlayersStorage extends JsonConfigurateFile<PowerCamera> {
    private Map<String, List<String>> cameraPlayedMap;

    protected PlayersStorage(@NotNull final PowerCamera plugin) throws ConfigurateException {
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

    public boolean hasPlayed(final String uuid, final String cameraId){
        return cameraPlayedMap.get(cameraId).contains(uuid);
    }

    public void addPlayer(final String uuid, final String cameraId) {
        if(this.cameraPlayedMap.get(cameraId).contains(uuid))
            return;

        this.cameraPlayedMap.get(cameraId).add(uuid);
    }
}
