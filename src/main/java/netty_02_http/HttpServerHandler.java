package netty_02_http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author Xiahu
 * @create 2021/1/5
 */
/*
 *  1.SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 *  2.HttpObject 由HttpServerCodec 封装
 */

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取事件触发该方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg的类型
        /*if (msg instanceof HttpRequest) {
            System.out.println("Msg 类型: " + msg.getClass());
            System.out.println("客户端地址: " + ctx.channel().remoteAddress());

            //回复消息
            ByteBuf byteBuf = Unpooled.copiedBuffer("客户端你好,我是服务器!", CharsetUtil.UTF_8);
            //构造与httpRequest 相对应的 HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            //返回response
            ctx.writeAndFlush(response);
        }*/


        //对特殊的数据做过滤
        if (msg instanceof HttpRequest) {
            System.out.println("Msg 类型: " + msg.getClass());
            System.out.println("客户端地址: " + ctx.channel().remoteAddress());

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("你请求了图标~~~~");
                return;
            }

            //回复消息
            ByteBuf byteBuf = Unpooled.copiedBuffer("客户端你好,我是服务器!", CharsetUtil.UTF_8);
            //构造与httpRequest 相对应的 HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            //返回response
            ctx.writeAndFlush(response);
        }
    }
}
