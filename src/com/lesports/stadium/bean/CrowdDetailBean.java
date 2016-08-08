package com.lesports.stadium.bean;

import java.io.Serializable;
import java.util.List;

import com.letv.lepaysdk.R.string;

public class CrowdDetailBean implements Serializable{
/**
 * {
 * 
 * detailPicture
    "evaluateCount": 0,
    "id": 37,
    "projectAddress": "1",
    "projectInfo": "
111111111
freight
infoUrl

",
    "projectIntroduction": "1",
    "projectTime": 1458800873000,
    "propagatePicture": "upload/crowdfund/20160323/26dce711654a4dd88c613d6735114fee.jpg",
    "remark": "1",
    "returnCount": 0,
    "returns": [
        {
            "id": 44,
            "returnContent": "1",
            "returnLimit": 1,
            "returnName": "1",
            "returnPrice": 1,
            "totalCount": 0
        }
    ]
}
 */
	private String detailPicture;
	public String getDetailPicture() {
	return detailPicture;
}
public void setDetailPicture(String detailPicture) {
	this.detailPicture = detailPicture;
}
public String getOrderPicture() {
	return orderPicture;
}
public void setOrderPicture(String orderPicture) {
	this.orderPicture = orderPicture;
}
	private String orderPicture;
	private String infoUrl;
	
	public String getInfoUrl() {
	return infoUrl;
}
public void setInfoUrl(String infoUrl) {
	this.infoUrl = infoUrl;
}
	private String freight;
	
	public String getFreight() {
	return freight;
}
public void setFreight(String freight) {
	this.freight = freight;
}
	private String projectTime;
	public String getProjectTime() {
	return projectTime;
}
public void setProjectTime(String projectTime) {
	this.projectTime = projectTime;
}
public String getPropagatePicture() {
	return propagatePicture;
}
public void setPropagatePicture(String propagatePicture) {
	this.propagatePicture = propagatePicture;
}
	private String propagatePicture;
	
	private String evaluateCount;
	public String getEvaluateCount() {
	return evaluateCount;
}
public void setEvaluateCount(String evaluateCount) {
	this.evaluateCount = evaluateCount;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getProjectAddress() {
	return projectAddress;
}
public void setProjectAddress(String projectAddress) {
	this.projectAddress = projectAddress;
}
public String getProjectInfo() {
	return projectInfo;
}
public void setProjectInfo(String projectInfo) {
	this.projectInfo = projectInfo;
}
public String getProjectIntroduction() {
	return projectIntroduction;
}
public void setProjectIntroduction(String projectIntroduction) {
	this.projectIntroduction = projectIntroduction;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getReturnCount() {
	return returnCount;
}
public void setReturnCount(String returnCount) {
	this.returnCount = returnCount;
}
public List<CrowReportBackBean> getList() {
	return list;
}
public void setList(List<CrowReportBackBean> list) {
	this.list = list;
}
	private String id;
	private String projectAddress;
	private String projectInfo;
	private String projectIntroduction;
	private String remark;
	private String returnCount;
	private List<CrowReportBackBean> list;
}
