package com.lesports.stadium.bean;

public class TickOrderBean {
	// 图片路径
	private String picUrl;
	// 价格
	private String price;
	// 数量
	private String wareNumber;
	// 座位号说明
	private String seatNumber;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getWareNumber() {
		return wareNumber;
	}

	public void setWareNumber(String wareNumber) {
		this.wareNumber = wareNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

}
