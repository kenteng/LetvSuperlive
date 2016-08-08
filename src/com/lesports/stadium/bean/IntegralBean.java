package com.lesports.stadium.bean;

import java.util.List;

public class IntegralBean {
	private String page;
	private String rows;
	private String totalPage;
	private List<IntegralDataBean> data;

	
	public String getPage() {
		return page;
	}


	public void setPage(String page) {
		this.page = page;
	}


	public String getRows() {
		return rows;
	}


	public void setRows(String rows) {
		this.rows = rows;
	}


	public String getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}


	public List<IntegralDataBean> getData() {
		return data;
	}


	public void setData(List<IntegralDataBean> data) {
		this.data = data;
	}




}
