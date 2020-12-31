package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Xiahu
 * @create 2020/12/22
 */
public class Nio06_Server {
    //实现服务器端和客户端之间的数据简单通讯（非阻塞）

    public static void main(String[] args) throws IOException, InterruptedException {
        //1.创建ServerSocketChannel
        ServerSocketChannel socketChannel = ServerSocketChannel.open();

        //2.获取Selector对象
        Selector selector = Selector.open();

        //3.绑定端口,并且监听连接
        socketChannel.socket().bind(new InetSocketAddress(8888));
        //4.设置非阻塞
        socketChannel.configureBlocking(false);

        //5.把serverSocketChannel注册到Selector,关心时间为:OP_ACCEPT
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6.循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) {//阻塞1S,没有任何事件发生
                System.out.println("无任何连接");
                Thread.sleep(3000);
                continue;
            }


            //获取相关的SelectorKey集合
            //如果返回值 > 0,表示获取到关注的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历集合
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //根据Key对应的Channel,做不同的处理
                if (key.isAcceptable()) {//首次连接时间
                    //当前事件为:OP_ACCEPT
                    SocketChannel accept = socketChannel.accept();
                    accept.configureBlocking(false);
                    System.out.println("客户端连接成功!!! " + accept.hashCode());
                    //将当前socketChannel注册到Selector,并且为该channel关联一个Buffer
                    accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2014));
                } else if (key.isConnectable()) {

                } else if (key.isReadable()) {//读取时间
                    //根据Key获取channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    channel.configureBlocking(false);
                    //获取channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    //读取Buffer中的数据
                    int l = channel.read(byteBuffer);
                    System.out.println("客户端发送消息: " + new String(byteBuffer.array(), 0, l));


                } else if (key.isWritable()) {

                }

                //从集合中删除SelecrorKey,防止重复操作
                iterator.remove();
            }
        }

    }
}
