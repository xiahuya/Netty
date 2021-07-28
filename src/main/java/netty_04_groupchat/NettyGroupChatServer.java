package netty_04_groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author Xiahu
 * @create 2021/1/6
 */
public class NettyGroupChatServer {
    private int port;

    public NettyGroupChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new NettyGroupChatServer(8888).run();
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(100);

        try {

            //创建Bootstrap配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap
                    .group(bossGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程对象的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动的连接状态
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //绑定编码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //绑定解码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //绑定自己业务处理器
                            pipeline.addLast("MyHandler", new NettyServerGroupCharHandler());
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
