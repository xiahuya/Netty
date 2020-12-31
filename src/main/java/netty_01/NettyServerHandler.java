package netty_01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author Xiahu
 * @create 2020/12/29
 * <p>
 * 自定义handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取客户端发送的请求信息
     *
     * @param ctx 上下文对象,可以获取大量对象引用
     * @param msg 客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server ctx = " + ctx);
        //将msg转换为ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址: " + ctx.channel().remoteAddress().toString());
    }


    /**
     * 数据读取完毕后调用该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //数据写入到buffer并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));
    }


    /**
     * 异常处理,关闭资源
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
