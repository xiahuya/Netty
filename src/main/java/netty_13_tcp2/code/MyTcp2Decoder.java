package netty_13_tcp2.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author Xiahu
 * @create 2021/1/12
 */
public class MyTcp2Decoder extends ReplayingDecoder<Void> {

    //编码器
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyTcp2Decoder 被调用了!!!");

        //将Byte 转成 MessageProtocol 对象
        int length = in.readInt();

        //创建一个数组
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        //封装到MessageProtocol 对象内
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(bytes);

        //收集,返回到下一个处理器
        out.add(messageProtocol);
    }
}
