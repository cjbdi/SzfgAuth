package com.github.cjbdi.szfg.web;

import cn.hutool.core.util.StrUtil;
import com.github.cjbdi.szfg.core.TestConstant;
import com.github.cjbdi.szfg.core.vo.BasicInfo;
import com.github.cjbdi.szfg.core.vo.SearchVO;
import com.github.cjbdi.szfg.data.DocumentType;
import com.github.cjbdi.szfg.data.PartyType;
import com.github.cjbdi.szfg.data.SearchDataGenerator;
import com.github.cjbdi.szfg.data.SearchHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Boning Liang
 */
public class Endpoints {

    @Endpoint(path = "/data", produces = "application/json")
    public Map<String, Object> data(Map<String, Object> data) {
        Map<String, Object> objectMap = new HashMap<>();

        try {
            BasicInfo basicInfo = new BasicInfo("（2000）最高法民再111号", "盗窃罪", null, null);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(TestConstant.publicKey, "ROLE_TEST", basicInfo);
            searchDataGenerator.addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent);
            searchDataGenerator.addDocument(DocumentType.OTHER, "文书");
            searchDataGenerator.setCourt("石家庄市中级人民法院", "1234");
            searchDataGenerator.setUser("wangxiaoming", "王小明");
            searchDataGenerator.reportDownload();
            searchDataGenerator.addParty(PartyType.PEOPLE, "张三", "123123199001010001", "被告");
            searchDataGenerator.addParty(PartyType.PEOPLE, "李四", "123123199001010002", "原告");
            String searchId = searchDataGenerator.getSearchId();

            try {
                objectMap.put("data", searchId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            objectMap.put("code", 0);
            objectMap.put("msg", "success");

            return objectMap;
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put("code", 1);
            objectMap.put("msg", "fail");
            objectMap.put("data", null);
            return objectMap;
        }

    }

    @Endpoint(path = "/searchData", produces = "application/json")
    public Map<String, Object> searchData(Map<String, Object> data) {
        Map<String, Object> objectMap = new HashMap<>();
        try {
            if(data.get("id") == null) {
                throw new Exception("id is null");
            }
            String id = data.get("id").toString();
            if (StrUtil.isBlank(id)) {
                objectMap.put("data", null);
            } else {
//                SearchVO searchVO = SearchHolder.getSearch(id);
                SearchVO searchVO = SearchHolder.getSearchByGoogleCache(id);
                objectMap.put("data", searchVO);
            }

            objectMap.put("code", 0);
            objectMap.put("msg", "success");

            return objectMap;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            objectMap.put("code", 1);
            objectMap.put("msg", "fail");
            objectMap.put("data", null);
            return objectMap;
        }

    }

}
