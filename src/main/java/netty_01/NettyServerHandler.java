package netty_01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

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
        //在这里休眠10s,模拟服务端阻塞,如果这样被阻塞,是很影响性能的,有没有什么方法能让其异步执行呢？
        /*System.out.println("客户端已经连接~~~~");
        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server ctx = " + ctx);
        //将msg转换为ByteBuf
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址: " + ctx.channel().remoteAddress().toString());*/


        // 方式一: 用户程序自定义普通任务
        /*ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    System.out.println("Server ctx = " + ctx);
                    //将msg转换为ByteBuf
                    ByteBuf byteBuf = (ByteBuf) msg;
                    System.out.println("客户端发送消息: " + byteBuf.toString(CharsetUtil.UTF_8));
                    System.out.println("客户端地址: " + ctx.channel().remoteAddress().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

        //方式二: 用户自定义定时任务 --> 将任务提交到scheduledTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    System.out.println("Server ctx = " + ctx);
                    //将msg转换为ByteBuf
                    ByteBuf byteBuf = (ByteBuf) msg;
                    System.out.println("客户端发送消息: " + byteBuf.toString(CharsetUtil.UTF_8));
                    System.out.println("客户端地址: " + ctx.channel().remoteAddress().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);
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
        System.out.println("服务端信息发送完毕~~~");
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
