package netty_04_groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author Xiahu
 * @create 2021/1/6
 */
public class NettyGroupCharClient {
    private String host;
    private int port;

    public NettyGroupCharClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {
        new NettyGroupCharClient("localhost", 8888).run();
    }

    public void run() {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //绑定编码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //绑定解码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //绑定自己业务处理器
                            pipeline.addLast("MyHandler", new NettyClientGroupCharHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();

            Channel channel = future.channel();
            System.out.println("--------" + channel.localAddress() + "--------");
            //客户端发送的消息从键盘录入
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n");
            }

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }

    }
}
