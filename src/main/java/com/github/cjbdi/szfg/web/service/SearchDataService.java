package com.github.cjbdi.szfg.web.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author Boning Liang
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SearchDataService {

    @WebMethod
    @WebResult(name = "search")
    String getSearchVO(@WebParam(name = "searchId") String searchId);

}
