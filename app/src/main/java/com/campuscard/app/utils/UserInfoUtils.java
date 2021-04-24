package com.campuscard.app.utils;


import android.text.TextUtils;

import com.campuscard.app.Constant;
import com.campuscard.app.ui.entity.UserInfoEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 用户信息处理类
 */
public class UserInfoUtils {


    /**
     * 清除登录后缓存
     */
    public static void clearUserInfo() {
        SharedCacheUtils.put(Constant.USER_INFO_TAG, "");
        SharedCacheUtils.put(Constant.TOKEN, "");
    }

    /**
     * 保存用户信息类
     *
     * @param userAllInfo
     */
    public static void cacheUserInfo(UserInfoEntity userAllInfo) {
        Gson gson = new Gson();
        String userInfoStr = gson.toJson(userAllInfo);
        SharedCacheUtils.put(Constant.USER_INFO_TAG, userInfoStr);
    }

    /**
     * 保存用户token
     *
     * @param token
     */
    public static void cacheToken(String token) {
        SharedCacheUtils.put(Constant.TOKEN, token);
    }


    /**
     * 得到用户token
     */
    public static String getToken() {
        return SharedCacheUtils.get(Constant.TOKEN, "");
    }


    /**
     * 获取用户信息类
     *
     * @return
     */
    public static UserInfoEntity getUserInfo() {
        UserInfoEntity resultData = new UserInfoEntity();
        String userInfoStr = SharedCacheUtils.get(Constant.USER_INFO_TAG, "");
        if (!TextUtils.isEmpty(userInfoStr)) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserInfoEntity>() {
            }.getType();
            resultData = gson.fromJson(userInfoStr, type);
        }
        return resultData;
    }


}
