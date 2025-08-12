package rxcats.core.netty

import io.netty.handler.logging.LogLevel

class NettyWebSocketClientOptions(
    val uri: String = "ws://localhost:8080/ws",

    // 0 : 최소 1개, 사용가능한 프로세서 * 2
    val groupThreadSize: Int = 1,

    val logLevel: LogLevel = LogLevel.INFO,
)
