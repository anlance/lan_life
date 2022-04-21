package club.anlan.lanlife.commponent.netty.handler;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * ProxyMessageEncoder
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:14
 */
public class ProxyMessageEncoder extends MessageToByteEncoder<ProxyMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ProxyMessage msg, ByteBuf out) throws Exception {
        int bodyLength = Constant.TYPE_SIZE + Constant.SERIAL_NUMBER_SIZE + Constant.URI_LENGTH_SIZE;
        byte[] uriBytes = null;
        if (msg.getUri() != null) {
            uriBytes = msg.getUri().getBytes();
            bodyLength += uriBytes.length;
        }

        if (msg.getData() != null) {
            bodyLength += msg.getData().length;
        }

        // write the total packet length but without length field's length.
        out.writeInt(bodyLength);

        out.writeByte(msg.getType());
        out.writeLong(msg.getSerialNumber());

        if (uriBytes != null) {
            out.writeByte((byte) uriBytes.length);
            out.writeBytes(uriBytes);
        } else {
            out.writeByte((byte) 0x00);
        }

        if (msg.getData() != null) {
            out.writeBytes(msg.getData());
        }
    }
}
