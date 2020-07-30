package com.github.cjbdi.szfg.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjbdi.szfg.core.vo.*;
import com.github.cjbdi.szfg.data.DocumentType;
import com.github.cjbdi.szfg.data.PartyType;
import com.github.cjbdi.szfg.data.SearchDataGenerator;
import org.junit.Test;

/**
 * @author Boning Liang
 */
public class Demo {

    @Test
    public void AESDemo() {
        System.out.println(SzfgAuth.getAccessKey("ROLE_CJBDI", "1234567890123456"));
    }

    @Test
    public void FtpDemo() throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqPvovSfXcwBbW8cKMCgwqNpsYuzF8RPAPFb7LGsnVo44JhM/xxzDyzoYtdfNmtbIuKVi9PzIsyp6rg+09gbuI6UGwBZ5DWBDBMqv5MPdOF5dCQkB2Bbr5yPfURPENypUz+pBFBg41d+BC+rwRiXELwKy7Y9caD/MtJyHydj8OUwIDAQAB";
        FtpVO ftpVO = new FtpVO();
        ftpVO.setHost("192.168.1.1");
        ftpVO.setPort(21);
        ftpVO.setUsername("demo");
        ftpVO.setPassword("demo");
        ftpVO.setEncoding("utf-8");
        ftpVO.setWorkDir("/report");
        String ftp = SzfgAuth.getFtp(ftpVO, publicKey);
        System.out.println(ftp);
    }

    @Test
    public void OssDemo() throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqPvovSfXcwBbW8cKMCgwqNpsYuzF8RPAPFb7LGsnVo44JhM/xxzDyzoYtdfNmtbIuKVi9PzIsyp6rg+09gbuI6UGwBZ5DWBDBMqv5MPdOF5dCQkB2Bbr5yPfURPENypUz+pBFBg41d+BC+rwRiXELwKy7Y9caD/MtJyHydj8OUwIDAQAB";
        OssVO ossVO = new OssVO();
        ossVO.setEndpoint("http://oss-cn-beijing.aliyuncs.com");
        ossVO.setAccessKeyId("xxx");
        ossVO.setAccessKeySecret("xxx");
        String oss = SzfgAuth.getOss(ossVO, publicKey);
        System.out.println(oss);
    }

    @Test
    public void ApiDemo() throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqPvovSfXcwBbW8cKMCgwqNpsYuzF8RPAPFb7LGsnVo44JhM/xxzDyzoYtdfNmtbIuKVi9PzIsyp6rg+09gbuI6UGwBZ5DWBDBMqv5MPdOF5dCQkB2Bbr5yPfURPENypUz+pBFBg41d+BC+rwRiXELwKy7Y9caD/MtJyHydj8OUwIDAQAB";
        ApiVO apiVO = new ApiVO();
        apiVO.setName("apiname");
        apiVO.setUrl("http://demo.com/demo");
        String api = SzfgAuth.getApi(apiVO, publicKey);
        System.out.println(api);
    }

    @Test
    public void getSearchIdDemo2() {
        try {
            SearchDataGenerator searchDataGenerator = SearchDataGenerator
                    .builder()
                    .role("ROLE_TEST")
                    .publicKey(TestConstant.publicKey)
                    .caseId("（2000）最高法民再111号")
                    .caseCause("盗窃罪")
                    .user("wangxiaoming", "王小明")
                    .court("石家庄市中级人民法院", "1234")
                    .addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent)
                    .addDocument(DocumentType.OTHER, "文书", "庭审笔录.doc")
                    .downloadReport()
                    .addParty(PartyType.PEOPLE, "张三", "123123199001010001", "被告")
                    .addParty(PartyType.PEOPLE, "李四", "123123199001010002", "原告")
                    .nationwide()
                    .build();
            String searchId = searchDataGenerator.getSearchId();
            System.out.println("Search ID: " + searchId);

            System.out.println(searchDataGenerator.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSearchIdDemo() {
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
            System.out.println("Search ID: " + searchId);

            SearchVO searchVO = searchDataGenerator.getSearch();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(searchVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getSearchIdWithoutPartyDemo() {
        try {
            BasicInfo basicInfo = new BasicInfo("（2000）最高法民再111号", "盗窃罪", null, null);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(TestConstant.publicKey, "ROLE_TEST", basicInfo);
            searchDataGenerator.addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent);
            searchDataGenerator.addDocument(DocumentType.OTHER, "文书");
            searchDataGenerator.setCourt("石家庄市中级人民法院", "1234");
            searchDataGenerator.setUser("wangxiaoming", "王小明");
            searchDataGenerator.reportDownload();
            String searchId = searchDataGenerator.getSearchId();
            System.out.println("Search ID: " + searchId);

            SearchVO searchVO = searchDataGenerator.getSearch();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(searchVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getSearchIdWithFtpDemo() {
        try {
            FtpVO ftpVO = new FtpVO();
            ftpVO.setHost("192.168.1.1");
            ftpVO.setPort(21);
            ftpVO.setUsername("demo");
            ftpVO.setPassword("demo");
            ftpVO.setEncoding("utf-8");
            ftpVO.setWorkDir("/report");


            BasicInfo basicInfo = new BasicInfo("（2000）最高法民再111号", "盗窃罪", null, null);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(TestConstant.publicKey, "ROLE_TEST", basicInfo);
            searchDataGenerator.addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent);
            searchDataGenerator.addDocument(DocumentType.OTHER, "文书");
            searchDataGenerator.setCourt("石家庄市中级人民法院", "1234");
            searchDataGenerator.setUser("wangxiaoming", "王小明");
            searchDataGenerator.saveReport2Ftp(ftpVO);
            String searchId = searchDataGenerator.getSearchId();
            System.out.println("Search ID: " + searchId);

            SearchVO searchVO = searchDataGenerator.getSearch();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(searchVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getSearchIdWithOssDemo() {
        try {
            OssVO ossVO = new OssVO();
            ossVO.setEndpoint("http://oss-cn-beijing.aliyuncs.com");
            ossVO.setAccessKeyId("xxx");
            ossVO.setAccessKeySecret("xxx");


            BasicInfo basicInfo = new BasicInfo("（2000）最高法民再111号", "盗窃罪", null, null);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(TestConstant.publicKey, "ROLE_TEST", basicInfo);
            searchDataGenerator.addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent);
            searchDataGenerator.addDocument(DocumentType.OTHER, "文书");
            searchDataGenerator.setCourt("石家庄市中级人民法院", "1234");
            searchDataGenerator.setUser("wangxiaoming", "王小明");
            searchDataGenerator.saveReport2Oss(ossVO);
            String searchId = searchDataGenerator.getSearchId();
            System.out.println("Search ID: " + searchId);

            SearchVO searchVO = searchDataGenerator.getSearch();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(searchVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getSearchIdWithApiDemo() {
        try {
            ApiVO apiVO = new ApiVO();
            apiVO.setName("apiname");
            apiVO.setUrl("http://demo.com/demo");


            BasicInfo basicInfo = new BasicInfo("（2000）最高法民再111号", "盗窃罪", null, null);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(TestConstant.publicKey, "ROLE_TEST", basicInfo);
            searchDataGenerator.addDocument(DocumentType.PAN_JUE_SHU, TestConstant.documentContent);
            searchDataGenerator.addDocument(DocumentType.OTHER, "文书");
            searchDataGenerator.setCourt("石家庄市中级人民法院", "1234");
            searchDataGenerator.setUser("wangxiaoming", "王小明");
            searchDataGenerator.callBackApi(apiVO);
            String searchId = searchDataGenerator.getSearchId();
            System.out.println("Search ID: " + searchId);

            SearchVO searchVO = searchDataGenerator.getSearch();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(searchVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
