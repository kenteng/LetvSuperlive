package com.lesports.stadium.utils;


import android.content.Context;
/**
 * 
 * @ClassName:  DensityUtil   
 * @Description:dp 和px 之间的转换  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:55:42   
 *
 */
public class DensityUtil {
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
