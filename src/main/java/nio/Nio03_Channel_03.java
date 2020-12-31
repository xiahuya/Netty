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
public class Nio03_Channel_03 {
    //需求: 使用Buffer完成文件的复制

    public static void main(String[] args) throws IOException {
        String line = "hellp,xiahu";

        //2.创建一个输入流,创建一个输出流
        FileInputStream fileInputStream = new FileInputStream(new File("channel.txt"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("channel.txt.copy"));

        //2.分别获取对应的channel对象
        FileChannel fileChannel01 = fileInputStream.getChannel();
        FileChannel fileChannel02 = fileOutputStream.getChannel();


        //3.创建两fileChannel公用的Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //循环读取文件
        while (true) {
            //重置内部维护索引,重要
            byteBuffer.clear();
            //4.将inputStream内的数据循环读取出来,并且暂时放置到Buffer
            int length = fileChannel01.read(byteBuffer);
            if (length == -1) {
                break;
            }

            //读写反转
            byteBuffer.flip();

            //5.从Buffer中读取缓存的内容,由outPutStream写回本地磁盘
            fileChannel02.write(byteBuffer);
        }

        //6.释放资源
        fileInputStream.close();
        fileOutputStream.close();

    }

}
