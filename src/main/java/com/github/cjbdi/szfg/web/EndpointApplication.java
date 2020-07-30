package com.github.cjbdi.szfg.web;

import com.github.cjbdi.szfg.web.service.SearchDataServiceImpl;

import javax.xml.ws.Endpoint;

/**
 * @author Boning Liang
 */
public class EndpointApplication {

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/searchData", new SearchDataServiceImpl());
//        new ServerListeningThread(27401).start();
    }

}
