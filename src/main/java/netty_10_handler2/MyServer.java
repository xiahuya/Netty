package netty_10_handler2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {

    /**
     *  服务端发送long类型到客户端,所以对于的编解码器为:
     *  客户端: long类型的解码器
     *  服务端: long类型的编码器
     *
     *  在handler 基础上,
     *  需要在客户端pipeline 内添加 LongToByteHandler
     *  需要在服务端pipeline 内添加 ByteToLongHandler
     *
     */

    //todo 数据流向--
    //服务器 --> 服务端出站(ByteToLong) --> SocketChannel --> 客户端入站(LongToByte) --> 客户端

    public static void main(String[] args) throws Exception{

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    /**
     * 结论
     *  1. 不论解码器handler 还是 编码器handler 即接收的消息类型必须与待处理的消息类型一致，否则该handler不会被执行
     *      解释:
     *          客户端 --> 服务端发送数据时,如果客户端使用的出站Handler 是 Long类型,但是发送的数据是String,那么编码器是不会处理该条数据
     *          同理,如果服务端使用的入站Handdler 是 String类型,但是客户端发送的Long类型,那么服务端的解码Handler也不会处理该消息
     *
     *  2. 在解码器 进行数据解码时，需要判断 缓存区(ByteBuf)的数据是否足够 ，否则接收到的结果会期望结果可能不一致
     */
}
