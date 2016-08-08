package com.lesports.stadium.bean;
/**
 * 
 * ***************************************************************
 * 
 * @Desc : (预约活动实体类)
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
public class YuYueActivityBean {

	/**
	 * "activityId": 1,
        "bStatus": 1,
        "bid": 67,
        "userId": 1

	 */
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
