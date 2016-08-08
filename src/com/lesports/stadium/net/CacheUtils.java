package com.lesports.stadium.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.lesports.stadium.net.Code.HttpSetting;

/**
 * 
 * @ClassName:  CacheUtils   
 * @Description:获取缓存数据   
 * @author: 王新年 
 * @date:   2015-12-28 下午5:54:13   
 *
 */
public class CacheUtils {
   
    /**
     * 获取缓存数据
     * 
     * @param path
     * @return
     */
    public static String getCacheData(String path)throws Exception {
        File file = new File(path);
        String result = "";
        if (file.exists()) {
            InputStreamReader isr;
            try {
                isr = new InputStreamReader(new FileInputStream(file), HttpSetting.CHARSET);
                BufferedReader br = new BufferedReader(isr);
                String mimeTypeLine = null;
                while ((mimeTypeLine = br.readLine()) != null) {
                    result = result + mimeTypeLine;
                }  
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
       return result;
    }
}
