package netty_06_websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Xiahu
 * @create 2021/1/10
 */

//TextWebSocketFrame : 表示一个文本帧
public class NettyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //服务器端有消息时触发
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息: " + msg.text());
        //回复客户端消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame(sdf.format(new Date()) + " : " + msg.text()));
        //ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }


    //web客户端连接时被触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //ctx.channel().id()表示唯一的值
        //asLongText:唯一
        //asShortText:不唯一
        System.out.println("handlerAdded 被调用:" + ctx.channel().id().asLongText());
        //System.out.println("handlerAdded 被调用:" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用:" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage());
        ctx.close();
    }
}
