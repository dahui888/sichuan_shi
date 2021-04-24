package com.base.frame.utils;


/**
 * 目录配置
 */
public class XAppConfig {
    /**
     * UI设计的基准宽度.
     */
    public static int UI_WIDTH = 720;

    /**
     * UI设计的基准高度.
     */
    public static int UI_HEIGHT = 1280;

    /**
     * UI设计的密度.
     */
    public static int UI_DENSITY = 2;

    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_ROOT_DIR = "";

    /**
     * 默认下载图片文件地址.
     */
    public static String DOWNLOAD_IMAGE_DIR = "images";

    /**
     * 默认下载文件地址.
     */
    public static String DOWNLOAD_FILE_DIR = "files";

    /**
     * APP缓存目录.
     */
    public static String CACHE_DIR = "cache";

    /**
     * DB目录.
     */
    public static String DB_DIR = "db";

    /**
     * 默认磁盘缓存超时时间设置，毫秒.
     */
    public static long DISK_CACHE_EXPIRES_TIME = 60 * 1000 * 1000;

    /**
     * 内存缓存大小  单位10M.
     */
    public static int MAX_CACHE_SIZE_INBYTES = 10 * 1024 * 1024;

    /**
     * 磁盘缓存大小  单位10M.
     */
    public static int MAX_DISK_USAGE_INBYTES = 100 * 1024 * 1024;

}
