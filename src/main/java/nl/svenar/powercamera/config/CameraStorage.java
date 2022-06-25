package nl.svenar.powercamera.config;

import com.github.sarhatabaot.kraken.core.config.HoconConfigurateFile;
import com.github.sarhatabaot.kraken.core.config.Transformation;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sarhatabaot
 */
public class CameraStorage extends HoconConfigurateFile<PowerCamera> {
    private Map<String, Camera> cameras;

    public CameraStorage(@NotNull final PowerCamera plugin) throws ConfigurateException {
        super(plugin, "", "cameras.conf", "");
    }

    @Override
    protected void initValues() throws ConfigurateException {
        this.cameras = new HashMap<>();

        final CommentedConfigurationNode camerasNode = rootNode.node("cameras");
        for(Map.Entry<Object, CommentedConfigurationNode> entry: camerasNode.childrenMap().entrySet()) {
            final String cameraId = entry.getKey().toString();
            final Camera camera = entry.getValue().get(Camera.class);
            this.cameras.put(cameraId,camera);
        }
    }

    @Override
    protected void builderOptions() {
        loaderBuilder.defaultOptions(opts -> opts.serializers(builder ->
                builder.register(Location.class,LocationSerializer.INSTANCE)
                        .register(CameraPoint.class, CameraPointSerializer.INSTANCE)
                        .register(Camera.class,CameraSerializer.INSTANCE)));
    }

    @Override
    protected Transformation getTransformation() {
        return null;
    }

    @Nullable
    public Camera getCamera(final String id) {
        if(!hasCamera(id))
            return null;

        return cameras.get(id);
    }

    /**
     * Tries to get a camera directly.
     * @param id camera id
     * @return Camera object
     */
    public Camera getRawCamera(final String id) throws SerializationException {
        return rootNode.node("camera").node(id).get(Camera.class);
    }

    public boolean hasCamera(final String id) {
        return cameras.containsKey(id);
    }

    public static class CameraSerializer implements TypeSerializer<Camera> {
        public static final CameraSerializer INSTANCE = new CameraSerializer();
        private static final String ALIAS = "alias";
        private static final String RETURN_TO_ORIGIN = "return-to-origin";
        private static final String POINTS = "points";

        @Override
        public Camera deserialize(final Type type, final @NotNull ConfigurationNode node) throws SerializationException {
            Object key = node.key();
            if(key == null) {
                throw new SerializationException("Could not obtain node key");
            }

            final String id = key.toString();
            final boolean returnToOrigin = node.node(RETURN_TO_ORIGIN).getBoolean();
            final String alias = node.node(ALIAS).getString();
            final List<CameraPoint> points = node.node(POINTS).getList(CameraPoint.class);

            return new Camera(id,alias, points,returnToOrigin);
        }

        @Override
        public void serialize(final Type type, @Nullable final Camera camera, final ConfigurationNode node) throws SerializationException {
            if(camera == null) {
                node.raw(null);
                return;
            }

            node.node(ALIAS).set(camera.getAlias());
            node.node(RETURN_TO_ORIGIN).set(camera.isReturnToOrigin());
            node.node(POINTS).setList(CameraPoint.class,camera.getPoints());
        }
    }

    public static class CameraPointSerializer implements TypeSerializer<CameraPoint> {
        public static final CameraPointSerializer INSTANCE = new CameraPointSerializer();

        private static final String TYPE = "type";
        private static final String EASING = "easing";
        private static final String DURATION = "duration";
        private static final String LOCATION = "location";

        // Commands
        private static final String COMMANDS = "commands";
        private static final String START = "start";
        private static final String END = "end";

        @Override
        public CameraPoint deserialize(final Type type, final @NotNull ConfigurationNode node) throws SerializationException {
            final CameraPoint.Type pointType = node.node(TYPE).get(CameraPoint.Type.class);
            final CameraPoint.Easing easing = node.node(EASING).get(CameraPoint.Easing.class);

            final Double duration = node.node(DURATION).getDouble();
            final Location location = node.node(LOCATION).get(Location.class);

            final List<String> commandsStart = node.node(COMMANDS).node(START).getList(String.class);
            final List<String> commandsEnd = node.node(COMMANDS).node(END).getList(String.class);
            return new CameraPoint(pointType,easing,duration,location,commandsStart,commandsEnd);
        }

        @Override
        public void serialize(final Type type, @Nullable final CameraPoint point, final ConfigurationNode node) throws SerializationException {
            if (point == null) {
                node.raw(null);
                return;
            }

            node.node(TYPE).set(point.getType());
            node.node(EASING).set(point.getEasing());
            node.node(DURATION).set(point.getDuration());
            node.node(LOCATION).set(point.getLocation());
            node.node(COMMANDS).node(START).set(point.getCommandsStart());
            node.node(COMMANDS).node(END).set(point.getCommandsEnd());
        }
    }

    public static final class LocationSerializer implements TypeSerializer<Location> {
        public static final LocationSerializer INSTANCE = new LocationSerializer();

        private static final String WORLD = "world";
        private static final String X = "x";
        private static final String Y = "y";
        private static final String Z = "z";
        private static final String YAW = "yaw";
        private static final String PITCH = "pitch";

        @Contract("_, _ -> new")
        @Override
        public @NotNull Location deserialize(final Type type, final @NotNull ConfigurationNode node) throws SerializationException {
            final ConfigurationNode worldNode = node.node(WORLD);
            final ConfigurationNode xNode = node.node(X);
            final ConfigurationNode yNode = node.node(Y);
            final ConfigurationNode zNode = node.node(Z);
            final ConfigurationNode yawNode = node.node(YAW);
            final ConfigurationNode pitchNode = node.node(PITCH);

            final World world = Bukkit.getWorld(worldNode.getString(WORLD));

            if (world == null)
                throw new SerializationException("This world does not exist.");

            return new Location(world, xNode.getDouble(), yNode.getDouble(), zNode.getDouble(), yawNode.getFloat(), pitchNode.getFloat());
        }

        @Override
        public void serialize(final Type type, @Nullable final Location location, final ConfigurationNode node) throws SerializationException {
            if (location == null) {
                node.raw(null);
                return;
            }

            final String worldName = (location.getWorld() == null) ? WORLD : location.getWorld().getName();
            node.node(WORLD).set(worldName);
            node.node(X).set(location.getX());
            node.node(Y).set(location.getY());
            node.node(Z).set(location.getZ());
            node.node(YAW).set(location.getYaw());
            node.node(PITCH).set(location.getPitch());
        }
    }
}
