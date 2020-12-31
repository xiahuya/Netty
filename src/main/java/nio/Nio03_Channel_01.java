package nio;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Xiahu
 * @create 2020/12/17
 * <p>
 * NIO三大组件之:Channel
 *
 * NIO的通道类似于流，但有些区别如下
 *      通道可以同时进行读写，而流只能读或者只能写
 *      通道可以实现异步读写数据
 *      通道可以从缓冲读数据，也可以写数据到缓冲: 
 */
public class Nio03_Channel_01 {
    //需求: 将 hello ,使用channel写入文件

    public static void main(String[] args) throws IOException {
        String line = "hellp,xiahu";

        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream(new File("channel.txt"));
        //file channel的真实类型时FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将Buffer与Channel连接
        byteBuffer.put(line.getBytes());

        //将Buffer进行反转
        byteBuffer.flip();

        //将Buffer的数据写入到FileChannel
        fileChannel.write(byteBuffer);

        //释放资源
        fileOutputStream.close();

    }

}
