package com.lesports.stadium.engine;

import java.util.ArrayList;

import com.lesports.stadium.bean.Goods;
/**
 * 
 * @ClassName:  GetGoods   
 * @Description:   解析网络返回数据 获取商品列表
 * @author: 王新年 
 * @date:   2015-12-28 下午7:28:47   
 *
 */
public interface GetGoods {
	/**
	 * 
	 * @Title: getAllGoods   
	 * @Description: 获取商品列表   
	 * @param: @param goodls      
	 * @return: void      
	 * @throws
	 */
	void getAllGoods(ArrayList<Goods> goodls);
}
