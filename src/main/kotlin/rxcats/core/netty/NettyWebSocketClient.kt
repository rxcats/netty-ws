package rxcats.core.netty

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory
import java.net.URI

class NettyWebSocketClient(
    private val options: NettyWebSocketClientOptions,
    private val textHandler: NettyWebSocketClientTextHandler,
) {
    private val log = LoggerFactory.getLogger(NettyWebSocketClient::class.java)

    private lateinit var group: NioEventLoopGroup
    private lateinit var bootstrap: Bootstrap
    private lateinit var channel: Channel

    fun initialize() {
        group = NioEventLoopGroup(options.groupThreadSize)
        bootstrap = Bootstrap()

        val uri = URI(options.uri)

        val handshaker = WebSocketClientHandshakerFactory.newHandshaker(
            uri, WebSocketVersion.V13, null, false, DefaultHttpHeaders()
        )

        bootstrap
            .group(group)
            .channel(NioSocketChannel::class.java)
            .handler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline()
                        .addLast(HttpClientCodec())
                        .addLast(WebSocketClientProtocolHandler(handshaker))
                        .addLast(LoggingHandler(options.logLevel))
                        .addLast(textHandler)
                }
            })
        channel = bootstrap.connect(uri.host, uri.port).sync().channel()
        channel.closeFuture().sync()
    }

    fun connect() {
        try {
            log.info("Trying to connect to the server...")
            initialize()
        } catch (_: InterruptedException) {
            log.info("Interrupted client...")
        } finally {
            log.info("Disconnect from the server...")
            close()
        }
    }

    fun close() {
        group.shutdownGracefully()
    }

    fun sendMessage(text: String) {
        channel.writeAndFlush(TextWebSocketFrame(text))
    }

}
