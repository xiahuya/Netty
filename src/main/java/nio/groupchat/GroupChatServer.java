package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author Xiahu
 * @create 2020/12/23
 */
public class GroupChatServer {
    //基于Nio的群聊系统

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int port = 8888;

    public GroupChatServer() {
        try {
            //获取选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.configureBlocking(false);
            listenChannel.socket().bind(new InetSocketAddress(port));
            //注册listenChannel 到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

            listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        try {
            while (true) {
                int count = selector.select(1000);
                if (count > 0) {
                    //有对应的事件需要被处理
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey key = selectionKeyIterator.next();
                        //根据Key对应的Channel,做不同的处理
                        if (key.isAcceptable()) {
                            accept(listenChannel);
                        } else if (key.isConnectable()) {

                        } else if (key.isReadable()) {//读取时间
                            readData(key);
                        } else if (key.isWritable()) {

                        }

                        //从集合中删除SelecrorKey,防止重复操作
                        selectionKeyIterator.remove();
                    }

                } else {
                    System.out.println("暂时没有需要处理的任务!");
                    Thread.sleep(3000);
                    continue;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accept(ServerSocketChannel listenChannel) {
        try {
            //当前事件为:OP_ACCEPT
            SocketChannel accept = listenChannel.accept();
            accept.configureBlocking(false);
            //将当前socketChannel注册到Selector,并且为该channel关联一个Buffer
            accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

            System.out.println(String.format("%s --->上线了", accept.getRemoteAddress().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取客户端消息
    public void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            //根据Key获取channel
            channel = (SocketChannel) key.channel();
            channel.configureBlocking(false);
            //获取channel关联的buffer
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            //读取Buffer中的数据
            int l = channel.read(byteBuffer);
            if (l > 0) {//已经读取到数据
                String msg = new String(byteBuffer.array(), 0, l);
                System.out.println(String.format("from 客户端 %s : %s", channel.getRemoteAddress(), msg));

                //todo 向其他客户端转发消息,将自己排除
                sendMsgToOtherClient(msg, channel);
            }

        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                key.cancel();
                //关闭改通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端
     *
     * @param msg     消息内容
     * @param channel 该客户端自己本身的channel
     */
    public void sendMsgToOtherClient(String msg, SocketChannel channel) {
        System.out.println("服务器转发消息中~~~~");
        try {

            //遍历所有注册到selector的SocketChannel,并且排除自己
            for (SelectionKey key : selector.keys()) {
                //通过key取出每一个SocketChannel
                Channel taretChannel = key.channel();

                //排除自己
                if (taretChannel instanceof SocketChannel && taretChannel != channel) {
                    SocketChannel targetSocketChannel = (SocketChannel) taretChannel;
                    ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                    //将buffer的数据写入通道
                    targetSocketChannel.write(byteBuffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GroupChatServer();
    }


}
