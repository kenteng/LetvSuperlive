package com.lesports.stadium.utils.imageLoader;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.lesports.stadium.utils.LogUtil;

import android.graphics.Bitmap;
 
/**
 * 
 * @ClassName:  MemoryCache   
 * @Description:内存管理 
 * @author: 王新年 
 * @date:   2015-12-28 下午5:36:59   
 *
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache=Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10,1.5f,true));//Last argument true for LRU ordering
    private long size=0;//current allocated size
    private long limit=1000000;//max memory in bytes

    public MemoryCache(){
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }
    /**
     * 
     * @Title: setLimit   
     * @Description: 设置SD卡中最大缓存   
     * @param: @param new_limit      
     * @return: void      
     * @throws
     */
    public void setLimit(long new_limit){
        limit = new_limit;
        LogUtil.i(TAG, "MemoryCache will use up to "+limit/1024./1024.+"MB");
    }
    /**
     * 
     * @Title: get   
     * @Description: 根据id返回临时缓存中图片 
     * @param: @param id
     * @param: @return      
     * @return: Bitmap      
     * @throws
     */
    public Bitmap get(String id){
        try{
            if(!cache.containsKey(id))
                return null;
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            return cache.get(id);
        }catch(NullPointerException ex){
            return null;
        }
    }
    /**
     * 
     * @Title: put   
     * @Description:将图片添加到缓存中  
     * @param: @param id
     * @param: @param bitmap      
     * @return: void      
     * @throws
     */
    public void put(String id, Bitmap bitmap){
        try{
            if(cache.containsKey(id))
                size-=getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size+=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }
    /**
     * 
     * @Title: checkSize   
     * @Description: 查看是否超过最大内存 
     * @param:       
     * @return: void      
     * @throws
     */
    private void checkSize() {
    	LogUtil.i(TAG, "cache size="+size+" length="+cache.size());
        if(size>limit){
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();//least recently accessed item will be the first one iterated  
            while(iter.hasNext()){
                Entry<String, Bitmap> entry = iter.next();
                size-= getSizeInBytes(entry.getValue());
                iter.remove();
                if(size <= limit/2)
                    break;
            }
            LogUtil.i(TAG, "Clean cache. New size "+cache.size());
        }
    }
    /**
     * 
     * @Title: clear   
     * @Description: 清除缓存  
     * @param:       
     * @return: void      
     * @throws
     */
    public void clear() {
        cache.clear();
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}