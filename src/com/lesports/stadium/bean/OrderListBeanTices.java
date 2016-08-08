package com.lesports.stadium.bean;

import java.io.Serializable;

public class OrderListBeanTices implements Serializable{
	/**
	 *    "goodsId": 1,
                    "id": 20164015,
                    "pinfo": "{\"picture\":\"http://interface.jingzhaokeji.com:8070/letv_mgr/upload/img/image001.png\",\"priceTag\":\"内场\",\"seatNumber\":\"1排12B\",\"productName\":\"演唱会\"}",
                    "price": 0.01,
                    "wareNumber": 1

	 */
	private String goodsId;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPinfo() {
		return pinfo;
	}
	public void setPinfo(String pinfo) {
		this.pinfo = pinfo;
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
	private String id;
	private String pinfo;
	private String price;
	private String wareNumber;
}
