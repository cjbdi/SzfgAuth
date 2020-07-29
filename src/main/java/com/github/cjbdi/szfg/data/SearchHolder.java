package com.github.cjbdi.szfg.data;

import cn.hutool.core.util.StrUtil;
import com.github.cjbdi.szfg.core.vo.SearchVO;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Boning Liang
 */
public class SearchHolder {

    private static ConcurrentHashMap<String, SearchCache> searchCache = new ConcurrentHashMap<>();

    private static Cache<String, SearchCache> googleCache = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(2, TimeUnit.DAYS)
            .expireAfterAccess(1, TimeUnit.DAYS)
            .build();

    public static SearchVO getSearchByGoogleCache(String id) {
        if (StrUtil.isNotBlank(id)) {
            SearchCache cache = googleCache.getIfPresent(id);
            if (cache != null) {
                return cache.searchVO;
            }
            return null;
        }
        return null;
    }

    public static SearchVO getSearch(String id) {
        if (StrUtil.isNotBlank(id)) {
            SearchCache cache = searchCache.get(id);
            if (cache != null) {
                return cache.searchVO;
            }
            return null;
        }
        return null;
    }

    public static void addSearch(String id, SearchVO searchVO) {
        if (StrUtil.isNotBlank(id) && searchVO != null) {
            SearchCache cache = new SearchCache();
            cache.searchVO = searchVO;
            searchCache.put(id, cache);
        }
    }

    public static void addSearchIntoGoogleCache(String id, SearchVO searchVO) {
        if (StrUtil.isNotBlank(id) && searchVO != null) {
            SearchCache cache = new SearchCache();
            cache.searchVO = searchVO;
            googleCache.put(id, cache);
        }
    }
}

class SearchCache {

    SearchCache() {
        this.createTime = LocalDateTime.now();
    }

    private LocalDateTime createTime;

    SearchVO searchVO;

}
