package netty_13_tcp2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty_13_tcp2.code.MessageProtocol;

import java.nio.charset.Charset;

public class MyTcp2ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送消息到服务器,发送:你好啊,我是xiahu
        for (int i = 0; i < 10; i++) {
            String msg = "你好啊,我是xiahu";
            byte[] bytes = msg.getBytes(Charset.forName("utf-8"));
            int length = msg.getBytes(Charset.forName("utf-8")).length;

            //创建MessageProtocol 对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(bytes);

            ctx.writeAndFlush(messageProtocol);
        }

    }


    //客户端接收消息时被调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //读取服务端返回的消息
        int len = msg.getLen();
        byte[] bytes = msg.getContent();
        System.out.println("客户端收到消息信息如下:");
        System.out.println("客户端收到消息长度:" + len);
        System.out.println("客户端收到消息内容:" + new String(bytes, Charset.forName("utf-8")));
        System.out.println("客户端收到收取消息条数:" + (++this.count));
        System.out.println("###########################");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
