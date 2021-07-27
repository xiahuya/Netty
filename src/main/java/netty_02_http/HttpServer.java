package netty_02_http;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Xiahu
 * @create 2021/1/5
 * <p>
 * Http服务器
 */
public class HttpServer {
    //基于netty 开发http服务

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //具体业务逻辑
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程配置启动参数
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitalizer());

            //绑定端口
            System.out.println("服务端已经准备好了~~~");
            ChannelFuture future = bootstrap.bind(8888).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("连接成功!!!");
                    } else {
                        System.out.println("连接失败!!!");

                    }

                }
            });

            future.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
