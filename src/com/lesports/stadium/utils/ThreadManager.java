package com.lesports.stadium.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 
 * @ClassName:  ThreadManager   
 * @Description:线程池管理线程 
 * @author: 王新年 
 * @date:   2015-12-28 下午5:58:40   
 *
 */
public class ThreadManager {

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = 3;

    /**
     * 最大线程数
     */

    private static final int MAXIMUM_POOL_SIZE = 6;

    /**
     * 线程生存时间，单位秒
     */
    private static final long KEEP_ALIVE_TIME = 10;

    /**
     * 线程池对象
     */
    private static ThreadPoolExecutor threadPoolExecutor;
    
    /**
     * 单例实例变量
     */
    private volatile static ThreadManager instance = null;
    
    
    /**
     * @Description 
     *              获取线程管理类实例
     */ 
    public static ThreadManager getInstance() {
        /* 双重校验锁 */
        if (instance == null) {
            synchronized (ThreadManager.class) { 
                if(instance==null) {
                    synchronized(ThreadManager.class) {
                        instance = new ThreadManager();
                    }
                }
            }
        }
        return instance;
    }
    
    private ThreadManager() {
        threadPoolExecutor = new ThreadPoolExecutor(
                                            CORE_POOL_SIZE,
                                            MAXIMUM_POOL_SIZE,
                                            KEEP_ALIVE_TIME,
                                            TimeUnit.SECONDS,
                                            new LinkedBlockingQueue<Runnable>(),
                                            new ThreadPoolExecutor.DiscardOldestPolicy());
    }
    
    /**
     * @Description 
     *              往任务队列里添加任务
     */ 
    public void addTask(Runnable task) {
        
        threadPoolExecutor.execute(task);
    }
 
    /**
     * @Description
     *              关闭线程池
     */
    public void shutdown() {
        
        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
            return;
        }
        threadPoolExecutor.shutdown();
    }

}
