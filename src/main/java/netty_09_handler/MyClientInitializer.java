package netty_09_handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty_09_handler.decode.MyByteToLongDecoder;
import netty_09_handler.decode.MyLongToByteEncoder;


public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //加入一个出站的handler 对数据进行一个编码
        pipeline.addLast(new MyLongToByteEncoder());

        //加入一个自定义的handler ， 处理业务
        pipeline.addLast(new MyClientHandler());


    }
}
