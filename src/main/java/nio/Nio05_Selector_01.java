package nio;

/**
 * @author Xiahu
 * @create 2020/12/22
 * NIO 组件之: Selector
 */
public class Nio05_Selector_01 {
    /**
     * 1. Netty 的 IO 线程 NioEventLoop 聚合了 Selector(选择器，也叫多路复用器)，可以同时并发处理成百上千个客户端连接。
     * 2. 当线程从某客户端 Socket 通道进行读写数据时，若没有数据可用时，该线程可以进行其他任务。
     * 3. 线程通常将非阻塞 IO 的空闲时间用于在其他通道上执行 IO 操作，所以单独的线程可以管理多个输入和输出通道。
     * 4. 由于读写操作都是非阻塞的，这就可以充分提升 IO 线程的运行效率，避免由于频繁 I/O 阻塞导致的线程挂起。
     * 5. 一个 I/O 线程可以并发处理 N 个客户端连接和读写操作，
     *    这从根本上解决了传统同步阻塞 I/O 一连接一线程模型，架构的性能、弹性伸缩能力和可靠性都得到了极大的提升。
     */


    public static void main(String[] args) {

    }
}
