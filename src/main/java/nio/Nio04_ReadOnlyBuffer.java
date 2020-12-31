package nio;

import java.nio.ByteBuffer;

/**
 * @author Xiahu
 * @create 2020/12/17
 * NIO 组件之:Buffer
 * <p>
 * 将一个普通Buffer 转成只读Buffer
 */
public class Nio04_ReadOnlyBuffer {
    public static void main(String[] args) {
        //创建一个Buffer
        ByteBuffer allocate = ByteBuffer.allocate(100);

        for (int i = 0; i < 100; i++) {
            allocate.put((byte) i);
        }

        //取出
        allocate.flip();

        //转成只读Buffer
        ByteBuffer byteBuffer = allocate.asReadOnlyBuffer();
        System.out.println(byteBuffer.getClass());

        //读取
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }

        //此时无法往Buffer中存入数据,否则会抛出异常
        byteBuffer.put((byte)10);//Exception in thread "main" java.nio.ReadOnlyBufferException


    }
}
