package bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Xiahu
 * @create 2020/12/16
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        // 线程池机制
        ExecutorService executorService = Executors.newCachedThreadPool();

        //创建服务端
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务端启动了......");

        while (true) {
            System.out.println("等待客户端连接.....");
            //监听客户端的连接
            final Socket socket = serverSocket.accept();
            System.out.println("客户端: " + socket.getLocalAddress().toString() + ":" + socket.getPort() + " 连接上来了...");


            //使用线程池来与客户端建立连接并处理请求
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }

    }

    //与客户端交互,输出客户端的请求信息
    private static void handler(Socket socket) {
        String address = socket.getLocalAddress().toString();
        int port = socket.getPort();
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            while (true) {
                System.out.println("等待客户端请求数据.....");
                int length = inputStream.read(bytes);
                if (length != -1) {
                    //打印客户端请求消息
                    System.out.println(address + ":" + port + "-----> " + new String(bytes, 0, length));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭资源
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
