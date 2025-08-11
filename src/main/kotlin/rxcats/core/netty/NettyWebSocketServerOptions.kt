package rxcats.core.netty

import io.netty.handler.logging.LogLevel

class NettyWebSocketServerOptions(
    val port: Int = 8080,

    val wsPath: String = "/ws",

    // 0 : 최소 1개, 사용가능한 프로세서 * 2
    val bossGroupThreadSize: Int = 1,

    // 0 : 최소 1개, 사용가능한 프로세서 * 2
    val workerGroupThreadSize: Int = 0,

    val logLevel: LogLevel = LogLevel.INFO,
)
