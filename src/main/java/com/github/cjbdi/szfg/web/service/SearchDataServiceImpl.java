package com.github.cjbdi.szfg.web.service;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Boning Liang
 */
@WebService(endpointInterface = "com.github.cjbdi.szfg.web.service.SearchDataService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class SearchDataServiceImpl implements SearchDataService {


    @Override
    public String getSearchVO(String searchId) {
        return "test";
    }
}
