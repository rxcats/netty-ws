package rxcats

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import rxcats.core.netty.NettyWebSocketServer
import rxcats.core.netty.NettyWebSocketServerOptions
import rxcats.core.netty.NettyWebSocketServerTextHandler

@SpringBootApplication(proxyBeanMethods = false)
class Application {

    // 테스트시 서버를 수동시작 할 수 있도록 Property 값을 이용
    @Bean(initMethod = "", destroyMethod = "")
    fun nettyWebSocketServer(@Value("\${app.netty.autostart:true}") autoStart: Boolean): NettyWebSocketServer {
        val options = NettyWebSocketServerOptions()
        val handler = NettyWebSocketServerTextHandler()
        return NettyWebSocketServer(options, handler).also {
            if (autoStart) {
                it.start()
            }
        }
    }

}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
