package com.github.cjbdi.szfg.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Boning Liang
 */
public class ServerListeningThread extends Thread {

    private int bindPort;

    private ServerSocket serverSocket;

    public ServerListeningThread(int port) {
        this.bindPort = port;
    }

    @Override
    public void run() {

        Socket rcvSocket = null;
        try {
            serverSocket = new ServerSocket(bindPort);

            while (true) {
                rcvSocket = serverSocket.accept();
                // 初期请求 todo
                HttpRequestHandler request = new HttpRequestHandler(rcvSocket);
                request.handle();
                if (rcvSocket != null) {
                    try {
                        rcvSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rcvSocket != null) {
                try {
                    rcvSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
