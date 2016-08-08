package com.lesports.stadium.bean;

public class GetIntegralBean {
	private String id;
	private String integralRechargeCount;
	private double money;
	private String remark;
	public String getId() {
		return id;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIntegralRechargeCount() {
		return integralRechargeCount;
	}
	public void setIntegralRechargeCount(String integralRechargeCount) {
		this.integralRechargeCount = integralRechargeCount;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
