package netty_01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Xiahu
 * @create 2020/12/29
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建 bossGroup 和 WorkerGroup
        //1.创建两个线程组 bossGroup 和 WorkerGroup
        //2.bossGroup 只是处理连接请求.
        //3.WorkerGroup 是真正处理服务端与客户端的业务流程
        //4.两个线程组启动后都是无限循环工作
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象,配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //使用链式编程来配置参数
            bootstrap
                    .group(bossGroup, workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器通道的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程对象的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动的连接状态
                    //给WorkerGroup 下的EventLoop 设置对应的管道处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //创建一个通道测试对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("服务端已经准备好了~~~");
            //绑定一个端口并且同步,生成了一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();

            //添加监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("连接成功!!!");
                    } else {
                        System.out.println("连接失败!!!");

                    }
                }
            });

            //对关闭通道的事件进行监听
            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
