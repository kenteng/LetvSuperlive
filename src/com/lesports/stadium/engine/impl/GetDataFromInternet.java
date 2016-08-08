package com.lesports.stadium.engine.impl;

import java.util.Map;

import android.text.TextUtils;

import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.GlobalParams;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : 网络获取服务器数据的工具类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : wang
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class GetDataFromInternet {

	/**
	 * 本类对象
	 */
	private static GetDataFromInternet service;
	/**
     * 单例模式
     *
     * @return 返回网络访问的一个实例
     */
    public static GetDataFromInternet getInStance() {
        if (service == null) {
            service = new GetDataFromInternet();
        }
//    	service = new GetDataFromInternet();
        return service;
    }
	/**
	 * 获取 商品列表数据
	 * @param wondef 回掉函数
	 * classicId=2&sort=1&page=1&rows=20
	 * @param getdataback 回调网络获取的数据的接口对象
	 * @param params 存储网络请求参数的集合
	 * @param httpurl 网络请求的url
	 * @param isUser 是否使用安全验证
	 */
	public void GetInternetData(final GetDatas getdataback,Map<String, String> params,String httpurl,boolean isUser) {
		
//		interViewNet(httpurl, params, new NetLoadListener(){
//			@Override
//			public void onloaderListener(BackData result) {
//				if(result.getNetResultCode() == 0){
//					getdataback.getServerData(result);
//					Log.i("工具类里面","走到这里了么？");
//				}
//			}
//		},isUser);
	}
	
	/**
	 * 
	 * @param url 访问网址
	 * @param params 访问参数
	 * @param listener 回掉函数
	 * @param isUser 是否使用安全验证
	 */
	public void interViewNet(String url,Map<String, String> params,final GetDatas getdataback,boolean isUser,boolean isUseCache){
		NetService service = NetService.getInStance();
		if(!TextUtils.isEmpty(GlobalParams.USER_ID)){
			service.headers.setUid(Integer.parseInt(GlobalParams.USER_ID)); //设置用户id 
		}
//		else{
//			service.headers.setUid(1); //设置用户id 
//		}
		service.setUrl(url); //设置访问路径
		service.setUsedSec(true); //是否使用更高一级的安全验证
		service.setHttpMethod(HttpSetting.POST_MODE); //访问方式：get Post
		service.setUseCache(isUseCache);
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				service.setParams(entry.getKey(), entry.getValue());
			}
		}
		service.loader(new NetLoadListener() {

			@Override
			public void onloaderListener(BackData result) {
				getdataback.getServerData(result);
			}
		});
	}

}
