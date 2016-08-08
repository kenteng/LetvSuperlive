package com.lesports.stadium.bean;

import java.io.Serializable;
import java.util.List;

public class CallBackData implements Serializable {

	public int page  ;
	public int totalPage  ;
	public int rows  ;
	public List<Data> data;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data implements Serializable  {

	public int id  ;
	public long createTime  ;
	public int balance  ;
	public String remark  ;
	public int userId  ;
	public String getFrom  ;
	public int integral  ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getGetFrom() {
		return getFrom;
	}
	public void setGetFrom(String getFrom) {
		this.getFrom = getFrom;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	
	}

	

}
