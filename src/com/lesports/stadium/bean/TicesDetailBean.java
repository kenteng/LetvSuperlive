package com.lesports.stadium.bean;
/**
 * 购票订单详细数据实体类
 * @author JZKJ-LWC
 *"{\"picture\":\"http://interface.jingzhaokeji.com:8070/letv_mgr/upload/img/image001.png\",\"priceTag\":\"内场\",\"seatNumber\":\"1排12B\",\"productName\":\"演唱会\"}",
 */
public class TicesDetailBean {

	private String picture;
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getPriceTag() {
		return priceTag;
	}
	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	

	private String priceTag;
	private String seatNumber;
	private String productName;
	private String freight;
	private String amount;
}
