package com.xdz.nio;

import com.xdz.util.ResourceUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class BioTimeClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            socket = new Socket("127.0.0.1", 8080);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("query");
            // blocking-write
            out.println("now");
            out.flush();
            // blocking read
            String message = in.readLine();
            // handle message
            System.out.println("get time : " + message);
            // reply
            System.out.println("exit");
            out.println("exit");
            out.flush();
        } finally {
            ResourceUtil.close(socket, in, out);
        }
    }
}
