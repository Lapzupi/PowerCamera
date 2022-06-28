package nl.svenar.powercamera.config;



import com.github.sarhatabaot.kraken.core.config.HoconConfigurateFile;
import com.github.sarhatabaot.kraken.core.config.Transformation;
import nl.svenar.powercamera.PowerCamera;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public class PluginConfig extends HoconConfigurateFile<PowerCamera> {
	private int pointPreviewTime;
	private int singleFrameDuration;
	private Join join;
	private CameraEffects cameraEffects;

	public static class Join {
		private boolean showOnce;
		private String cameraId;

		public boolean isShowOnce() {
			return showOnce;
		}

		public String getCameraId() {
			return cameraId;
		}

		public void setShowOnce(final boolean showOnce) {
			this.showOnce = showOnce;
		}

		public void setCameraId(final String cameraId) {
			this.cameraId = cameraId;
		}
	}

	public static class CameraEffects {
		private boolean invisible;
		private boolean spectator;

		public boolean isInvisible() {
			return invisible;
		}

		public boolean isSpectator() {
			return spectator;
		}

		public void setInvisible(final boolean invisible) {
			this.invisible = invisible;
		}

		public void setSpectator(final boolean spectator) {
			this.spectator = spectator;
		}
	}

	public int getPointPreviewTime() {
		return pointPreviewTime;
	}

	public PluginConfig(@NotNull final PowerCamera plugin) throws ConfigurateException{
		super(plugin, "", "config.conf", "");
	}

	@Override
	protected void initValues() throws ConfigurateException {
		this.pointPreviewTime = rootNode.node("point-preview-time").getInt();
		this.singleFrameDuration = rootNode.node("single-frame-duration").getInt(60);

		this.join = new Join();
		this.join.setCameraId(rootNode.node("on-join").node("camera-id").getString());
		this.join.setShowOnce(rootNode.node("on-join").node("show-once").getBoolean());

		this.cameraEffects = new CameraEffects();
		this.cameraEffects.setSpectator(rootNode.node("camera-effects").node("spectator").getBoolean());
		this.cameraEffects.setInvisible(rootNode.node("camera-effects").node("invisible").getBoolean());
	}

	@Override
	protected void builderOptions() {

	}

	@Override
	protected Transformation getTransformation() {
		return null;
	}

	public Join getJoin() {
		return join;
	}

	public CameraEffects getCameraEffects() {
		return cameraEffects;
	}

	public int getSingleFrameDuration() {
		return singleFrameDuration;
	}
}
