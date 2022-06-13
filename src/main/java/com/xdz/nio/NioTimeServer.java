package com.xdz.nio;

import com.xdz.util.ResourceUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NioTimeServer {
    public static void main(String[] args) throws IOException {
        MultiplexerTimeServer server = new MultiplexerTimeServer(8080);
        new Thread(server, "MultiplexerTimeServer-01").start();
    }

    public static class MultiplexerTimeServer implements Runnable {

        Selector selector;
        ServerSocketChannel ssc;
        private volatile boolean stop;

        SimpleDateFormat formatter = new SimpleDateFormat();

        public MultiplexerTimeServer(int port) throws IOException {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.socket().bind(new InetSocketAddress("127.0.0.1", port));
            System.out.println("MultiplexerTimeServer bind at port : " + port);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("MultiplexerTimeServer register with selector done");
            System.out.println("MultiplexerTimeServer is started, wait for connect...");
        }

        @Override
        public void run() {

            while (!stop) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    if (selectionKeys == null || selectionKeys.size() == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        handleSelectedKey(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ResourceUtil.close(selector, ssc);
        }

        private void handleSelectedKey(SelectionKey key) throws IOException {
            if (key != null && key.isValid()) {
                if (key.isAcceptable()) {
                    // 处理连接事件
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(buffer);
                    if (readBytes < 0) {
                        // eof 对端关闭
                        key.cancel();
                        sc.close();
                    } else if (readBytes == 0) {
                        // no data.
                    } else {
                        // decode data from buffer
                        buffer.flip(); // write done, flip for read.
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        System.out.println("get message from client: " + body);

                        // doHandle
                        String message;
                        if ("now".equalsIgnoreCase(body)) {
                            message = formatter.format(new Date());
                        } else {
                            message = "no cmd: " + body;
                        }
                        // reply
                        doWrite(sc, message);
                    }
                }
            }
        }

        private void doWrite(SocketChannel sc, String message) throws IOException {
            if (message != null && message.trim().length() > 0) {
                // encode message to ByteBuffer
                byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
                ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
                buffer.put(bytes);
                // sc write
                buffer.flip(); // flip after write, if you want read
                sc.write(buffer);

                // todo 写半包, 不处理
            }
        }

        public void stop() {
            stop = true;
        }
    }
}
