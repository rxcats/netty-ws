package rxcats

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import rxcats.core.netty.NettyWebSocketServer
import rxcats.core.netty.NettyWebSocketServerOptions
import rxcats.core.netty.NettyWebSocketTextHandler

@SpringBootApplication(proxyBeanMethods = false)
class Application {

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    fun nettyWebSocketServer(): NettyWebSocketServer {
        val options = NettyWebSocketServerOptions()
        val handler = NettyWebSocketTextHandler()
        return NettyWebSocketServer(options, handler)
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
