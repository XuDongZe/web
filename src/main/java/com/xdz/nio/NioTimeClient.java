package com.xdz.nio;

import com.xdz.util.ResourceUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioTimeClient {
    public static void main(String[] args) throws IOException {
        TimeClientHandler handler = new TimeClientHandler(8080);
        new Thread(handler, "NioTimeClient").start();
    }

    public static class TimeClientHandler implements Runnable {
        int port;
        Selector selector;
        SocketChannel sc;
        private volatile boolean stop;

        public TimeClientHandler(int port) throws IOException {
            this.port = port;
            this.selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
        }

        @Override
        public void run() {
            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            while (!stop) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    if (selectionKeys == null || selectionKeys.isEmpty()) {
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

            ResourceUtil.close(selector, sc);
        }

        private void doConnect() throws IOException {
            boolean connected = sc.connect(new InetSocketAddress("127.0.0.1", port));
            if (connected) {
                sc.register(selector, SelectionKey.OP_READ);
                doWrite("now");
            } else {
                sc.register(selector, SelectionKey.OP_CONNECT);
            }
        }

        private void doWrite(String message) throws IOException {
            if (message == null || message.trim().length() == 0) {
                return;
            }

            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);

            buffer.flip();
            sc.write(buffer);
            // todo 不处理写半包
        }

        private void handleSelectedKey(SelectionKey key) throws IOException {
            if (key == null || !key.isValid()) {
                return;
            }

            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite("now");
                } else {
                    System.out.println("connect error.");
                    System.exit(-1);
                }
            }

            if (key.isReadable()) {
                // read data
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(buffer);
                if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                } else if (readBytes == 0) {
                    // do nothing
                } else {
                    byte[] bytes = new byte[readBytes];
                    buffer.flip(); // read from buffer after write
                    buffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println(body);
                    this.stop = true;
                }
            }
        }
    }
}
