package netty_09_handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//自定义编解码器在netty的使用
public class MyServer {

    /**
     * 当Netty发送或者接受一个消息的时候，就将会发生一次数据转换。
     * 入站消息会被解码：从字节转换为另一种格式（比如java对象）
     * 如果是出站消息，它会被编码成字节。
     */


    /**
     * 客户端发送long类型到服务端,所以对于的编解码器为:
     *  服务端: long类型的解码器
     *  客户端: long类型的编码器
     */

    //todo 数据流向--
    //客户端 --> 客户端出站(LongToByte) --> SocketChannel --> 服务端入站(ByteToLong) --> 服务端

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer()); //自定义一个初始化类

            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
