package rxcats

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import rxcats.core.netty.NettyWebSocketClient
import rxcats.core.netty.NettyWebSocketClientOptions
import rxcats.core.netty.NettyWebSocketClientTextHandler
import rxcats.core.netty.NettyWebSocketServer
import kotlin.concurrent.thread

@SpringBootTest(properties = ["app.netty.autostart=false", "spring.output.ansi.enabled=always"])
class NettyWsApplicationTests {

    @Autowired
    private lateinit var server: NettyWebSocketServer

    @Test
    fun nettyWebSocketTest() {
        thread {
            server.start()
        }

        val options = NettyWebSocketClientOptions()
        val handler = NettyWebSocketClientTextHandler()
        val client = NettyWebSocketClient(options, handler)

        thread {
            Thread.sleep(500)
            client.connect()
        }

        Thread.sleep(1000)
        client.sendMessage("hello world!")

        Thread.sleep(1000)
        client.close()
        server.shutdown()
    }
}
