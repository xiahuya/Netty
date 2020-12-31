package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author Xiahu
 * @create 2020/12/21
 * NIO 组件之:Buffer
 * Scattering : 将数据写入到buffer时可以采用Buffer数组依次写入
 * Gathering: 从Buufer读取数据时,采用Buffer数据依次读取
 */
public class Nio04_BufferScatteringAndGather {
    public static void main(String[] args) throws IOException {
        //使用ServerSocketChannel 和SocketChannel

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
        //绑定IP地址并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("客户端已经连接~~~");

        //假设从客户端接收8个字节
        int messageLength = 8;

        //循环读取客户端发送的数据
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                //累计读取字节数
                byteRead += l;
                System.out.println("ByteRead=" + byteRead);
                Arrays.
                        asList(byteBuffers)
                        .stream()
                        .map(buffer -> "postion=" + buffer.position() + ", limit=" + buffer.limit())
                        .forEach(System.out::println);
            }

            //将所有Buffer反转
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将所有数据读出并打印
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }
            System.out.println(new String(byteBuffers[0].array(),0,byteBuffers[0].limit()));
            System.out.println(new String(byteBuffers[1].array(),0,byteBuffers[1].limit()));

            //将所有Buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println(String.format("ByteRead: %s ,ByteWrite: %s ,MessageLength: %s", byteRead, byteWrite, messageLength));
        }


    }
}
