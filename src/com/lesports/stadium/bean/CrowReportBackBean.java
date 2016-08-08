package com.lesports.stadium.bean;

import java.io.Serializable;

public class CrowReportBackBean implements Serializable{
	
	/**
	 *    "id": 44,
            "returnContent": "1",
            "returnLimit": 1,
            "returnName": "1",
            "returnPrice": 1,
            "totalCount": 0

	 */
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReturnContent() {
		return returnContent;
	}
	public void setReturnContent(String returnContent) {
		this.returnContent = returnContent;
	}
	public String getReturnLimit() {
		return returnLimit;
	}
	public void setReturnLimit(String returnLimit) {
		this.returnLimit = returnLimit;
	}
	public String getReturnName() {
		return returnName;
	}
	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}
	public String getReturnPrice() {
		return returnPrice;
	}
	public void setReturnPrice(String returnPrice) {
		this.returnPrice = returnPrice;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	private String returnContent;
	private String returnLimit;
	private String returnName;
	private String returnPrice;
	private String totalCount;

}
