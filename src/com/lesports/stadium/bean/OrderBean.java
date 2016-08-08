package com.lesports.stadium.bean;

import java.io.Serializable;

/**
 * ***************************************************************
 * 
 * @Desc :订单界面数据实体类
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
public class OrderBean implements Serializable{
	
	
	/**
	 * {
"resultCode": "000",
"ordersType": 0,
"object": {
    "amount": 560,
    "companies": "顺丰",
    "courier": 1,
    "createBy": 0,
    "createTime": 1458564989468,
    "freight": 5,
    "id": 88739,
    "orderAmount": 555,
    "orderNumber": "2016032100095",
    "orderType": 0,
    "ordersType": 0,
    "payMent": 0,
    "payStatus": 0,
    "position": "五排19号",
    "remark": "顺丰",
    "status": 1
}
}
	 */
	
	private String resultCode;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getOrdersType() {
		return ordersType;
	}
	public void setOrdersType(String ordersType) {
		this.ordersType = ordersType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCompanies() {
		return companies;
	}
	public void setCompanies(String companies) {
		this.companies = companies;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCourier() {
		return courier;
	}
	public void setCourier(String courier) {
		this.courier = courier;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPayMent() {
		return payMent;
	}
	public void setPayMent(String payMent) {
		this.payMent = payMent;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String ordersType;
	private String amount;
	private String companies;
	private String createBy;
	private String courier;
	private String createTime;
	private String freight;
	private String id;
	private String orderAmount;
	private String orderNumber;
	private String orderType;
	private String payMent;
	private String payStatus;
	private String position;
	private String remark;
	private String status;
	

}
