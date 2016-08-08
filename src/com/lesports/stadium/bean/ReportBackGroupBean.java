package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc : 众筹详情页面中展示回报部分数据的组的实体类
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
public class ReportBackGroupBean implements Serializable{
	
	/**
	 * 金额
	 */
	private String price;
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
	}
	/**
	 * zongrenshu
	 */
	private String peopleNum;
	/**
	 * 人数限定
	 */
	private String numLimit="0";
	public String getNumLimit() {
		return numLimit;
	}
	public void setNumLimit(String numLimit) {
		this.numLimit = numLimit;
	}
	/**
	 * id
	 */
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private CrowReportBackBean childBean;
	public CrowReportBackBean getChildBean() {
		return childBean;
	}
	public void setChildBean(CrowReportBackBean childBean) {
		this.childBean = childBean;
	}
	/**
	 * 众筹详情实体类
	 */
	private AllChipsBean bean;
	public AllChipsBean getBean() {
		return bean;
	}
	public void setBean(AllChipsBean bean) {
		this.bean = bean;
	}
	private CrowdDetailBean cbean;
	public CrowdDetailBean getCbean() {
		return cbean;
	}
	public void setCbean(CrowdDetailBean cbean) {
		this.cbean = cbean;
	}
	/**
	 * 
	 */
	

}
