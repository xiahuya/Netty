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
public class Nio03_Channel_04 {
    //需求: 使用tranform完成图片拷贝

    public static void main(String[] args) throws IOException {
        //1.创建相关流
        FileInputStream fileInputStream = new FileInputStream("a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("a_copy.jpg");

        //2.获取每个流对应的channel
        FileChannel intputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        //3.使用transForm完成图片的拷贝
        outputChannel.transferFrom(intputChannel,0,intputChannel.size());

        //4.关闭相关流
        fileInputStream.close();
        fileOutputStream.close();
    }

}
