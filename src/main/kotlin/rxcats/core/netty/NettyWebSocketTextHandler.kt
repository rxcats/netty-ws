package rxcats.core.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.slf4j.LoggerFactory

class NettyWebSocketTextHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    private val log = LoggerFactory.getLogger(NettyWebSocketTextHandler::class.java)

    override fun isSharable(): Boolean {
        return true
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        log.info("Received message: [{}] - {}", ctx.channel().id(), msg.text())
        ctx.channel().writeAndFlush(msg)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        super.channelInactive(ctx)
        log.info("channelInactive: [{}]", ctx.channel().id())
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause)
        log.info("exceptionCaught: [{}] - {}", ctx.channel().id(), cause.message)
    }
}
