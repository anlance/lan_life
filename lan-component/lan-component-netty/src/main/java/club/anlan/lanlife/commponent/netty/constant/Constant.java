package club.anlan.lanlife.commponent.netty.constant;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

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

    public static final int READ_IDLE_TIME = 60;

    public static final int WRITE_IDLE_TIME = 40;

    public static final int MAX_FRAME_LENGTH = 1024 * 1024;

    public static final int LENGTH_FIELD_OFFSET = 0;

    public static final int LENGTH_FIELD_LENGTH = 4;

    public static final int INITIAL_BYTES_TO_STRIP = 0;

    public static final int LENGTH_ADJUSTMENT = 0;

    public static final AttributeKey<String> CLIENT_KEY = AttributeKey.newInstance("client_key");

    public static final AttributeKey<Channel> NEXT_CHANNEL = AttributeKey.newInstance("nxt_channel");

    public static final AttributeKey<String> USER_ID = AttributeKey.newInstance("user_id");
}
