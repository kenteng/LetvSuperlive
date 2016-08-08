package com.lesports.stadium.cache;


import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.lesports.stadium.cache.CacheManagerConst.CacheDirctory;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;
import com.lesports.stadium.utils.GlobalParams;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

public class CacheManager {
    
    private static final String TAG = CacheManager.class.getName();
    
    private String cacheRootPath;    //用户缓存的root目录
    
    // 目录列表集合
    private Map <Integer ,String> cacheDirectoryList =  new LinkedHashMap<Integer ,String>();
    
    /**
     * 缓存管理类实例
     */
    private volatile static CacheManager instance = null;

    
    
    /**
     * 时至缓存的root目录
     * @param cacheRootPath
     */
    public void setCacheRootPath(String cacheRootPath) {
		this.cacheRootPath = cacheRootPath;
	}


	/**
     * 保存对象的唯一性
     * @return
     */
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    
    /**
     * 初始化缓存目录 ,这里会把缓存管理定义的目录都创建好
     * 
     * return :成功返回true ,失败返回false.
     */
    public boolean initCacheDirectory(){        
        // 初始化目录列表
        this.initCacheDirList();        
        if(cacheRootPath == null){
        	try{
        		cacheRootPath=FileUtil.getCacheRootDirPath(GlobalParams.context);
        		if(!cacheRootPath.endsWith("/")){
        			cacheRootPath = cacheRootPath+"/";
        		}

        	}catch(Exception e){
        		return false;
        	}
        }
        
        // 创建目录
        for (Iterator<Integer> it =  cacheDirectoryList.keySet().iterator();it.hasNext();)
        {
            Object key = it.next();
            FileUtil.createDirectory(cacheRootPath + cacheDirectoryList.get(key));
        }
        return true;
    }
    
    /**
     * 添加缓存目录
     * 添加的目录都在
     */
    public void addCachePath(int key,String path){
    	cacheDirectoryList.put(key, path);
    }
    
    /*
     * 初始化所有缓存文件夹，应该在application的onCreate方法中调用
     */
    private void initCacheDirList(){
        
        // root目录
        cacheDirectoryList.put(CacheType.CACHE_ROOT, CacheDirctory.CACHE_ROOT_PATH);
        // 一级目录
        cacheDirectoryList.put(CacheType.CACHE_TMP, CacheDirctory.CACHE_DIR_TMP);
        cacheDirectoryList.put(CacheType.CACHE_VIDEO, CacheDirctory.CACHE_VIDEO_PATH);
        cacheDirectoryList.put(CacheType.CACHE_IMAGE, CacheDirctory.CACHE_IMAGE_PATH);
        cacheDirectoryList.put(CacheType.CACHE_MATERIAL, CacheDirctory.CACHE_MATERIAL_PATH);
        cacheDirectoryList.put(CacheType.CACHE_NETWORK, CacheDirctory.CACHE_NETWORK_PATH);
        cacheDirectoryList.put(CacheType.CACHE_CRASH, CacheDirctory.CACHE_CRASH);
        cacheDirectoryList.put(CacheType.CACHE_LOG, CacheDirctory.CACHE_LOG);
        // 二级目录
        cacheDirectoryList.put(CacheType.CACHE_IMAGE_USER_HEADER, CacheDirctory.CHILD_IMAGE_USER_HEADER);
        cacheDirectoryList.put(CacheType.CACHE_IMAGE_FAMILY_HEADER, CacheDirctory.CHILD_IMAGE_FAMILY_HEADER);
        cacheDirectoryList.put(CacheType.CACHE_IMAGE_PIRCTURE, CacheDirctory.CHILD_IMAGE_PICTURE);
    }
    
    /**
     * 根据cacheType 返回缓存目录
     * 
     * 只返回文件路径，不判断文件是否存在,这块判断文件是否存在放到具体业务里面去做。
     */
    public  String getCachePath(int cacheType) {
        String cacheFilePath = cacheRootPath + cacheDirectoryList.get(cacheType) + "/";
        return cacheFilePath;
    }

    /**
     * 根据key值和cacheType 返回缓存文件的路径
     * 这里的key 基本上是url
     * 只返回文件路径，不判断文件是否存在,这块判断文件是否存在放到具体业务里面去做。
     */
    public  String getCachePath(String key ,int cacheType) {
        String cacheFilePath = cacheRootPath + cacheDirectoryList.get(cacheType) + "/" +key;
        return cacheFilePath;
    }

    
    /**
     * 判断缓存是否有效
     * 根据缓存的结束时间
     * @param key               缓存内容的key值，一般为网络url[说明，这里的key是业务层根据md5已经生成的key了，不是url了]
     * @param cacheType         缓存类型
     * @param cacheEndTime      缓存失效的时间，单位是毫秒
     * return                   : 如果没有过期则返回true
     */
    public  boolean isValidByEndTime(String key, int cacheType, long cacheEndTimeParam) {

        String cacheFilePath = cacheRootPath + cacheDirectoryList.get(cacheType) + "/" + key;  //StringUtil.md5(key);
        // 如果文件存在则返回文件路径，如果文件不存在，则返回null
        File f = new File(cacheFilePath);
        if(!f.exists()){
            return false;   // 文件不存在，直接返回
        }
        //缓存有效截止时间
        long cacheEndTime = cacheEndTimeParam;

        return ( cacheEndTime > System.currentTimeMillis());
    }
    
    /**
     * 判断缓存是否有效
     * 根据缓存的有效时长
     * @param key                       缓存内容的key值，一般为网络url
     * @param cacheType                 缓存类型
     * @param cacheDurationTimeParam    缓存距离上次修改的有效时间，单位是毫秒
     * return                   : 如果没有过期则返回true
     */
    public  boolean isValidByDurationTime(String key, int cacheType, long cacheDurationTimeParam) {
        String cacheFilePath = cacheRootPath + cacheDirectoryList.get(cacheType) + "/" + key;  //StringUtil.md5(key);
        // 如果文件存在则返回文件路径，如果文件不存在，则返回null
        File f = new File(cacheFilePath);
        if(!f.exists()){
            return false;   // 文件不存在，直接返回
        }
        //文件的最新更新时间
        long fileLastModifyTime = f.lastModified();
        //缓存有效截止时间
        long cacheValidDuraionTime = cacheDurationTimeParam;

        return ((fileLastModifyTime + cacheValidDuraionTime) > System.currentTimeMillis());
    }

    /**
     * 根据cacheType 清空缓存文件
     * @param cacheType             缓存类型
     * @param isRemoveDir           是否删除目录
     * return : true 成功清空缓存
     */
   public   boolean removeCacheFile(int cacheType ,final boolean isRemoveDir) {
        
        // 构建目录
        String cacheFileDir = cacheRootPath + cacheDirectoryList.get(cacheType) ;
        final File dir = new File(cacheFileDir);
        
        // delete cache in a separate thread to not block UI.
        final Runnable clearCache = new Runnable() {
            public void run() {
                // 删除文件
                delete( dir ,isRemoveDir);
            }
        };
        new Thread(clearCache).start();
        return true;
    }
    
    /**
     * remove all cache files
     * @param isRemoveDir   是否删除目录
     * @return true if it succeeds
     */
    public boolean removeAllCacheFiles(final boolean isRemoveDir) {
     
     // delete cache in a separate thread to not block UI.
        final Runnable clearCache = new Runnable() {
            public void run() {
                // delete all cache files
                File baseDirFile = new File(cacheRootPath);
                try {
                    String[] files = baseDirFile.list();
                    for (int i = 0; i < files.length; i++) {
                        // 删除文件
                        delete( new File(baseDirFile, files[i]) ,isRemoveDir);
                    }
                } catch (SecurityException e) {
                    // Ignore SecurityExceptions.
                }
            }
        };
        new Thread(clearCache).start();
        return true;
    }

    /**
     * 删除单个文件或者目录
     * @param dir
     * @param isRemoveDir  是否删除目录 ，true 是删除目录
     * @return
     */
    private boolean delete(File dir ,boolean isRemoveDir) {
        // 删除文件
        if (dir.isDirectory())  
        {  
            File[] listFiles = dir.listFiles();  
            for (int i = 0; i < listFiles.length; i++)  
            {
                delete(listFiles[i], isRemoveDir);
            }
            
            if(!isRemoveDir){  // 如果不删除目录
                return true;
            }
        }  
        //
        return dir.delete();
    }
    
    // TODO 
    /**
     * 根据cacheType 当前缓存目录占用多少空间
     * @param cacheType             缓存类型
     * return : long 缓存大小，单位是字节
     */
    public long getCacheDirSize(int cacheType) {
        // 构建目录
        String cacheFileDir;
        if(cacheType == CacheType.CACHE_ROOT){
            cacheFileDir = cacheRootPath;
        }else{
            cacheFileDir = cacheRootPath + cacheDirectoryList.get(cacheType) ;
        }
        File dir = new File(cacheFileDir);
        return getDirSize(dir);
    }

    /**
     * 
     * Return the size of a directory in bytes
     */
    private long getDirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += getDirSize(fileList [i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }
    
    /**
     * @Description 当前sd卡 可用空间多少 MB
     * @return int
     *            返回sd卡可用空间大小.
     */
    public  int validSDSpace() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        @SuppressWarnings("deprecation")
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        @SuppressWarnings("deprecation")
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        int freeSize = (int)((freeBlocks * blockSize) / 1024 / 1024); // 单位MB
        return freeSize;
    }
    
    /**
     * 根据缓存类型获取Cache目录
     */
    public  boolean  checkCacheFileDir(int cacheType){
     // 构建目录
        String cacheFileDir = cacheRootPath + cacheDirectoryList.get(cacheType) ;
        final File dir = new File(cacheFileDir);
        if(!dir.exists()){
            return dir.mkdirs();
        }else{
            return true;
        }
    }
    
    /**
     * 获取临时文件tmp文件的存储目录
     */
    public  String  getCacheDirForTempFile(){
        // 构建目录
        checkCacheFileDir(CacheType.CACHE_TMP);
        return cacheRootPath + cacheDirectoryList.get(CacheType.CACHE_TMP) ;
    }
}
