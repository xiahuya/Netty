package netty_05_heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty_04_groupchat.NettyServerGroupCharHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author Xiahu
 * @create 2021/1/6
 * Netty心跳检测机制案例
 */
public class NettyHeartBeatServer {
    /*
     *  当服务器超过3秒没有读时，就提示读空闲
     *  当服务器超过5秒没有写操作时，就提示写空闲
     *  超过7秒没有读或者写操作时，就提示读写空闲
     */

    private int port;

    public NettyHeartBeatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new NettyHeartBeatServer(8888).run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            //创建Bootstrap配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程对象的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动的连接状态
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//添加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //心跳检测机制主要体现在IdleStateHandler
                            pipeline.addLast("heartbead", new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            /*
                             *  IdleStateHandler 是netty提供的处理空闲状态的处理器
                             *  该类传入四个参数,参数描述分别如下:
                             *      1.readerIdleTime :表示多长时间没有读,就会发送一个心跳检测包检测是否连接
                             *      2.writerIdleTime :表示多长时间没有写,就会发送一个心跳检测包检测是否连接
                             *      3.allIdleTime    :表示多长时间没有读或写,就会发送一个心跳检测包检测是否连接
                             *      4.unit           :时间单位
                             *  当IdleStateHandler 触发后,触发时间会发送给pipeline,交给下一个handler处理
                             */

                            //添加自定义handler
                            pipeline.addLast("MyHandler",new NettyHeartBeatHandler());


                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("服务端已经准备好了~~~");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
