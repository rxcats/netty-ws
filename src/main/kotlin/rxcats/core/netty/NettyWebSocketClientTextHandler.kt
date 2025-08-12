package rxcats.core.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import org.slf4j.LoggerFactory

class NettyWebSocketClientTextHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    private val log = LoggerFactory.getLogger(NettyWebSocketClientTextHandler::class.java)

    override fun isSharable(): Boolean {
        return true
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        log.info("Received message: [{}] - ({}) {}", ctx.channel().id(), msg.refCnt(), msg.text())
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("channelInactive: [{}]", ctx.channel().id())
        super.channelInactive(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        log.error("exceptionCaught: [{}] - {}", ctx.channel().id(), cause.message)
        super.exceptionCaught(ctx, cause)
    }

}
