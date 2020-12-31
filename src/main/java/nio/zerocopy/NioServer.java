package nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Xiahu
 * @create 2020/12/24
 */
public class NioServer {
    //NIO 零拷贝文件

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("客户端连接上来了~~~");

            int count = 0;

            if (-1 != count) {
                socketChannel.read(byteBuffer);
            }
            byteBuffer.rewind();
        }

    }
}
