/**
 * 
 */
package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 众筹详情页面的实体类
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

public class AllChipsBean implements Serializable{
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
	public String getmStatus() {
		return mStatus;
	}
	public void setmStatus(String mStatus) {
		this.mStatus = mStatus;
	}
	/**
	 * 众筹状态
	 */
	private String mStatus;
	
	
	/**
	 *   "beginTime": 1457514633000,
        "crowdfundName": "众筹测试",
        "crowdfundStatus": 0,
        "endTime": 1457514633000,
        "hasMoney": 0,
        "id": 1,
        "participation": 0,
        "propagatePicture": "upload/crowdfund/propagetepicture.jpg",
        "targetMoney": 1000

	 */
	/**
	 * 开始时间
	 */
	private String beginTime;
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getCrowdfundName() {
		return crowdfundName;
	}
	public void setCrowdfundName(String crowdfundName) {
		this.crowdfundName = crowdfundName;
	}
	public String getCrowdfundStatus() {
		return crowdfundStatus;
	}
	public void setCrowdfundStatus(String crowdfundStatus) {
		this.crowdfundStatus = crowdfundStatus;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getHasMoney() {
		return hasMoney;
	}
	public void setHasMoney(String hasMoney) {
		this.hasMoney = hasMoney;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParticipation() {
		return participation;
	}
	public void setParticipation(String participation) {
		this.participation = participation;
	}
	public String getPropagatePicture() {
		return propagatePicture;
	}
	public void setPropagatePicture(String propagatePicture) {
		this.propagatePicture = propagatePicture;
	}
	public String getTargetMoney() {
		return targetMoney;
	}
	public void setTargetMoney(String targetMoney) {
		this.targetMoney = targetMoney;
	}
	/**
	 * 众筹名称
	 */
	private String crowdfundName;
	/**
	 * 众筹类型 0 未开始。1进行中，2已结束
	 */
	private String crowdfundStatus;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 现在已有金额
	 */
	private String hasMoney;
	/**
	 * id
	 */
	private String id;
	/**
	 * participation
	 */
	private String participation;
	/**
	 * 图片url
	 */
	private String propagatePicture;
	/**
	 * 目标金额
	 */
	private String targetMoney;

}
