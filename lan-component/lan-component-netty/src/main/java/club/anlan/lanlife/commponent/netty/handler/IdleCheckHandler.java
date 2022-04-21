package club.anlan.lanlife.commponent.netty.handler;

import club.anlan.lanlife.commponent.netty.message.ProxyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * check idle chanel.
 *
 * @author lan
 * @version 1.0
 * @date 2022/4/17 16:21
 */
@Slf4j
public class IdleCheckHandler extends IdleStateHandler {

    //读超时是指在指定时间内没有接收到任何数据
    //写超时是指在指定时间内没有发送任何数据
    //将服务器端的读超时设置为60秒，客户端写超时设为50秒，客户端写超时则发送一个心跳包
    //因此服务器端与客户端如果正常连接则一定不会发生读超时
    //如果发生读超时则可能是网络延迟太高或者断线了，这时候就可以考虑断开连接了

    public IdleCheckHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {

        if (IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT == evt) {
            log.debug("channel write timeout {}", ctx.channel());
            ProxyMessage proxyMessage = new ProxyMessage();
            proxyMessage.setType(ProxyMessage.TYPE_HEARTBEAT);
            ctx.channel().writeAndFlush(proxyMessage);
        } else if (IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT == evt) {
            log.warn("channel read timeout {}", ctx.channel());
            ctx.channel().close();
        }
        super.channelIdle(ctx, evt);
    }
}
