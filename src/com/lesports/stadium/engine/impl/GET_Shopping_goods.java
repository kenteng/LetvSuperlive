package com.lesports.stadium.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.bean.Goods;
import com.lesports.stadium.engine.GetGoods;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.ConstantValue;

public class GET_Shopping_goods {

	/**
	 * 获取 商品列表数据
	 * @param wondef 回掉函数
	 * classicId=2&sort=1&page=1&rows=20
	 */
	public void GetShoppGoods(final GetGoods goodlslist) {
		Map<String, String>params = new HashMap<String, String>();
		params.put("flag", "D");
		params.put("goodsId", "1");
		params.put("sort", "3");
		params.put("classicId", "2");
		params.put("price", "12.00");
		interViewNet(ConstantValue.SHOPP, params, new NetLoadListener(){
			
			@Override
			public void onloaderListener(BackData result) {
				if(result!=null && result.getNetResultCode() == 0){
					Object obj =  result.getObject();
					Gson gson = new Gson();
					ArrayList<Goods> groups = gson.fromJson(obj.toString(), new TypeToken<List<Goods>>() {
					}.getType());
					goodlslist.getAllGoods(groups);
				}
			}
		});
	}
	
	/**
	 * 
	 * @param url 访问网址
	 * @param params 访问参数
	 * @param listener 回掉函数
	 */
	private void interViewNet(String url,Map<String, String> params,final NetLoadListener listener ){
		NetService service = NetService.getInStance();
//		service.headers.setUid(Integer.parseInt(GlobalParams.USER_ID)); //设置用户id 
		service.setUrl(url); //设置访问路径
		service.setUsedSec(false); //是否使用更高一级的安全验证
		service.setHttpMethod(HttpSetting.POST_MODE); //访问方式：get Post
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				service.setParams(entry.getKey(), entry.getValue());
			}
		}
		service.loader(new NetLoadListener() {

			@Override
			public void onloaderListener(BackData result) {
				
				listener.onloaderListener(result);
			}
		});
	}

}
