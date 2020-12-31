package nio;

import java.nio.IntBuffer;

/**
 * @author Xiahu
 * @create 2020/12/17
 * NIO 组件之:Buffer
 *
 * 缓冲区（Buffer）：
 *  缓冲区本质上是一个可以读写数据的内存块，可以理解成是一个容器对象(含数组)，
 *  该对象提供了一组方法，可以更轻松地使用内存块，缓冲区对象内置了一些机制，
 *  能够跟踪和记录缓冲区的状态变化情况。
 *  Channel 提供从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由 Buffer
 */
public class Nio02_Buffer {
    public static void main(String[] args) {
        //Buffer的简单使用

        //创建一个Buffer,大小为5，可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer中写数据
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);
        intBuffer.put(1);

        //将buffer转换,读写切换
        intBuffer.flip();

        //遍历方式一
        for (int i = 0; i < intBuffer.capacity(); i++) {
            System.out.println(intBuffer.get());
        }


        //注意:Buffer不可以重复读,第一次读完后,由于position移动到了最后面，在往后已经没用数据可以读出
        if (!intBuffer.hasRemaining()) {
            System.out.println("Buffer为空");
        }


        //但是如果重新转换Buffer,将position归位，便可以在读
        intBuffer.flip();

        System.out.println("=============================");

        //遍历方式二
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }


    }
}
