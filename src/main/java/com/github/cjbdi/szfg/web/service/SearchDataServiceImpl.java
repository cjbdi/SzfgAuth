package com.github.cjbdi.szfg.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjbdi.szfg.core.vo.SearchVO;
import com.github.cjbdi.szfg.data.SearchHolder;

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
        try {
            SearchVO searchVO = SearchHolder.getSearchByGoogleCache(searchId);
            if (searchVO != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(searchVO);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }
}
