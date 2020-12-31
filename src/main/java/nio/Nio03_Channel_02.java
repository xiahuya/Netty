package nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Xiahu
 * @create 2020/12/17
 * <p>
 * NIO三大组件之:Channel
 */
public class Nio03_Channel_02 {
    //需求: 读取文件channel.txt的数据,并且打印出来

    public static void main(String[] args) throws IOException {
        String line = "hellp,xiahu";

        //1.创建一个输入流
        FileInputStream fileInputStream = new FileInputStream(new File("channel.txt"));
        //2.获取channel
        FileChannel fileChannel = fileInputStream.getChannel();


        //3.创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


        //4.从channel读取数据,并放置Buffer
        int read = fileChannel.read(byteBuffer);

        //5.将字节转成字符串,并打印
        System.out.println(new String(byteBuffer.array(), 0, read));

        //释放资源
        fileInputStream.close();

    }

}
