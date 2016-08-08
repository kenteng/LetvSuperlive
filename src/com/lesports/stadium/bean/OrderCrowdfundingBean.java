package com.lesports.stadium.bean;
/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 众筹订单item数据源实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class OrderCrowdfundingBean {

	/**
	 * 名称
	 */
	private String mName;
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * 类别0 未支付 1待发货。2待收货
	 */
	private int status;
}
