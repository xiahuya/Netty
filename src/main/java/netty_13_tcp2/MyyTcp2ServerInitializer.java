package netty_13_tcp2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty_13_tcp2.code.MyTcp2Decoder;
import netty_13_tcp2.code.MyTcp2Encoder;


public class MyyTcp2ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入解码器
        pipeline.addLast(new MyTcp2Decoder());
        //加入编码器
        pipeline.addLast(new MyTcp2Encoder());
        pipeline.addLast(new MyTcp2ServerHandler());

    }
}
