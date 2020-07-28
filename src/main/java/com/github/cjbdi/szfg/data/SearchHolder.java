package com.github.cjbdi.szfg.data;

import com.github.cjbdi.szfg.core.vo.SearchVO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author Boning Liang
 */
public class SearchHolder {

    private static Cache<String, SearchVO> instance = null;

    public static synchronized Cache<String, SearchVO> getInstance() {
        if (instance == null) {
            instance = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(1, TimeUnit.MINUTES)
                    .expireAfterAccess(10, TimeUnit.SECONDS)
                    .build();
        }
        return instance;
    }


}
