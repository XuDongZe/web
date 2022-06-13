package com.xdz.nio;

import com.xdz.util.ResourceUtil;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class AioTimeClient {
    public static void main(String[] args) throws IOException {
        new Thread(new AsyncTimeClientHandler(8080)).start();
        while(true)
            ;
    }

    public static class AsyncTimeClientHandler implements Runnable, CompletionHandler<Void, AsyncTimeClientHandler> {

        int port;

        AsynchronousSocketChannel asc;

        public AsyncTimeClientHandler(int port) throws IOException {
            this.port = port;
            asc = AsynchronousSocketChannel.open();
        }

        @Override
        public void run() {
            asc.connect(new InetSocketAddress("127.0.0.1", port), this, this);
        }

        @Override
        public void completed(Void result, AsyncTimeClientHandler attachment) {
            byte[] bytes = "now".getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);

            buffer.flip();
            asc.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) {
                        asc.write(attachment, attachment, this);
                    } else {
                        doRead();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    ResourceUtil.close(asc);
                }
            });
        }

        private void doRead() {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            asc.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    attachment.flip();

                    byte[] bytes = new byte[attachment.remaining()];
                    attachment.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println(body);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    ResourceUtil.close(asc);
                }
            });
        }

        @Override
        public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
            exc.printStackTrace();
            ResourceUtil.close(asc);
        }
    }
}
