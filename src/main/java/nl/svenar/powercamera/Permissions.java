package nl.svenar.powercamera;

public final class Permissions {
    public static final String BYPASS_JOIN_CAMERA = "powercamera.bypass.joincamera";
    public static final String SHOW_START_MESSAGES = "powercamera.showstartmessages";

    public static class Command {
        public static final String RELOAD = "powercamera.command.reload";
        public static final String CREATE = "powercamera.command.create";
        public static final String REMOVE = "powercamera.command.remove";
        public static final String POINT_TP = "powercamera.command.point.tp";
        public static final String POINT_ADD = "powercamera.command.point.add";
        public static final String POINT_DELETE = "powercamera.command.point.delete";
        public static final String POINT_DURATION = "powercamera.command.point.duration";
        public static final String SELECT = "powercamera.command.select";
        public static final String PREVIEW = "powercamera.command.preview";
        public static final String INFO = "powercamera.command.info";
        public static final String START = "powercamera.command.start";
        public static final String STOP = "powercamera.command.stop";
        public static final String STATS = "powercamera.command.stats";
        public static final String INVISIBLE = "powercamera.command.invisible";
        public static final String HELP = "powercamera.command.help";

        private Command() {
            throw new UnsupportedOperationException("Util class");
        }
    }

    private Permissions() {
        throw new UnsupportedOperationException("Util class");
    }
}