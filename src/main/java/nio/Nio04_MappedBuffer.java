package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Xiahu
 * @create 2020/12/17
 * NIO 组件之:Buffer
 * <p>
 * MappedBuffer 可以让文件直接在内存(堆外内存)修改,操作系统不需要拷贝一次
 */
public class Nio04_MappedBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("channel.txt", "rw");

        //获取对应通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数说明:
         *  参数一: FileChannel.MapMode.READ_WRITE 使用读写模式
         *  参数二: 允许修改的起始位置
         *  参数三: 映射到内存的大小--> 将channel.txt文件的多少个字节映射到内存,可供直接修改的范围
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 10);

        map.put(0, (byte) 'X');
        map.put(1, (byte) 'I');
        map.put(2, (byte) 'A');
        map.put(3, (byte) 'H');
        map.put(4, (byte) 'U');

        //释放资源
        randomAccessFile.close();
        System.out.println("修改成功~~~~~");
    }
}
