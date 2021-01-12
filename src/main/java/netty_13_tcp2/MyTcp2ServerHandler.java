package netty_13_tcp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty_13_tcp2.code.MessageProtocol;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyTcp2ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        // 服务端读取客户端发送的消息
        int len = msg.getLen();
        byte[] bytes = msg.getContent();
        System.out.println("服务端收到消息信息如下:");
        System.out.println("服务端收到消息长度:" + len);
        System.out.println("服务端收到消息内容:" + new String(bytes, Charset.forName("utf-8")));
        System.out.println("服务端收到收取消息条数:" + (++this.count));
        System.out.println("###############################");
        System.out.println("");

        //回复客户端消息
        String uuid = UUID.randomUUID().toString();
        int length = uuid.getBytes("utf-8").length;
        byte[] uuidBytes = uuid.getBytes("utf-8");
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(uuidBytes);

        ctx.writeAndFlush(messageProtocol);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
