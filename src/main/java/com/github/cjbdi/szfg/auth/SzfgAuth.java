package com.github.cjbdi.szfg.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjbdi.szfg.core.vo.ApiVO;
import com.github.cjbdi.szfg.core.vo.FtpVO;
import com.github.cjbdi.szfg.core.vo.OssVO;

import java.time.LocalDateTime;

/**
 * @author Boning Liang
 */
public class SzfgAuth {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getAccessKey(String leianRole, String pass) {
        return AuthUtil.encryptAES(leianRole + "&" + LocalDateTime.now().toString(), pass);
    }

    public static String getFtp(FtpVO ftpVO, String publicKey) throws Exception {
        return AuthUtil.encryptRSA(objectMapper.writeValueAsString(ftpVO), publicKey);
    }

    public static String getOss(OssVO ossVO, String publicKey) throws Exception {
        return AuthUtil.encryptRSA(objectMapper.writeValueAsString(ossVO), publicKey);
    }

    public static String getApi(ApiVO apiVO, String publicKey) throws Exception {
        return AuthUtil.encryptRSA(objectMapper.writeValueAsString(apiVO), publicKey);
    }

}
