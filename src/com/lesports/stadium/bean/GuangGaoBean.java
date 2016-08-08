package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 广告列表页面实体类
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class GuangGaoBean implements Serializable {

	/**
	 * [{"advertisementTime":5,"advertisementType":1,
	 * "creatTime":1461118559000,"displayOrder":1,
	 * "entTime":1461945600000,"httpUrl":"www.baidu.com",
	 * "id":4521,"posCode":"", "startTime":1461081600000,"status":1,
	 * "title":"摇一摇背景广告",
	 * "url":"/upload/advertisement/20160506/thumb_375x667.jpg"}]
	 */
	private String advertisementType;
	private String displayOrder;
	private String httpUrl;
	private String status;
	private String creatTime;
	private String entTime;
	private String id;
	private String startTime;
	private String title;
	private String url;
	private String posCode;
	private String resourceId;
	private SenceBean sencebean;
	private RoundGoodsBean goodsbean;
	
	private String resourceType;
	
	
	

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public RoundGoodsBean getGoodsbean() {
		return goodsbean;
	}

	public void setGoodsbean(RoundGoodsBean goodsbean) {
		this.goodsbean = goodsbean;
	}

	public SenceBean getActivitysbean() {
		return sencebean;
	}

	public void setActivitysbean(SenceBean sencebean) {
		this.sencebean = sencebean;
	}

	public String getAdvertisementType() {
		return advertisementType;
	}

	public void setAdvertisementType(String advertisementType) {
		this.advertisementType = advertisementType;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String advertisementTime;

	public String getAdvertisementTime() {
		return advertisementTime;
	}

	public void setAdvertisementTime(String advertisementTime) {
		this.advertisementTime = advertisementTime;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	public String getEntTime() {
		return entTime;
	}

	public void setEntTime(String entTime) {
		this.entTime = entTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
}
