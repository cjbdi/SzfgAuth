package com.github.cjbdi.szfg.web;

import cn.hutool.core.collection.CollectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Boning Liang
 */

public class ResponseEntity {

    private String protocol;

    private String code;

    private String status;

    private Map<String, String> headers;

    private Object body;

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void putHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getResponseMessage() {
        String responseMessage = protocol + " " + code + " " + status + "\r\n";
        List<String> headerList = new ArrayList<>();
        headers.forEach((k,v)->{
            String header = k + ":" + v;
            headerList.add(header);
        });
        responseMessage += CollectionUtil.join(headerList, "\r\n");
        responseMessage += "\r\n\r\n";
        responseMessage += body.toString();
        return  responseMessage;
    }

    public byte[] getResponseMessageBytes() {
        return getResponseMessage().getBytes();
    }
}
