package netty_04_groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Xiahu
 * @create 2021/1/6
 */
public class NettyServerGroupCharHandler extends SimpleChannelInboundHandler<String> {

    //定义channel组,管理所有的channel
    // 全局唯一的事件执行器(单例)
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<Channel> channelList = new ArrayList<>();


    //读取事件触发该方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        //遍历channelGroup,根据不同的情况返回不同的消息
        channelGroup.forEach(ch -> {
            if (ch != channel) { // 不是当前的channel,需要转发其他客户端传来的消息
                ch.writeAndFlush(String.format("[客户端: %s ] 说: %s", channel.remoteAddress(), msg));
            } else { // 是自己,回显自己发送的消息
                ch.writeAndFlush(String.format("[ 自己 ] 说: %s", msg));
            }
        });
        /*channelGroup.forEach(ch -> {
            if(channel != ch) { //不是当前的channel,转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            }else {//回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });*/
    }


    //客户端与服务端建立连接时第一个被调用
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush(String.format("[Server] : 客户端[ %s ] 于  %s 加入聊天", channel.remoteAddress(), sdf.format(new Date())) + "\n");

        //当客户端与服务端建立连接时,记录该channel,用于后面的通信
        channelGroup.add(channel);

    }


    //客户端与服务端丢失连接时第一个被调用
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush(String.format("[Server] : 客户端[ %s ] 于  %s 离开聊天", channel.remoteAddress(), sdf.format(new Date())) + "\n");
        System.out.println("channelGroup size" + channelGroup.size());

        //当客户端与服务端建立连接时,记录该channel,用于后面的通信
        channelGroup.remove(channel);
    }

    //表示Channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format(" %s 上线啦~~~~", ctx.channel().remoteAddress()));

    }

    //表示Channel处于离开状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format(" %s 离线啦~~~~", ctx.channel().remoteAddress()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
