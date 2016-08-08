package com.lesports.stadium.engine;

import com.lesports.stadium.net.BackData;
/**
 * 
 * @ClassName:  GetGoods   
 * @Description:   解析网络返回数据 
 * @author: 王新年 
 * @date:   2015-12-28 下午7:28:47   
 *
 */
public interface GetDatas {
	/**
	 * 
	 * @Title: getAllGoods   
	 * @Description: 获取商品列表   
	 * @param: @param goodls      
	 * @return: void      
	 * @throws
	 * 该回调接口的目的是为了将网络获取的数据进行返回
	 */
	void getServerData(BackData data);
}
