package com.github.cjbdi.szfg.auth;

import java.time.LocalDateTime;

/**
 * @author Boning Liang
 */
public class SzfgAuth {

    public static String getAccessKey(String leianRole, String pass) {
        return AuthUtil.encryptAES(leianRole + "&" + LocalDateTime.now().toString(), pass);
    }

}
