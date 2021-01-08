package netty_05_heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author Xiahu
 * @create 2021/1/7
 */
public class NettyHeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //转型
            IdleStateEvent event = (IdleStateEvent) evt;
            String result = null;
            switch (event.state()) {
                case ALL_IDLE:
                    result = "读写空闲";
                    break;
                case READER_IDLE:
                    result = "读空闲";
                    break;
                case WRITER_IDLE:
                    result = "写空闲";
                    break;
            }

            System.out.println(ctx.channel().remoteAddress() + "----超时事件-----" + result);

            //关闭通道
//            ctx.channel().close();
        }

    }
}
