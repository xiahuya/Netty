package netty_06_websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import netty_02_http.HttpServerHandler;
import netty_02_http.HttpServerInitalizer;

/**
 * @author Xiahu
 * @create 2021/1/10
 * 通过websocket编程实现服务器和客户端长链接
 */
public class NettyWebSocketServer {
    /**
     * 实现基于webSocket的长连接的全双工的交互
     * 改变Http协议多次请求的约束，实现长连接了， 服务器可以发送消息给浏览器
     * 客户端浏览器和服务器端会相互感知，比如服务器关闭了，浏览器会感知，同样浏览器关闭了，服务器会感知
     */

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //具体业务逻辑
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程配置启动参数
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //向管道添加handler

                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入netty提供的httpServerCodec
                            //HttpServerCodec : netty 提供处理http请求的编-解码处理器
                            pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

                            //以块的方式写入,使用ChunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());
                            //http数据在传输的过程中是分段的,HttpObjectAggregator可以将多段数据聚合在一起
                            // 浏览器在加载大量数据时,会发出多次http请求,就是这个原因
                            pipeline.addLast(new HttpObjectAggregator(8092));

                            /**
                             * 1.对应websocket,它的数据时以帧(frame)形式传递
                             * 2.可以看到websocketframe 下有6个子类
                             * 3.浏览器请求时: ws//localhost:8888/hello 表示其url
                             * 4. WebSocketServerProtocolHandler 核心功能,将http协议升级为ws协议,保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //增加自定义handler
                            pipeline.addLast(new NettyWebSocketHandler());

                        }
                    });

            //绑定端口
            System.out.println("服务端已经准备好了~~~");
            ChannelFuture future = bootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
