package com.github.cjbdi.szfg.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;

/**
 * @author Boning Liang
 */
public class HttpRequestHandler {

    private Socket socket;
    private Map<String, String> headers = new HashMap<>();
    private String requestMethod;
    private String requestPath;
    private String protocolVersion;
    private Object bodyObj;
    private List<String> responseContent;
    private String responseContentType;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
        responseContent = new ArrayList<>();
    }

    public void handle() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        char[] charBuf = new char[1024];
        int mark;
//        System.out.println("Reading...");
        int i = 0;
        while ((mark = inputStreamReader.read(charBuf)) != -1) {
//            System.out.println(i + " charBuf = " + String.valueOf(charBuf) + " mark = " + mark);
            stringBuilder.append(charBuf, 0, mark);
            if (mark < charBuf.length) {
                break;
            }
            i++;
        }
//        System.out.println("Reading end...");

        String receivedContent = stringBuilder.toString();
        handleHttpMessage(receivedContent);
//        System.out.println(receivedContent);


        socket.getOutputStream().
                write(responseContent().getBytes());
    }

    private String responseContent() {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setProtocol("HTTP/1.1");
        responseEntity.setCode("200");
        responseEntity.setStatus("OK");
        responseEntity.putHeader("Content-Type", responseContentType);
        responseEntity.setBody(CollectionUtil.join(responseContent, ""));

//        System.out.println(responseEntity.getResponseMessage());
        return responseEntity.getResponseMessage();
    }

    private void handleHttpMessage(String msg) {
        if (StrUtil.isNotBlank(msg)) {

//            System.out.println("Messages are ... ");
//            System.out.println(msg);

            String[] splits = msg.split("\r\n");

            String[] firstLineSplits = splits[0].split(" ");

            requestMethod = firstLineSplits[0];
            requestPath = firstLineSplits[1];
            protocolVersion = firstLineSplits[2];

            int index = 1;

            // header
            while (splits.length > index + 1 && splits[index].length() > 0) {
                String[] keyVal = splits[index].split(":");
                headers.put(keyVal[0], keyVal[1].trim());
                index++;
            }

            // body
            StringBuilder bodyBuilder = new StringBuilder();
            while (splits.length > index + 1 && splits[index+1].length() > 0) {
                bodyBuilder.append(splits[index + 1]);
                index++;
                if (index+1 >= splits.length) {
                    break;
                }
            }

            String body = bodyBuilder.toString();

            String contentType = headers.get("Content-Type");
            if (contentType == null) {
                contentType = "application/x-www-from-urlencoded";
            }
            if (contentType != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                String[] contentTypeCharset = contentType.split(";");
                if (contentTypeCharset != null && contentTypeCharset.length == 2) {
                    contentType = contentTypeCharset[0];
                }
                switch (contentType) {
                    case "application/json":

                        try {
                            Map<String, Object> map = objectMapper.readValue(body, Map.class);

                            Endpoints endpoints = new Endpoints();
                            Class<Endpoints> endpointsClass = Endpoints.class;
//                            Method method = endpointsClass.getMethod("data", Map.class);
                            Method[] methods = endpointsClass.getMethods();
                            Arrays.stream(methods).forEach(method -> {
                                Endpoint endpoint = method.getAnnotation(Endpoint.class);
                                if (endpoint != null && endpoint.path().equals(requestPath)) {
                                    if (method.isAnnotationPresent(Endpoint.class)) {
                                        try {
                                            responseContentType = endpoint.produces();
                                            responseContent.add(objectMapper.writeValueAsString(method.invoke(endpoints, map)));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            bodyObj = map;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    case "application/x-www-form-urlencoded":
//                        System.out.println("In case " + contentType);
//                        System.out.println("Have body " + body);
                        Map<String, String> params = new HashMap<>();
                        String[] values = body.split("&");
                        int i = 0;
                        while (values.length > i && values[i].length() > 0) {
                            String[] keyVal = values[i].split("=");
                            params.put(keyVal[0], keyVal[1]);
                            i++;
                        }

                        Endpoints endpoints = new Endpoints();
                        Class<Endpoints> endpointsClass = Endpoints.class;
//                            Method method = endpointsClass.getMethod("data", Map.class);
                        Method[] methods = endpointsClass.getMethods();
                        Arrays.stream(methods).forEach(method -> {
                            Endpoint endpoint = method.getAnnotation(Endpoint.class);
                            String path = requestPath.split("\\?")[0];
                            if (endpoint != null && endpoint.path().equals(path)) {
                                if (method.isAnnotationPresent(Endpoint.class)) {
                                    try {
                                        responseContentType = endpoint.produces();
                                        responseContent.add(objectMapper.writeValueAsString(method.invoke(endpoints, params)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        break;

                }
            }

        }

    }

}
