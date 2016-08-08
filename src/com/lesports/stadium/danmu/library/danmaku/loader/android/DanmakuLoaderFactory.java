package com.lesports.stadium.danmu.library.danmaku.loader.android;



import com.lesports.stadium.danmu.library.danmaku.loader.ILoader;

public class DanmakuLoaderFactory {

    public static String TAG_BILI = "bili";
    public static String TAG_ACFUN = "acfun";
    
    public static ILoader create(String tag) {
        if (TAG_BILI.equalsIgnoreCase(tag)) {
            return BiliDanmakuLoader.instance();
        } else if(TAG_ACFUN.equalsIgnoreCase(tag))
        	return AcFunDanmakuLoader.instance();
        return null;
    }

}
