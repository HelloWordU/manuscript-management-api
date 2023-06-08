package com.rz.manuscript.common;

import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.entity.CacheEntity;

import javax.servlet.http.HttpServletRequest;

public class LoginUserUtils {
    public static User getCurrentLoginUser(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return null;
        }
        return ((User) cacheInfo.getValue());
    }
}
