package club.anlan.lanlife.commponent.netty.constant;

/**
 * Constant
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:14
 */
public class Constant {

    public static final byte HEADER_SIZE = 4;

    public static final int TYPE_SIZE = 1;

    public static final int SERIAL_NUMBER_SIZE = 8;

    public static final int URI_LENGTH_SIZE = 1;

    public static final int CLIENT_READ_IDLE_TIME = 0;
    public static final int SERVER_READ_IDLE_TIME = 0;

    public static final int CLIENT_WRITE_IDLE_TIME = 60;
    public static final int SERVER_WRITE_IDLE_TIME = 180;

    public static final int MAX_FRAME_LENGTH = 1024 * 1024;

    public static final int LENGTH_FIELD_OFFSET = 0;

    public static final int LENGTH_FIELD_LENGTH = 4;

    public static final int INITIAL_BYTES_TO_STRIP = 0;

    public static final int LENGTH_ADJUSTMENT = 0;
}
