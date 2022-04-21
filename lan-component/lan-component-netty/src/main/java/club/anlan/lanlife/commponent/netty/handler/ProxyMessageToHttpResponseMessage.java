package club.anlan.lanlife.commponent.netty.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 代理消息转换为 httpResponse对象
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/21 1:28
 */
public class ProxyMessageToHttpResponseMessage extends MessageToMessageDecoder<ProxyMessage> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ProxyMessage msg, List<Object> out) throws Exception {
        out.add(msg.getData());
    }
}
