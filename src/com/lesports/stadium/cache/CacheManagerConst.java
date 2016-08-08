package com.lesports.stadium.cache;

public class CacheManagerConst {

    /**
     * 一分钟 60秒*1000
     */
    public final static long EXPIRATION_MINUTE = 60 * 1000;
    public final static long EXPIRATION_HOUR =  60 * EXPIRATION_MINUTE;
    public final static long EXPIRATION_DAY =  24 * EXPIRATION_HOUR;
    public final static long EXPIRATION_WEEK = 7 * EXPIRATION_DAY;

    /**
     * 缓存目录类型
     */
    public class CacheType {
        /* 缓存root目录 */
        public static final int CACHE_ROOT = 0;
         /* 视频缓存 */
        public static final int CACHE_VIDEO = 1;
         /* 图片缓存 */
        public static final int CACHE_IMAGE = 2;
         /* 其他缓存 */
        public static final int CACHE_MATERIAL = 3;
         /* 网络缓存 */
        public static final int CACHE_NETWORK = 4;
        /* 异常日志*/
        public static final int CACHE_CRASH=5;
        /* log日志*/
        public static final int CACHE_LOG=6;
        
        

        /* 网络缓存 */
       public static final int CACHE_TMP = 20;

        /*- 二级目录缓存--*/
        /* 用户头像 */
        public static final int CACHE_IMAGE_USER_HEADER = 100;
        /* 家庭头像 */
        public static final int CACHE_IMAGE_FAMILY_HEADER = 101;
        /* 用户发布的图 */
        public static final int CACHE_IMAGE_PIRCTURE = 102;
        /* 聊天模块的图 */
        public static final int CACHE_IMAGE_CHAT = 103;
        /* 其他模块的图 */
        public static final int CACHE_IMAGE_OTHER = 104;
    }

    /**
     * 缓存目录相对路径
     */
    public class CacheDirctory{
        /**
         * 缓存root目录
         */
        public static final String CACHE_ROOT_PATH            = "cache";

        /*-------------缓存root目录下一级目录----------------------*/
        /**
         * 异常文件
         */
        public static final String CACHE_CRASH				= CACHE_ROOT_PATH + "/crash";
        
        /**
         * log文件
         */
        public static final String CACHE_LOG				= CACHE_ROOT_PATH + "/log";
        
        /**
         * 临时文件缓存目录
         */
        public static final String CACHE_DIR_TMP            = CACHE_ROOT_PATH + "/tmp";
        /**
         * 视频缓存目录
         */
        public static final String CACHE_VIDEO_PATH          = CACHE_ROOT_PATH + "/video";
        /**
         * 图片缓存目录
         */
        public static final String CACHE_IMAGE_PATH          = CACHE_ROOT_PATH + "/image";
        /**
         * 图片缓存二级目录
         */
        // 用户头像图片在图片缓存目录下的子目录名称
        public static final String CHILD_IMAGE_USER_HEADER = CACHE_IMAGE_PATH + "/header_user";
        // 用户家庭头像图片在图片缓存目录下的子目录名称
        public static final String CHILD_IMAGE_FAMILY_HEADER = CACHE_IMAGE_PATH + "/header_family";
        // 用户发布图片在图片缓存目录下的子目录名称
        public static final String CHILD_IMAGE_PICTURE = CACHE_IMAGE_PATH + "/picture";
        // 用户聊天模块发布在图片缓存目录下的子目录名称
        public static final String CHILD_IMAGE_CHAT = CACHE_IMAGE_PATH + "/chat";
        // 用户图片其他的图片保存目录
        public static final String CHILD_IMAGE_OTHERS = CACHE_IMAGE_PATH + "/other";
        //--------------------------   end  ------------------------------------------
        
        /**
         * 其他缓存目录
         */
        public static final String CACHE_MATERIAL_PATH = CACHE_ROOT_PATH + "/material";
        
        /**
         * 网络缓存目录
         */
        public static final String CACHE_NETWORK_PATH = CACHE_ROOT_PATH + "/network";
    }
    
    /**
     * 缓存有效期类型
     *
     */
    public class ValidityType{
        /**
         * 时间段缓存
         */
        public static final int CACHE_DURATION_TIME=1;
       
        /**
         * 截止日期缓存
         */
        public static final int CACHE_END_TIME=2;
    }

}
