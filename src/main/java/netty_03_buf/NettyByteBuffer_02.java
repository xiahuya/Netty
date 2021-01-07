package netty_03_buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @author Xiahu
 * @create 2021/1/6
 * netty 内Buffer 的使用 02
 */
public class NettyByteBuffer_02 {
    public static void main(String[] args) {
        //创建一个buffer
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,Netty~", CharsetUtil.UTF_8);

        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            System.out.println(new String(array, 0, 12));

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

        }
    }
}
