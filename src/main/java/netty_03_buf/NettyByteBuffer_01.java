package netty_03_buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Xiahu
 * @create 2021/1/6
 * netty 内Buffer 的使用 01
 */
public class NettyByteBuffer_01 {
    public static void main(String[] args) {
        //创建一个buffer
        //1.创建了一个ByteBuf对象,对象内封装的是一个Byte[]
        //2.netty 内的 buffer 不需要flip进行反转,因为其内部自己维护: readIndex & writeIndex
        //3.通过 readIndex & writeIndex & capacity ,将内部数据分为三部分
        //  0 - readIndex :已读区域
        //  readIndex - writeIndex : 可读区域
        //  writeIndex - capacity  : 可写区域
        ByteBuf buffer = Unpooled.buffer(10);

        //往buffer内写入数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.writeByte(i);
        }

        //从buffer读取数据
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
