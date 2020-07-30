package com.github.cjbdi.szfg.web.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Boning Liang
 * @date 2020-07-29 17:54:11
 */
@WebService(endpointInterface = "com.github.cjbdi.szfg.web.service.SearchDataService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class SearchDataServiceImpl implements SearchDataService {


    @Override
    public String getSearchVO(String searchId) {
        return "test";
    }
}
