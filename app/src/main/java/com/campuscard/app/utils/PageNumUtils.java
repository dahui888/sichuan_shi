package com.campuscard.app.utils;

public class PageNumUtils {

    /**
     * 获取总页数
     * allPage // 计算出来的总页数
     * totalRecord // 数据的总数
     * pageSize 每页返回的数据总数
     */
    public static int getAllPageCount(int totalRecord, int pageSize) {
//        int allPage = (totalCount - 1) / pageSize + 1;
        int allPage = (totalRecord + pageSize - 1) / pageSize;
        return allPage;
    }
}
