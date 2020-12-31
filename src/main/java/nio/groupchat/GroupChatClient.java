package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Xiahu
 * @create 2020/12/23
 */
public class GroupChatClient {
    private SocketChannel socketChannel;
    private SocketAddress socketAddress;
    private Selector selector;
    private String localAddress;

    public GroupChatClient() {
        try {
            selector = Selector.open();
            socketAddress = new InetSocketAddress("127.0.0.1", 8888);
            socketChannel = SocketChannel.open(socketAddress);
            //socketChannel.socket().bind(socketAddress);
            socketChannel.configureBlocking(false);

            //将channel注册到selector
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            localAddress = socketChannel.getLocalAddress().toString();
            System.out.println(localAddress + " is Ok~~");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfoToServer(String msg) {
        msg = localAddress + " 说:" + msg;

        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从服务器读取数据
    public void readInfoFromServer() {
        try {
            //返回注册到selector 的channel的个数
            int count = selector.select();
            if (count > 0) {//表示有可用的channel
                Iterator<SelectionKey> selectionKeyIterator = selector.keys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        socketChannel.configureBlocking(false);
                        //实例化一个Buffer
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int readLength = socketChannel.read(byteBuffer);
                        if (readLength > 0) {
                            System.out.println("服务器说: " + new String(byteBuffer.array(), 0, readLength));
                        }
                    }
                }
            } else {
                System.out.println("暂时没有可以使用的通道!!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();

        //读取端
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    groupChatClient.readInfoFromServer();

                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        //发送数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            groupChatClient.sendInfoToServer(msg);
        }
    }
}
