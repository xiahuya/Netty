package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Xiahu
 * @create 2020/12/22
 */
public class Nio06_Client {
    //客户端代码
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //绑定服务端IP,端口
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8888);

        //连接服务端
        if (!socketChannel.connect(socketAddress)) {
            //客户端与服务端没有连接成功
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间,客户端目前设置为非阻塞,可以干其他的事情~~~");
                Thread.sleep(3000);
            }
        }

        //连接成功,执行正常业务
        String msg = "hello,夏虎";
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(byteBuffer);


        //进程不会被关闭,一直在这里挂起
        System.in.read();

    }
}
