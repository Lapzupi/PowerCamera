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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

/**
 * @author sarhatabaot
 */
public class CameraStorage extends HoconConfigurateFile<PowerCamera> {
    public CameraStorage(@NotNull final PowerCamera plugin) throws ConfigurateException {
        super(plugin, "", "cameras.conf", "");
    }

    @Override
    protected void initValues() throws ConfigurateException {

    }

    @Override
    protected void builderOptions() {

    }

    @Override
    protected Transformation getTransformation() {
        return null;
    }

    public class CameraSerializer implements TypeSerializer<Camera> {

    }

    public class CameraPointSerializer implements TypeSerializer<CameraPoint> {
        @Override
        public CameraPoint deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
            return null;
        }

        @Override
        public void serialize(final Type type, @Nullable final CameraPoint obj, final ConfigurationNode node) throws SerializationException {

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

        @Override
        public Location deserialize(final Type type, final ConfigurationNode node) throws SerializationException {
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
