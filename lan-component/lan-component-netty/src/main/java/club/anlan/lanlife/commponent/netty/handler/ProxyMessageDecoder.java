package club.anlan.lanlife.commponent.netty.handler;

import club.anlan.lanlife.commponent.netty.constant.Constant;
import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * ProxyMessageDecoder
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:02
 */
public class ProxyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                               int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    public ProxyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
                               int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected ProxyMessage decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        if (in == null) {
            return null;
        }

        if (in.readableBytes() < Constant.HEADER_SIZE) {
            return null;
        }

        int frameLength = in.readInt();
        if (in.readableBytes() < frameLength) {
            return null;
        }
        ProxyMessage proxyMessage = new ProxyMessage();
        byte type = in.readByte();
        long sn = in.readLong();

        proxyMessage.setSerialNumber(sn);

        proxyMessage.setType(type);

        byte uriLength = in.readByte();
        byte[] uriBytes = new byte[uriLength];
        in.readBytes(uriBytes);
        proxyMessage.setRequestId(new String(uriBytes));

        byte[] data = new byte[frameLength - Constant.TYPE_SIZE - Constant.SERIAL_NUMBER_SIZE - Constant.URI_LENGTH_SIZE - uriLength];
        in.readBytes(data);
        proxyMessage.setData(data);

        in.release();

        return proxyMessage;
    }
}
