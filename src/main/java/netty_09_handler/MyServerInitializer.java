package netty_09_handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty_09_handler.decode.MyByteToLongDecoder;
import netty_09_handler.decode.MyLongToByteEncoder;


public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();//一会下断点

        //入站的handler进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());


        //自定义的handler 处理业务逻辑
        pipeline.addLast(new MyServerHandler());
        System.out.println("xx");
    }
}
