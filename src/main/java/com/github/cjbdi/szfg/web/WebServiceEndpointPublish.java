package com.github.cjbdi.szfg.web;

import com.github.cjbdi.szfg.web.service.SearchDataServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Boning Liang
 * @date 2020-08-07 17:47:20
 */
@Slf4j
public class WebServiceEndpointPublish {

    public static List<String> addresses;

    static {
        addresses = new ArrayList<>();
        addresses.add("http://192.168.25.223:27401/searchData");
        addresses.add("http://localhost:27401/searchData");
        addresses.forEach(address->{
            log.info("Publishing " + address);
            Endpoint.publish(address, new SearchDataServiceImpl());
        });
    }

}
