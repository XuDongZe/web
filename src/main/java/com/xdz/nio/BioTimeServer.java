package com.xdz.nio;

import com.xdz.util.ResourceUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

@CommonsLog
public class BioTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            log.info("TimeSever is listening at port : " + port);
            while (true) {
                Socket socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } finally {
            ResourceUtil.close(server);
        }
    }

    @NoArgsConstructor
    public static class TimeServerHandler implements Runnable {
        Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        SimpleDateFormat formatter = new SimpleDateFormat();

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true) {
                    // block read
                    String body = in.readLine();
                    // doHandle
                    String message;
                    if ("exit".equalsIgnoreCase(body)) {
                        break;
                    } else if ("now".equalsIgnoreCase(body)) {
                        message = formatter.format(new Date());
                    } else {
                        message = "no cmd: " + body;
                    }
                    // block write
                    out.println(message);
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e);
            } finally {
                ResourceUtil.close(in, out);
            }
        }
    }
}
