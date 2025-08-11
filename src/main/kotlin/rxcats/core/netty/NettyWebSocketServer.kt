package rxcats.core.netty

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.logging.LoggingHandler
import org.slf4j.LoggerFactory

open class NettyWebSocketServer(
    private val options: NettyWebSocketServerOptions,
    private val wsHandler: NettyWebSocketTextHandler,
) {
    private val log = LoggerFactory.getLogger(NettyWebSocketServer::class.java)

    private lateinit var serverBootstrap: ServerBootstrap
    private lateinit var bossGroup: NioEventLoopGroup
    private lateinit var workerGroup: NioEventLoopGroup

    private fun initialize() {
        // Acceptor
        // 새로운 클라이언트이 연결요청을 처리
        // 연결된 클라이언트 SocketChannel을 생성, accept 이벤트를 처리
        bossGroup = NioEventLoopGroup(options.bossGroupThreadSize)

        // Worker
        // bossGroup에서 전달은 SocketChannel의 발생 이벤트(read/write)를 처리
        // 보통 클라이언트 연결요청 보다는 read/write 이벤트가 훨씬 많기 때문에 workerGroupThreadSize 를 더 크게 설정
        workerGroup = NioEventLoopGroup(options.workerGroupThreadSize)

        // Bootstrap은 Channel, EventLoopGroup등 Netty로 작성한 네트워크 애플리케이션의 동작 방식과 환경을 설정하는 도우미 클래스
        // 이를 통해 Netty 애플리케이션을 시동할 수 있으며, 각종 설정도 할 수 있다.
        // - 설정 가능한 요소
        //   - 전송 계층 (소켓 모드와 I/O 종류)
        //   - 이벤트 루프
        //   - 채널 파이프라인 설정
        //   - 소켓 주소와 포트
        //   - 소켓 옵션
        serverBootstrap = ServerBootstrap()
        serverBootstrap
            // 이벤트 루프 그룹 설정
            .group(bossGroup, workerGroup)

            // 비동기 네트워킹에 사용될 서버 채널 설정
            // NioServerSocketChannel, EpollServerSocketChannel
            .channel(NioServerSocketChannel::class.java)

            // boss 이벤트 루프 채널 초기화 클래스 설정
            // ServerSocketChannel에 대한 핸들러 설정
            .handler(object : ChannelInitializer<NioServerSocketChannel>() {
                override fun initChannel(ch: NioServerSocketChannel) {
                    ch.pipeline()
                        .addLast(LoggingHandler(options.logLevel))
                }
            })

            // worker 이벤트 루프 채널 초기화 클래스 설정
            // SocketChannel에 대한 핸들러 설정
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    ch.pipeline()
                        .addLast(HttpServerCodec())
                        .addLast(
                            WebSocketServerProtocolHandler(options.wsPath),
                        )
                        .addLast(LoggingHandler(options.logLevel))
                        .addLast(wsHandler)
                }
            })

            // 소켓 옵션
            .option(ChannelOption.SO_REUSEADDR, true)

        val cf = serverBootstrap.bind(options.port).sync()
        cf.channel().closeFuture().sync()
    }

    fun start() {
        try {
            log.info("Server started on port ${options.port}")
            initialize()
        } catch (_: InterruptedException) {
            log.info("Interrupted server...")
        } finally {
            log.info("Shutting down server...")
            shutdown()
        }
    }

    fun shutdown() {
        workerGroup.shutdownGracefully()
        bossGroup.shutdownGracefully()
    }
}
