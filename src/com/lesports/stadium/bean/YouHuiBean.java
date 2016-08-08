package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 优惠券实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
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
public class YouHuiBean implements Serializable{

	/**
	 * 
	 *   "beginTime": 1458284739000,
        "couponCondition": 4,
        "couponPrice": 10,
        "couponType": 1,
        "endTime": 1458803148000,
        "id": 2,
        "status": 1
        myCouponId

	 */
	private String inusing;
	public String getInusing() {
		return inusing;
	}
	public void setInusing(String inusing) {
		this.inusing = inusing;
	}
	private String myCouponId;
	public String getMyCouponId() {
		return myCouponId;
	}
	public void setMyCouponId(String myCouponId) {
		this.myCouponId = myCouponId;
	}
	private boolean isChoise=false;
	public boolean isChoise() {
		return isChoise;
	}
	public void setChoise(boolean isChoise) {
		this.isChoise = isChoise;
	}
	private String beginTime;
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getCouponCondition() {
		return couponCondition;
	}
	public void setCouponCondition(String couponCondition) {
		this.couponCondition = couponCondition;
	}
	public String getCouponPrice() {
		return couponPrice;
	}
	public void setCouponPrice(String couponPrice) {
		this.couponPrice = couponPrice;
	}
	public String getCouponType() {
		return couponType;
	}
	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String couponCondition;
	private String couponPrice;
	private String couponType;
	private String endTime;
	private String id;
	private String status;
}
