package com.xdz.nio;

import com.xdz.util.ResourceUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class AioTimeServer {
    final static SimpleDateFormat formatter = new SimpleDateFormat();

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        Thread thread = new Thread(new AsyncTimeServerHandler(port));
        thread.start();
        thread.join();
    }

    public static class AsyncTimeServerHandler implements Runnable {
        int port;

        AsynchronousServerSocketChannel assc;

        CountDownLatch latch = new CountDownLatch(1);

        public AsyncTimeServerHandler(int port) throws IOException {
            this.port = port;
            assc = AsynchronousServerSocketChannel.open();
            assc.bind(new InetSocketAddress("127.0.0.1", port));
            System.out.println("AsynchronousServer is listening at port : " + port);
        }

        @Override
        public void run() {
            assc.accept(this, new AcceptCompletionHandler());
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

        @Override
        public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
            attachment.assc.accept(attachment, this);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            result.read(buffer, buffer, new ReadCompletionHandler(result));
        }

        @Override
        public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
            exc.printStackTrace();
            attachment.latch.countDown();
        }
    }

    public static class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        AsynchronousSocketChannel asc;

        public ReadCompletionHandler(AsynchronousSocketChannel asc) {
            this.asc = asc;
        }

        @Override
        public void completed(Integer result, ByteBuffer attachment) {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            String body = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("get message from client: " + body);

            String message;
            if ("now".equalsIgnoreCase(body)) {
                message = formatter.format(new Date());
            } else {
                message = "no cmd: " + body;
            }
            doWrite(message);
        }

        private void doWrite(String message) {
            if (message == null || message.trim().length() == 0) {
                return;
            }
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);

            buffer.flip();
            asc.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (buffer.hasRemaining()) {
                        asc.write(buffer, buffer, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    ResourceUtil.close(asc);
                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            ResourceUtil.close(asc);
        }
    }
}
