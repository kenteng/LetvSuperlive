package com.lesports.stadium.bean;

import java.io.Serializable;

public class MyYuyueBean implements Serializable{

	/**
	 * "activityId": 1,
        "bStatus": 1,
        "bid": 67,
        "userId": 1
        
        //还有一个是活动的实体类

	 */
	private SenceBean mSenceBean;
	public SenceBean getmSenceBean() {
		return mSenceBean;
	}
	public void setmSenceBean(SenceBean mSenceBean) {
		this.mSenceBean = mSenceBean;
	}
	private String  activityId;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getbStatus() {
		return bStatus;
	}
	public void setbStatus(String bStatus) {
		this.bStatus = bStatus;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	private String bStatus;
	private String bid;
	private String userId;
	
}
