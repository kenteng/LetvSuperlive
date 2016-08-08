package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 优惠券实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class CouponBean implements Serializable{
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private boolean isChoise=false;
	public boolean isChoise() {
		return isChoise;
	}
	public void setChoise(boolean isChoise) {
		this.isChoise = isChoise;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	private int type;

}
