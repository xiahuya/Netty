package nio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * @author Xiahu
 * @create 2020/12/17
 * NIO 组件之:Buffer
 *
 * ByteBuffer 支持类型化的put 和 get, put 放入的是什么数据类型，
 * get就应该使用相应的数据类型来取出，否则可能有 BufferUnderflowException 异常
 */
public class Nio04_BufferGetAndPut {
    public static void main(String[] args) {
        //创建一个Buffer
        ByteBuffer allocate = ByteBuffer.allocate(100);

        //向Buffer添加数据
        allocate.putInt(100);
        allocate.putLong(10l);
        allocate.putChar('夏');
        allocate.putShort((short)10);

        //取出
        allocate.flip();

        System.out.println(allocate.getInt());
        System.out.println(allocate.getLong());
        System.out.println(allocate.getChar());
        System.out.println(allocate.getShort());

    }
}
