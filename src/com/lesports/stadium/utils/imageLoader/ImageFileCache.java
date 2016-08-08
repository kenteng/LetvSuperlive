package com.lesports.stadium.utils.imageLoader;


import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.utils.UIUtils;

import android.content.Context;
 
/**
 * 
 * @ClassName:  ImageFileCache   
 * @Description:图片缓存  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:27:14   
 *
 */
public class ImageFileCache {
    /**
     * 图片缓存根目录
     */
    private File cacheDir;
    
    private final int MB = 1024 * 1024;
    private final int CACHE_SIZE = 3; // 限制文件包缓存图片目录大小最大为3M
    private final int SD_CACHE_SIZE = 10; // 限制文件图片缓存目录大小最大为10M
    
    public ImageFileCache(Context context){
    	String file = CacheManager.getInstance().getCachePath(CacheType.CACHE_IMAGE);
        cacheDir = new File(file);
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }
    /**
     * 
     * @Title: getFile   
     * @Description: 根据网络路径 获取本地图片路径  
     * @param: @param url
     * @param: @return      
     * @return: File      
     * @throws
     */
    public File getFile(String url){
        final String filename = UIUtils.getMD5Str(url) + ".dat";
        File f = new File(cacheDir, filename);
        if(f.exists()){
        	f.setLastModified(System.currentTimeMillis());
        }
        return f;
    }
    
    public File getNewFile(String url){
        final String filename = UIUtils.getMD5Str(url) + ".jpg";
        File f = new File(cacheDir, filename);
        if(f.exists()){
        	f.setLastModified(System.currentTimeMillis());
        }
        return f;
    }
    /**
     * 清理缓存
     * 计算当前缓存目录大小
     * 删除40%最近没有被使用的文件
     * 
     */
    public synchronized void clear(){
        try {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return;
			Arrays.sort(files, new FileLastModifSort());
			
			double freeSpaceOnSd = UIUtils.getAvailableExternalMemory();
			int cacheDirSize = 0; // 计算当前缓存目录大小
			for (int i = 0; i < files.length; i++) {
				cacheDirSize += files[i].length();
			}
			int removeFactor = (int) ((0.4 * files.length));
			// 有SD卡并且当前图片文件目录缓存大等于10M 或者 SD卡剩余空小于3M
			if (UIUtils.isSDcardExist()
					&& ((cacheDirSize >= SD_CACHE_SIZE * MB) || freeSpaceOnSd < 3)) {
				for (int i = 0; i < removeFactor; i++) {
					files[i].delete();
				}
			}
			// 没有SD卡并且程序内缓存目录大等于3M
			if (!UIUtils.isSDcardExist() && cacheDirSize >= CACHE_SIZE * MB) {
				for (int i = 0; i < removeFactor; i++) {
					files[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 根据文件的最后修改时间进行升序排序 
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
        	 long d1 = arg0.lastModified();
             long d2 = arg1.lastModified();
             if (d1 == d2){
                 return 0;
             } else {
                 return d1 > d2 ? 1 : -1;
             }
        }
    }
    
}