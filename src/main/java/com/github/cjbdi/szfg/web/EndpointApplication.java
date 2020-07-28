package com.github.cjbdi.szfg.web;

/**
 * @author Boning Liang
 */
public class EndpointApplication {

    public static void main(String[] args) {
        new ServerListeningThread(27401).start();
    }

}
