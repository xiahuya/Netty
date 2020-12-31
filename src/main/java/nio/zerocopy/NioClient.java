package nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Xiahu
 * @create 2020/12/24
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8888));
        FileInputStream fileInputStream = new FileInputStream("protoc-3.6.1-win32.zip");
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //准备发送
        long start = System.currentTimeMillis();
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        long end = System.currentTimeMillis();
        System.out.println("发送总字节数: " + transferCount + ",最终耗费时间: " + (end - start) / 1000.0);
    }
}
