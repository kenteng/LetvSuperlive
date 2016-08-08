package com.lesports.stadium.service;

import java.util.List;

import com.lesports.stadium.app.LApplication;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
/***
 * 监听程序是否在前后台运行Service
 * @author Administrator
 *
 */
public class AppStatusService extends Service{
    private static final String TAG = "AppStatusService"; 
    private ActivityManager activityManager; 
    private String packageName;
    private boolean isStop = false;
    private  LApplication alication;
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void onCreate() {
    	super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE); 
        packageName = this.getPackageName();
        alication = (LApplication) getApplication();
        new Thread() { 
            public void run() { 
                try { 
                    while (!isStop) { 
                        Thread.sleep(300); 
                        if (isAppOnForeground()) { 
//                            Log.i(TAG, "前台运行");
                            String url = alication.getGuangGaoUrl();
                            if(!TextUtils.isEmpty(url))
                                  alication.showGuanggaoView(url);
                        } else { 
//                            Log.i(TAG, "后台运行");
                            alication.removeView();
                        } 
                    } 
                } catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            } 
        }.start(); 
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    /**
     * 程序是否在前台运行
     * @return
     */
    public boolean isAppOnForeground() { 
        // Returns a list of application processes that are running on the device 
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses(); 
        if (appProcesses == null) return false; 
        
        for (RunningAppProcessInfo appProcess : appProcesses) { 
            // The name of the process that this object is associated with. 
            if (appProcess.processName.equals(packageName) 
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) { 
                return true; 
            } 
        } 
        
        return false; 
    } 
    
    @Override
    public void onDestroy() {
    	isStop = true;
        super.onDestroy();
    }
}
