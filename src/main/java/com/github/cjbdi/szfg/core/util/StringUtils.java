package com.github.cjbdi.szfg.core.util;

import java.util.UUID;

/**
 * @author Boning Liang
 */
public class StringUtils {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

}
