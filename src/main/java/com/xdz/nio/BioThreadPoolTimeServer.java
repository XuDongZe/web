package com.xdz.nio;

import com.xdz.util.ResourceUtil;
import lombok.extern.apachecommons.CommonsLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@CommonsLog
public class BioThreadPoolTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        TimeServerHandlerThreadPool pool = new TimeServerHandlerThreadPool(3, 10);
        ServerSocket sc = null;
        try {
            sc = new ServerSocket(port);
            log.info("BioThreadPoolTimeServer is listen at port : " + port);
            while (true) {
                Socket socket = sc.accept();
                pool.execute(new BioTimeServer.TimeServerHandler(socket));
            }
        } finally {
            pool.close();
            ResourceUtil.close(sc);
        }
    }

    public static class TimeServerHandlerThreadPool {
        ExecutorService executor;

        public TimeServerHandlerThreadPool(int maxSize, int queueSize) {
            this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxSize, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
        }

        public void execute(Runnable task) {
            executor.execute(task);
        }

        public void close() {
            this.executor.shutdown();
        }
    }
}
