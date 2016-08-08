/*****************************************************************************
 * 网络请求模块 通用的头部信息 ，单例
 * x-ennew-req = appid/versioncode/otaVersion/pid/uid/mid/did/sid
 * 各字段：
 * appid	产品ID
 * versionCode	版本号
 * otaVersion	配置版本	采用自增正整数，从1开始，每个版本加1。客户端参数配置通过OTAVersion进行更新控制，更新过程采用静默方式。
 * pid	渠道ID int类型
 * uid	用户ID
 * mid	设备型号ID  int类型
 * did	设备ID号    string类型
 * sid	会话ID      string 类型
 *
 *****************************************************************************
 * Copyright (C) 2015-2016
 *
 * Authors: ShengZhiGang, ChrisZhang <zhangyanlongcodec@gmail.com>
 *
 *
 */
package com.lesports.stadium.net;

import com.lesports.stadium.net.Code.HeaderMsg;
import com.lesports.stadium.utils.GlobalParams;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;


public class RequestHeader {

    public static RequestHeader instance;
    
    /**
     * 产品ID
     */
    private  int appId;
    
    /**
     * 版本号
     */
    private  int versionCode=1;
    
    /**
     *配置版本
     *客户端参数配置通过OTAVersion进行更新控制，更新过程采用静默方式
     */
    private  int otaVersion;
    
    /**
     * 渠道ID
     */
    private int pid;
    
    /**
     * 用户ID
     * 平台内用户唯一标识，由服务端根据一定规则生成。
     * 客户端缺省值0
     */
    private long uid;
    
    /**
     * 设备型号ID
     */
    private int mid;
    
    /**
     * 设备ID
     */
    private String did;
    
    /**
     * 会话ID
     * 如果客户端没有sid，则该header传空字符串；
     * 如果客户端缓存（内存）有sid，则通过该header传递，由服务端判断是否有效；
     *  服务端传递了新的sid，客户端需更新缓存（内存）中的sid
     */
    private String sid;

    /**
     *  构造函数
     */
    public RequestHeader(){
        this.appId          = HeaderMsg.APPID;
        this.versionCode    = getVersionCode();
        this.otaVersion     = HeaderMsg.OTA_VERSION;
        this.pid            = HeaderMsg.PID;
        this.uid            = HeaderMsg.UID;
        this.mid            = HeaderMsg.MID;
        this.did            = HeaderMsg.DID;
        this.sid            = HeaderMsg.SID;
    }

    public static RequestHeader getInstance(){
        /* 双重校验锁 */
        if (instance == null) {
            synchronized (RequestHeader.class) {
                if(instance==null) {
                    synchronized(RequestHeader.class) {
                        instance = new RequestHeader();
                    }
                }
            }
        }
        return instance;
    }

    /**
     * 设置产品ID
     * @param appId
     *              产品ID
     */
    public void setAppId(int appId) {
        this.appId = appId;
    }

    /**
     * 设置版本号
     * @param versionCode
     *                  产品版本号
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 设置配置版本
     * @param otaVersion
     *                  产品的配置版本号
     */
    public  void setOtaVersion(int otaVersion) {
        this.otaVersion = otaVersion;
    }
    
    /**
     * 设置渠道ID
     * @param pid
     *            产品的渠道id
     */
    public void setPid(int pid) {
        this.pid = pid;
    }
    
    /**
     * 设置用户ID
     * @param uid
     *          用户id
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * 设置设备型号ID
     * @param mid
     *              设备型号ID
     */
    public void setMid(int mid) {
        this.mid = mid;
    }

    /**
     * 设置设备ID
     * @param did
     *          设备ID
     */
    public void setDid(String did) {
        this.did = did;
    }

    /**
     * 设置会话ID
     * @param sid
     *           会话ID
     */
    public  void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * 获取请求的头部信息     *产品ID *版本号 *配置版本 *渠道ID *用户ID *设备型号ID *设备ID *会话ID
     * @return
     *          返回网络请求的通用头部信息
     */
    public String  getHeaders(){
       return appId+"/"+versionCode+"/"+otaVersion+"/"+pid+"/"+uid+"/"+mid+"/"+did+"/"+sid;
    }

    /**
     * 获取应用的版本号
     * @return
     *          返回应用版本号，内部识别号
     */
    public static int getVersionCode()
    {
        try {
        	Context context=GlobalParams.context;
            PackageInfo pi =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

	public long getUid() {
		return uid;
	}
    
}

