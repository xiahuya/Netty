package netty_02_http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * @author Xiahu
 * @create 2021/1/5
 */
public class HttpServerInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道添加handler

        //获取pipeline
        ChannelPipeline pipeline = ch.pipeline();
        //加入netty提供的httpServerCodec
        //HttpServerCodec : netty 提供处理http请求的编-解码处理器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //增加自定义的处理器
        pipeline.addLast("HttpServerHandler", new HttpServerHandler());

    }
}
