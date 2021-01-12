package netty_12_tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 模拟tcp 协议 粘包和拆包现象
 */
public class MyTcpServer {
    /**
     * TCP是面向连接的，面向流的，提供高可靠性服务。
     * 收发两端（客户端和服务器端）都要有一一成对的socket，
     * 因此，发送端为了将多个发给接收端的包，更有效的发给对方，使用了优化方法（Nagle算法），
     * 将多次间隔较小且数据量小的数据，合并成一个大的数据块，然后进行封包。
     * 这样做虽然提高了效率，但是接收端就难于分辨出完整的数据包了，因为面向流的通信是无消息保护边界的
     */

    /**
     * 假设客户端分别发送了两个数据包D1和D2给服务端，由于服务端一次读取到字节数是不确定的，故可能存在以下四种情况：
     *  1.服务端分两次读取到了两个独立的数据包，分别是D1和D2，没有粘包和拆包
     *  2.服务端一次接受到了两个数据包，D1和D2粘合在一起，称之为TCP粘包
     *  3.服务端分两次读取到了数据包，第一次读取到了完整的D1包和D2包的部分内容，第二次读取到了D2包的剩余内容，这称之为TCP拆包
     *  4.服务端分两次读取到了数据包，第一次读取到了D1包的部分内容D1_1，第二次读取到了D1包的剩余部分内容D1_2和完整的D2包。
     */


    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyyTcpServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
